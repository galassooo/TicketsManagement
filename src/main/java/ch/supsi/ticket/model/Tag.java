package ch.supsi.ticket.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Tag {


    @Id
    @GeneratedValue
    private Long id;

    private String tagValue;

    @JsonBackReference
    @ManyToMany
    @JoinTable(
            name = "ticket_tag",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    )
    private List<Ticket> tickets = new ArrayList<>();
}
