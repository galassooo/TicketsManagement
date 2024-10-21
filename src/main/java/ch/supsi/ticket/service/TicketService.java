package ch.supsi.ticket.service;

import ch.supsi.ticket.model.Ticket;
import ch.supsi.ticket.model.TicketDTO;
import ch.supsi.ticket.model.User;
import ch.supsi.ticket.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserService userService;

    public Optional<Ticket> getTicket(Long id) {
        return ticketRepository.findById(id);
    }


    public List<Ticket> getTickets() {
        return ticketRepository.findAll();
    }


    public Ticket createTicket(TicketDTO ticketDTO) {
        Optional<User> user = userService.getUser(ticketDTO.getUsername());

        if(user.isPresent()) {
            User usr = user.get();
            Ticket ticket = new Ticket(usr, ticketDTO.getDescription(), ticketDTO.getTitle());
            return ticketRepository.save(ticket);
        }else{
            User usr = new User(ticketDTO.getUsername(), "Not available", "Not available");
            userService.createUser(usr);
            return ticketRepository.save(new Ticket(usr, ticketDTO.getDescription(), ticketDTO.getTitle()));
        }
    }

    public Ticket updateTicket(Long id, TicketDTO ticketDto ) {
        Optional<Ticket> oldTicket = ticketRepository.findById(id);
        if(oldTicket.isPresent()) {
            Ticket tck = oldTicket.get();
            tck.setTitle(ticketDto.getTitle());
            tck.setDescription(ticketDto.getDescription());

            if(ticketDto.getUsername() != null && ticketDto.getUsername().equals(tck.getUser().getUsername())) {
                return null; //non posso aggiornare username, è chiave!!
            }

            return ticketRepository.save(tck);
        }
        return null;
    }

    public boolean deleteTicket(Long id) {
        if(ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
