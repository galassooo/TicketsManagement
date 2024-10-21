package ch.supsi.ticket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String title;

    public Ticket(User user, String description, String title) {
        this.user = user;
        this.description = description;
        this.title = title;
    }

}
