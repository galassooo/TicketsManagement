package ch.supsi.ticket.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Data
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User user;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String title;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column
    private TicketType type;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime date;

    @OneToOne
    private Attachment attachment;

    @JsonManagedReference
    @ManyToMany(mappedBy = "tickets")
    private List<Milestone> milestones = new ArrayList<>();


    @JsonManagedReference
    @ManyToMany(mappedBy = "tickets")
    private List<Tag> tags = new ArrayList<>();

    public Ticket(User user, String description, String title, TicketType type, Attachment attachment) {
        this.user = user;
        this.description = description;
        this.title = title;
        this.status = Status.OPEN;
        this.type = type;
        this.date = LocalDateTime.now();
        this.attachment = attachment;
    }
}