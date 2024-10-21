package ch.supsi.ticket.service;

import ch.supsi.ticket.model.Ticket;
import ch.supsi.ticket.model.User;
import ch.supsi.ticket.repository.TicketRepository;
import ch.supsi.ticket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    @Autowired
    private UserRepository repo;

    public Optional<User> getUser(String id) {
        return repo.findById(id);
    }


    public List<User> getUsers() {
        return repo.findAll();
    }


    public void createUser(User user) {
        repo.save(user);
    }

    public boolean updateUser(String id, User user ) {
        Optional<User> oldUder = repo.findById(id);
        if(oldUder.isPresent()) {
            User usr = oldUder.get();
            usr.setName(user.getName());
            usr.setSurname(user.getSurname());

            repo.save(usr);
            return true;
        }
        return false;
    }

    public boolean deleteUser(String id) {
        if(repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

}
