package ch.supsi.ticket.controller;



import ch.supsi.ticket.model.Ticket;
import ch.supsi.ticket.model.TicketDTO;
import ch.supsi.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/old")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("{id}")
    private ResponseEntity<Ticket> getTicket(@PathVariable("id") Long id) {
        Optional<Ticket> ticket = ticketService.getTicket(id);
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    private List<Ticket> getTickets() {
        return ticketService.getTickets();
    }

    @PostMapping
    private ResponseEntity<Ticket> createTicket(@RequestBody TicketDTO ticketDTO) throws URISyntaxException {
        return null;
    }

    @PutMapping("{id}")
    private ResponseEntity<Ticket> updateTicket(@PathVariable("id") Long id,@RequestBody TicketDTO ticket ) {
        Ticket tck = ticketService.updateTicket(id, ticket);
        if(tck != null) {

            return ResponseEntity.ok(tck);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("{id}")
    private ResponseEntity<String> deleteTicket(@PathVariable("id") Long id) {
        if(ticketService.deleteTicket(id)) {
            return ResponseEntity.status(200).body("{\n\"success\": true\n}");
        }else {
            return ResponseEntity.status(404).build();
        }
    }
}
