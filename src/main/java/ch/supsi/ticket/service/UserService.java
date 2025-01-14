package ch.supsi.ticket.service;

import ch.supsi.ticket.model.Ticket;
import ch.supsi.ticket.model.User;
import ch.supsi.ticket.model.UserRole;
import ch.supsi.ticket.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    @Autowired
    private UserRepository repo;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    public User registerNewUser(String username, String name, String surname, String password) {
        if (repo.existsById(username)) {
            throw new RuntimeException("Username already exists");
        }

        User newUser = new User(
                username,
                name,
                surname,
                encoder.encode(password),
                UserRole.ROLE_USER
        );

        return repo.save(newUser);
    }

    @PostConstruct
    private void init() {
        if (!repo.existsById("_admin")) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User use = new User("_admin", "name", "surname", encoder.encode("password"), UserRole.ROLE_ADMIN);
            repo.save(use);
        }
    }

    public Optional<User> getUser(String id) {
        return repo.findById(id);
    }


    public List<User> getUsers() {
        return repo.findAll();
    }


    public void createUser(User user) {
        repo.save(user);
    }

    public boolean updateUser(String id, User user) {
        Optional<User> oldUder = repo.findById(id);
        if (oldUder.isPresent()) {
            User usr = oldUder.get();
            usr.setName(user.getName());
            usr.setSurname(user.getSurname());

            repo.save(usr);
            return true;
        }
        return false;
    }

    public boolean deleteUser(String id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public void addWatchedTicket(String username, Ticket ticket) {
        Optional<User> userOpt = repo.findById(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getTickets() == null) {
                user.setTickets(new HashSet<>());
            }
            user.getTickets().add(ticket);

            // Salva entrambe le entità
            repo.save(user);
        }
    }
}
