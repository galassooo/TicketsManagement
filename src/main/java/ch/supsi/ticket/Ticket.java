package ch.supsi.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Data
public class Ticket {

    UUID uuid = UUID.randomUUID();

    private String author;
    private String description;
    private String title;

}
