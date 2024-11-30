package ch.supsi.ticket.controller;

import ch.supsi.ticket.model.TicketDTO;
import ch.supsi.ticket.model.Status;
import ch.supsi.ticket.model.Attachment;
import ch.supsi.ticket.model.TicketType;

import ch.supsi.ticket.model.User;
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
import java.util.Optional;

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
        model.addAttribute("tickets", list);
        model.addAttribute("status", Status.values());
        return "index";
    }

    @GetMapping("{id}")
    public String ticketPage(@PathVariable Long id, Model model) {
        var ticket = ticketService.getTicket(id);
        model.addAttribute("ticket", ticket);
        return "ticket";
    }

    @GetMapping("new")
    public String showTicketForm(Model model) {
        // Assumendo che tu abbia un enum TicketType
        model.addAttribute("ticketTypes", TicketType.values());
        // Prendi tutti gli utenti dal service
        model.addAttribute("users", userService.getUsers());
        return "form";
    }

    @PostMapping("new")
    public String createTicket(@ModelAttribute TicketDTO ticketDTO, @RequestParam("file") MultipartFile file) throws IOException {
        org.springframework.security.core.userdetails.User userTmp = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> usr = userService.getUser(userTmp.getUsername());

        if (file != null && !file.isEmpty()) {
            Attachment attachment = new Attachment();
            attachment.setSize(file.getSize());
            attachment.setName(file.getOriginalFilename());
            attachment.setData(file.getBytes());
            ticketDTO.setAttachment(attachment);
        }
        ticketService.createTicket(ticketDTO, usr.get());
        return "redirect:/tickets";
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
        if (file != null && !file.isEmpty()) {
            Attachment attachment = new Attachment();
            attachment.setSize(file.getSize());
            attachment.setName(file.getOriginalFilename());
            attachment.setData(file.getBytes());
            ticketDTO.setAttachment(attachment);
        }
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
}

