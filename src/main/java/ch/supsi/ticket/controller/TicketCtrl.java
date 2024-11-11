package ch.supsi.ticket.controller;

import ch.supsi.ticket.model.Status;
import ch.supsi.ticket.model.TicketDTO;
import ch.supsi.ticket.model.TicketType;
import ch.supsi.ticket.service.TicketService;
import ch.supsi.ticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String createTicket(@ModelAttribute TicketDTO ticketDTO) {
        ticketService.createTicket(ticketDTO);
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

    @PostMapping("/{id}/edit")
    public String updateTicket(@PathVariable Long id, @ModelAttribute TicketDTO ticketDTO) {
        ticketService.updateTicket(id, ticketDTO);
        return "redirect:/tickets/{id}";
    }

    @GetMapping("/{id}/delete")
    public String deleteTicket(@PathVariable Long id) {
        if(ticketService.deleteTicket(id)) {
            return "redirect:/tickets";
        }
        return "redirect:/tickets?error=delete-failed";
    }
}
