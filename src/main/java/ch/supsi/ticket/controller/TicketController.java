package ch.supsi.ticket.controller;


import ch.supsi.ticket.Ticket;
import ch.supsi.ticket.TicketApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @GetMapping("{id}")
    private ResponseEntity<Ticket> getTicket(@PathVariable("id") String id) {
        Optional<Ticket> ticket = TicketApplication.ALL_TICKETS.stream().filter(
                t -> t.getUuid().toString().equals(id)).findFirst();
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    private List<Ticket> getTickets() {
        return TicketApplication.ALL_TICKETS;
    }

    @PostMapping
    private ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) throws URISyntaxException {
        TicketApplication.ALL_TICKETS.add(ticket);
        return ResponseEntity.created(new URI("/tickets/" + ticket.getUuid().toString())).body(ticket);
    }

    @PutMapping("{id}")
    private ResponseEntity<Ticket> updateTicket(@PathVariable("id") String id,@RequestBody Ticket ticket ) {
        Optional<Ticket> toBeUpdate = TicketApplication.ALL_TICKETS.stream().filter(
                t -> t.getUuid().toString().equals(id)).findFirst();
        if(toBeUpdate.isPresent()) {
            toBeUpdate.get().setDescription(ticket.getDescription());
            toBeUpdate.get().setTitle(ticket.getTitle());
            toBeUpdate.get().setAuthor(ticket.getAuthor());

            return ResponseEntity.ok(toBeUpdate.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }

    }
    @DeleteMapping("{id}")
    private ResponseEntity<String> deleteTicket(@PathVariable("id") String id) {
        boolean removed = TicketApplication.ALL_TICKETS.removeIf(t -> t.getUuid().toString().equals(id));

        if(removed){
            return ResponseEntity.status(200).body("{\n\"success\": true\n}");
        }else {
            return ResponseEntity.status(404).build();
        }
    }
}
