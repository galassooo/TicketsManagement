package ch.supsi.ticket.model;

import ch.supsi.ticket.model.TicketType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate due_date;
    private LocalTime estimate;
    private User assignee;
    private LocalTime timeSpent;
}