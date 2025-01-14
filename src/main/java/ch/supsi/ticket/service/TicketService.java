package ch.supsi.ticket.service;

import ch.supsi.ticket.model.*;
import ch.supsi.ticket.repository.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AttachmentService attachmentService;

    public Optional<Ticket> getTicket(Long id) {
        return ticketRepository.findById(id);
    }


    public List<Ticket> getTickets() {
        return ticketRepository.findAll();
    }


    public Ticket createTicket(TicketDTO ticketDTO, User user) {
        System.out.println("description: "+ticketDTO.getDescription());
        System.out.println("title: "+ticketDTO.getTitle());
        System.out.println("Type: "+ticketDTO.getType());
        System.out.println("username: "+ticketDTO.getUsername());

        if(ticketDTO.getAttachment() != null)
            attachmentService.save(ticketDTO.getAttachment());
        return ticketRepository.save(new Ticket(user ,ticketDTO.getDescription(), ticketDTO.getTitle(), ticketDTO.getType(), ticketDTO.getAttachment(), ticketDTO.getAssignee(), ticketDTO.getEstimate(), ticketDTO.getDue_date() ));
    }

    public Ticket updateSpentTime(Long id, TicketDTO ticketDTO) {
        Optional<Ticket> oldTicket = ticketRepository.findById(id);
        if(oldTicket.isPresent()) {
            Ticket tck = oldTicket.get();

            tck.setTimeSpent(ticketDTO.getTimeSpent());
            tck.setTitle(tck.getTitle());
            tck.setType(tck.getType());
            tck.setAssignee(tck.getAssignee());
            tck.setEstimate(tck.getEstimate());
            tck.setDue_date(tck.getDue_date());
            tck.setDescription(tck.getDescription());
            tck.setAttachment(tck.getAttachment());
            tck.setUser(tck.getUser());
            tck.setStatus(tck.getStatus());
            tck.setType(tck.getType());

            return ticketRepository.save(tck);
        }
        return null;
    }
    public Ticket updateTicket(Long id, TicketDTO ticketDto) {
        Optional<Ticket> oldTicket = ticketRepository.findById(id);
        if(oldTicket.isPresent()) {
            Ticket tck = oldTicket.get();

            tck.setTitle(ticketDto.getTitle());
            tck.setDescription(ticketDto.getDescription());
            tck.setStatus(ticketDto.getStatus());
            tck.setType(ticketDto.getType());

            if(ticketDto.getAttachment() != null) {
                attachmentService.save(ticketDto.getAttachment());
                tck.setAttachment(ticketDto.getAttachment());
            }

            if(ticketDto.getUsername() != null && !ticketDto.getUsername().equals(tck.getUser().getUsername())) {
                Optional<User> newUser = userService.getUser(ticketDto.getUsername());
                if(newUser.isPresent()) {
                    tck.setUser(newUser.get());
                } else {
                    throw new RuntimeException("User not found");
                }
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

    public List<Ticket> getAllTicketsWithString(String q){
        return ticketRepository.findAll().stream()
                .filter(ticket -> (ticket.getDescription() + " " +
                        ticket.getTitle() + " " +
                        ticket.getUser().getUsername())
                        .toLowerCase()
                        .contains(q.toLowerCase()))
                .collect(Collectors.toList());
    }
}
