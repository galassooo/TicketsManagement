package ch.supsi.ticket.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String username;

    private String name;

    private String surname;

    private String password;
    @Enumerated
    private UserRole role;

    public User(String username, String name, String surname, String password, UserRole role) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.role = role;
    }

    @JsonBackReference
    @ManyToMany
    @JoinTable(
            name = "ticket_user",
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    )
    private Set<Ticket> tickets = new HashSet<>();

}
