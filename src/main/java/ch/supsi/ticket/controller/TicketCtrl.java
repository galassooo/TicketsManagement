package ch.supsi.ticket.controller;

import ch.supsi.ticket.model.*;

import ch.supsi.ticket.service.MilestoneService;
import ch.supsi.ticket.service.TicketService;
import ch.supsi.ticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static ch.supsi.ticket.model.Status.DONE;

@Controller
@RequestMapping("/tickets")
public class TicketCtrl {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private MilestoneService milestoneService;

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
        model.addAttribute("ticketTypes", TicketType.values());
        return "form";
    }

    @PostMapping("new")
    public String createTicket(@ModelAttribute TicketDTO ticketDTO, @RequestParam("file") MultipartFile file) throws IOException {
        // Prendiamo l'utente direttamente dal contesto di sicurezza
        org.springframework.security.core.userdetails.User userTmp =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> usr = userService.getUser(userTmp.getUsername());

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

    @GetMapping("/milestone/new")
    public String  mileStoneCreatePage() {
        return "milestoneCreate";
    }
    @PostMapping("/milestone/new")
    public String createMilestone(@ModelAttribute Milestone milestone) {
        // Prendiamo l'utente direttamente dal contesto di sicurezza
        org.springframework.security.core.userdetails.User userTmp =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> usr = userService.getUser(userTmp.getUsername());

        milestoneService.createMilestone(milestone, usr.get());
        return "redirect:/tickets/milestones";
    }


    @GetMapping("/milestones")
    public String searchParam(Model model) {
        var milestones = milestoneService.getMilestones();
        /*

        con .stream().sorted(Comparator.comparing(Milestone::getDueDate));

        mi dava un multiple use della stream e quindi errore in run, non so perche, ma fa la stessa cosa

         */

        milestones.sort(Comparator.comparing(Milestone::getCreationDate));

        Map<Long, Integer> progresses = new HashMap<>();
        Map<Long, Integer> totals = new HashMap<>();

        milestones.forEach(milestone -> {
           int ticketNumber = milestone.getTickets().size();
           AtomicInteger completed = new AtomicInteger();
                   milestone.getTickets().forEach( ticket ->
                   {
                       completed.addAndGet(ticket.getStatus() == DONE || ticket.getStatus() == Status.CLOSED ? 1 : 0);
                   }
           );
                   progresses.put(milestone.getId(), completed.get());
                   totals.put(milestone.getId(), ticketNumber);
        });

        model.addAttribute("total",totals );
        model.addAttribute("milestones", milestones);

        System.out.println("MILESTONES");
        milestones.forEach(System.out::println);
        model.addAttribute("progresses", progresses);
        return "milestones";
    }


    @GetMapping("/milestones/{id}/add")
    public String addTicketMilestone(@PathVariable Long id, Model model) {

        //sicuro che esista altrimenti non avrei la pagine
        model.addAttribute("milestone", milestoneService.getMilestone(id).get());
        model.addAttribute("tickets", ticketService.getTickets());
        return "addTicketMilestone";
    }


    @GetMapping("/milestones/{id}/add/{t_id}")
    public String addTicketToMilestone(@PathVariable Long id, @PathVariable Long t_id,Model model) {
        milestoneService.addTicket(ticketService.getTicket(t_id).get(), milestoneService.getMilestone(id).get());
        //sicuro che esista altrimenti non avrei la pagine
        model.addAttribute("milestone", milestoneService.getMilestone(id).get());
        model.addAttribute("tickets", ticketService.getTickets());
        return "redirect:/tickets/milestones/{id}/add";
    }

    @GetMapping("/milestones/{id}/complete")
    public  ResponseEntity<Milestone> completeTicketMilestone(@PathVariable Long id) {
        org.springframework.security.core.userdetails.User userTmp =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> usr = userService.getUser(userTmp.getUsername());

        if (usr.isEmpty() || usr.get().getRole() != UserRole.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Milestone milestone = milestoneService.complete(id).get();
        return ResponseEntity.ok(milestone);
    }

    @GetMapping("/{id}/changeStatus")
    public  ResponseEntity<Ticket> changeStatus(@PathVariable Long id) {
        List<Status> statusList = List.of(Status.OPEN, Status.IN_PROGRESS, Status.DONE, Status.CLOSED);

        Ticket ticket = ticketService.getTicket(id).get();

        var tckStatus = ticket.getStatus();

        int index = statusList.indexOf(tckStatus);
        if(index == statusList.size()-1 ){
            return ResponseEntity.ok(ticket);
        }else{
            ticket = ticketService.updateStatus(id, statusList.get(index+1));
            return ResponseEntity.ok(ticket);
        }
    }

}

