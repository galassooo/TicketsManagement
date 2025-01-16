package ch.supsi.ticket.service;

import ch.supsi.ticket.model.Milestone;
import ch.supsi.ticket.model.Tag;
import ch.supsi.ticket.model.User;
import ch.supsi.ticket.repository.MilestoneRepository;
import ch.supsi.ticket.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository repo;


    public void createMilestone(Tag tag) {
        repo.save(tag);
    }

    public List<Tag> getMilestones() {
        return repo.findAll();
    }
}