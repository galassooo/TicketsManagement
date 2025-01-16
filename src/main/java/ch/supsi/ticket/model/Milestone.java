package ch.supsi.ticket.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Milestone {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private LocalDate dueDate;

    @Column
    private LocalDate creationDate;

    @Column
    private boolean completed;

    @ManyToOne
    private User author;

    @JsonBackReference
    @ManyToMany
    @JoinTable(
            name = "ticket_milestone",
            joinColumns = @JoinColumn(name = "milestone_id"),
            inverseJoinColumns = @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    )
    private List<Ticket> tickets = new ArrayList<>();


    public Milestone(String title, String description, LocalDate dueDate, User author) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.author = author;
        creationDate = LocalDate.now();
        completed = false;
    }
}
