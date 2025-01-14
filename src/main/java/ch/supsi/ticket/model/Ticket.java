package ch.supsi.ticket.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate due_date;

    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime estimate;

    @ManyToOne
    private User assignee;

    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeSpent;

    @JsonManagedReference
    @ManyToMany(mappedBy = "tickets")
    private Set<User> watchers = new HashSet<>();

    public Ticket(User user, String description, String title, TicketType type, Attachment attachment) {
        this.user = user;
        this.description = description;
        this.title = title;
        this.status = Status.OPEN;
        this.type = type;
        this.date = LocalDateTime.now();
        this.attachment = attachment;
    }
    public Ticket(User user, String description, String title, TicketType type, Attachment attachment, User assignee, LocalTime estimate, LocalDate due_date) {
        this.user = user;
        this.description = description;
        this.title = title;
        this.status = Status.OPEN;
        this.type = type;
        this.assignee = assignee;
        this.due_date = due_date;
        this.estimate = estimate;
        timeSpent = LocalTime.of(0,0,0);
        this.date = LocalDateTime.now();
        this.attachment = attachment;
    }
}