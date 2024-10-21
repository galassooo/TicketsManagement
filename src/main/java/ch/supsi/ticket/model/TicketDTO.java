package ch.supsi.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @Data @NoArgsConstructor
public class TicketDTO {

    private String title;
    private String description;
    private String username;
}
