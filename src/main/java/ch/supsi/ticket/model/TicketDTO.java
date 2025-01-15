package ch.supsi.ticket.model;

import ch.supsi.ticket.model.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TicketDTO {
    private String title;
    private String description;
    private String username;
    private TicketType type;
    private Status status;
    private Attachment attachment;
}