package ch.supsi.ticket.service;


import ch.supsi.ticket.model.Milestone;
import ch.supsi.ticket.model.Ticket;
import ch.supsi.ticket.model.User;
import ch.supsi.ticket.repository.AttachmentRepository;
import ch.supsi.ticket.repository.MilestoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MilestoneService {

    @Autowired
    private MilestoneRepository repo;


    public void createMilestone(Milestone milestone, User user) {
        Milestone m = new Milestone(milestone.getTitle(), milestone.getDescription(), milestone.getDueDate(), user);
        repo.save(m);
    }

    public List<Milestone> getMilestones() {
        return repo.findAll();
    }

    public Optional<Milestone> getMilestone(Long id) {
        return repo.findById(id);
    }

    public void addTicket(Ticket ticket, Milestone milestone){
        Milestone updated = new Milestone();
        updated.setId(milestone.getId());
        updated.setTitle(milestone.getTitle());
        updated.setDescription(milestone.getDescription());
        updated.setAuthor(milestone.getAuthor());
        updated.setCompleted(milestone.isCompleted());
        updated.setDueDate(milestone.getDueDate());
        updated.setCreationDate(milestone.getCreationDate());
        updated.setTickets(milestone.getTickets());

        updated.getTickets().add(ticket);

        repo.save(updated);
    }

    public Optional<Milestone> complete(long id){
        Optional<Milestone> mls = repo.findById(id);
        if(!mls.isPresent()){
            return Optional.empty();
        }
          Milestone  milestone = mls.get();
        Milestone updated = new Milestone();
        updated.setId(milestone.getId());
        updated.setTitle(milestone.getTitle());
        updated.setDescription(milestone.getDescription());
        updated.setAuthor(milestone.getAuthor());
        updated.setCompleted(true);
        updated.setDueDate(milestone.getDueDate());
        updated.setCreationDate(milestone.getCreationDate());
        updated.setTickets(milestone.getTickets());


        repo.save(updated);
        return Optional.of(updated);
    }
}

