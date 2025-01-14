package ch.supsi.ticket.controller;

import ch.supsi.ticket.model.*;

import ch.supsi.ticket.service.TicketService;
import ch.supsi.ticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping("/tickets")
public class TicketCtrl {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String index(Model model) {
        var list = ticketService.getTickets();

        org.springframework.security.core.userdetails.User userTmp =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> usr = userService.getUser(userTmp.getUsername());

        model.addAttribute("tickets", list);
        model.addAttribute("status", Status.values());
        model.addAttribute("currentUser",usr.orElse(null));
        return "index";
    }

    @GetMapping("{id}")
    public String ticketPage(@PathVariable Long id, Model model) {
        var ticket = ticketService.getTicket(id);
        model.addAttribute("ticket", ticket);

        org.springframework.security.core.userdetails.User userTmp =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> usr = userService.getUser(userTmp.getUsername());

        model.addAttribute("currentUser", usr.get());

        return "ticket";
    }

    @GetMapping("new")
    public String showTicketForm(Model model) {
        model.addAttribute("ticketTypes", TicketType.values());
        var utenti = userService.getUsers();
        model.addAttribute("users",utenti);
        return "form";
    }

    @PostMapping("new")
    public String createTicket(@ModelAttribute TicketDTO ticketDTO, @RequestParam("file") MultipartFile file) throws IOException {
        // Prendiamo l'utente direttamente dal contesto di sicurezza
        org.springframework.security.core.userdetails.User userTmp =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> usr = userService.getUser(userTmp.getUsername());

        if(file != null)
            validateFile(ticketDTO, file);
        ticketService.createTicket(ticketDTO, usr.get());
        return "redirect:/tickets";
    }

    private void validateFile(@ModelAttribute TicketDTO ticketDTO, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            Attachment attachment = new Attachment();
            attachment.setSize(file.getSize());
            attachment.setName(file.getOriginalFilename());
            attachment.setData(file.getBytes());
            ticketDTO.setAttachment(attachment);
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        var ticketOpt = ticketService.getTicket(id);
        if (!ticketOpt.isPresent()) {
            return "redirect:/tickets";
        }
        model.addAttribute("ticket", ticketOpt.get());
        model.addAttribute("ticketTypes", TicketType.values());
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("statusTypes", Status.values());
        return "edit";
    }

    @GetMapping("/{id}/{file}")
    public ResponseEntity<byte[]> showEditForm(@PathVariable Long id, @PathVariable String file, Model model) {
        var ticketOpt = ticketService.getTicket(id);
        if (ticketOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(ticketOpt.get().getAttachment().getData());
    }

    @PostMapping("/{id}/edit")
    public String updateTicket(@PathVariable Long id, @ModelAttribute TicketDTO ticketDTO, @RequestParam("file") MultipartFile file) throws IOException {
        validateFile(ticketDTO, file);
        ticketService.updateTicket(id, ticketDTO);
        return "redirect:/tickets/{id}";
    }

    @GetMapping("/{id}/delete")
    public String deleteTicket(@PathVariable Long id) {
        if (ticketService.deleteTicket(id)) {
            return "redirect:/tickets";
        }
        return "redirect:/tickets?error=delete-failed";
    }



    @GetMapping("/login")
    public String loginError(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            return "loginFailed";
        }
        return "login";
    }

    @GetMapping("/register")
    public String loginRegister() {
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String name,
                               @RequestParam String surname,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/tickets/register";
        }

        try {
            userService.registerNewUser(username, name, surname, password);
            return "redirect:/tickets/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/tickets/register";
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Ticket>> searchParam(@RequestParam("q") String query) {
        List<Ticket> filteredTickets = ticketService.getAllTicketsWithString(query);
        if (filteredTickets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(filteredTickets);
    }

    @GetMapping("/board")
    public String showBoard(Model model) {
        var list = ticketService.getTickets();
        model.addAttribute("tickets", list);
        var allOpened = ticketService.getTickets().stream().filter(t -> t.getStatus() == Status.OPEN).toList().size();
        var allInProgress = ticketService.getTickets().stream().filter(t -> t.getStatus() == Status.IN_PROGRESS).toList().size();

        AtomicInteger totalTimeMinutes = new AtomicInteger();
        ticketService.getTickets().forEach(
                ticket -> {
                    if((ticket.getStatus() == Status.OPEN || ticket.getStatus() == Status.IN_PROGRESS) && ticket.getEstimate() != null)
                        totalTimeMinutes.addAndGet(ticket.getEstimate().getMinute() + ticket.getEstimate().getHour() * 60);
                }
        );

        AtomicInteger totalSpentMinutes = new AtomicInteger();
        ticketService.getTickets().forEach(
                ticket -> {
                    if((ticket.getStatus() == Status.OPEN || ticket.getStatus() == Status.IN_PROGRESS) && ticket.getTimeSpent() != null)
                        totalSpentMinutes.addAndGet(ticket.getTimeSpent().getMinute() + ticket.getTimeSpent().getHour() * 60);
                }
        );

        double progress = (double) totalSpentMinutes.get() / totalTimeMinutes.get() *   100;
        System.out.println("------------------------------------");
        System.out.println("totalSpent="+ totalSpentMinutes.get());
        System.out.println("totalTimeMinutes="+ totalTimeMinutes.get());
        System.out.println("progress="+ progress);
        System.out.println("------------------------------------");
        model.addAttribute("allOpened", allOpened);
        model.addAttribute("allInProgress", allInProgress);
        model.addAttribute("progress", (int) progress);

        return "board";
    }

    @GetMapping("{id}/editTime")
    public String editTime(@PathVariable Long id,  Model model) {
        var ticket = ticketService.getTicket(id);
        model.addAttribute("ticket", ticket);
        return "editTime";
    }


    @PostMapping("{id}/editTime")
    public String saveTimeEdite(@PathVariable Long id, @ModelAttribute TicketDTO ticketDTO) {
        ticketService.updateSpentTime(id, ticketDTO);
        return "redirect:/tickets/{id}";
    }

    @GetMapping("{id}/info")
    public ResponseEntity<Ticket> requestInfoTime(@PathVariable Long id) {
        Optional<Ticket> tck = ticketService.getTicket(id);
        System.out.println("A");
        return tck.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }


    @GetMapping("watched")
    public String watchedTickets(Model model) {

        org.springframework.security.core.userdetails.User userTmp =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> usr = userService.getUser(userTmp.getUsername());


        var tickets = ticketService.getTickets();
        model.addAttribute("tickets", tickets);
        model.addAttribute("currentUser", usr.get());
        return "watched";
    }

    @GetMapping("{id}/watch")
    public String watchTicket(@PathVariable Long id) {
        org.springframework.security.core.userdetails.User userTmp =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        userService.addWatchedTicket(userTmp.getUsername(), ticketService.getTicket(id).get());

        return "index";
    }

}

