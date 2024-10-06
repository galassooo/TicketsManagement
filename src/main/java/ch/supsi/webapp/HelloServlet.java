package ch.supsi.webapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.supsi.business.TicketBusiness;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(value = "/tickets/*")
public class HelloServlet extends HttpServlet {

    private static final List<TicketBusiness> ticketList = new ArrayList<>();

    private List<TicketBusiness> parseParameter(HttpServletRequest req) {
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String author = req.getParameter("author");

        return ticketList.stream()
                .filter(ticket -> title == null || title.isEmpty() || ticket.getTitle().equalsIgnoreCase(title))
                .filter(ticket -> author == null || author.isEmpty() || ticket.getAuthor().equalsIgnoreCase(author))
                .filter(ticket -> description == null || description.isEmpty() || ticket.getDescription().equalsIgnoreCase(description))
                .toList();
    }

    private TicketBusiness findFromId(int id) {
        return ticketList.stream().filter(ticket -> ticket.getId() == id).findFirst().orElse(null);
    }

    private void sendResponse(HttpServletResponse res, int status, String message) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        res.setStatus(status);
        res.getWriter().write(message);
    }

    private String parseToJson(List<TicketBusiness> tickets) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(tickets);
    }

    private int getTicketID(String pathInfo) throws NumberFormatException {
        String idStr = pathInfo.substring(1);  // Rimuove la "/" iniziale
        return Integer.parseInt(idStr);

    }

    private StringBuilder getJsonBuilder(HttpServletRequest req) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }
        return jsonBuilder;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
            if (req.getParameterMap().isEmpty()) {
                sendResponse(res, HttpServletResponse.SC_OK, parseToJson(ticketList));

            } else {
                List<TicketBusiness> filtered = parseParameter(req);

                if (filtered.isEmpty()) {
                    sendResponse(res, HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"ticket not found\"}");
                } else {
                    sendResponse(res, HttpServletResponse.SC_OK, parseToJson(filtered));
                }
            }
        } else {

            try {
                int ticketId = getTicketID(req.getPathInfo());
                TicketBusiness ticket = findFromId(ticketId);

                if (ticket != null) {

                    sendResponse(res, HttpServletResponse.SC_OK, parseToJson(List.of(ticket)));
                } else {
                    sendResponse(res, HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"Ticket not found\"}");
                }
            } catch (NumberFormatException e) {
                sendResponse(res, HttpServletResponse.SC_BAD_REQUEST, "{\"message\": \"Invalid ticket ID\"}");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        TicketBusiness newTicket;
        if (req.getHeader("content-type").equals("application/json")) {


            ObjectMapper mapper = new ObjectMapper();
            newTicket = mapper.readValue(getJsonBuilder(req).toString(), TicketBusiness.class);


        } else if (req.getHeader("content-type").equals("application/x-www-form-urlencoded")) {
            newTicket = new TicketBusiness(req.getParameter("title"), req.getParameter("description"), req.getParameter("author"));

        } else {
            sendResponse(res, HttpServletResponse.SC_BAD_REQUEST, "{\"message\": \"Invalid request\"}");
            return;
        }

        if (ticketList.contains(newTicket)) {
            sendResponse(res, HttpServletResponse.SC_CONFLICT, "{\"message\": \"Ticket already exists\"}");
        } else {
            ticketList.add(newTicket);
            sendResponse(res, HttpServletResponse.SC_CREATED, "{\"message\": \"Ticket created\",\"id\":" + newTicket.getId() + "}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String contentType = req.getHeader("content-type");

        // Estrai l'ID del ticket dalla path (esempio: /tickets/1)
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            sendResponse(res, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "{\"message\": \"Missing ticket ID in path\"}");
            return;
        }
        try {
            int ticketId = getTicketID(pathInfo);

            TicketBusiness ticketToUpdate = findFromId(ticketId);
            if (ticketToUpdate == null) {
                sendResponse(res, HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"Ticket not found\"}");
                return;
            }

            if (contentType != null && contentType.equalsIgnoreCase("application/json")) {

                ObjectMapper mapper = new ObjectMapper();
                TicketBusiness updatedTicket;
                try {
                    updatedTicket = mapper.readValue(getJsonBuilder(req).toString(), TicketBusiness.class);
                } catch (Exception e) {
                    sendResponse(res, HttpServletResponse.SC_BAD_REQUEST, "{\"message\": \"Invalid JSON format\"}");
                    return;
                }

                ticketToUpdate.setTitle(updatedTicket.getTitle());
                ticketToUpdate.setDescription(updatedTicket.getDescription());
                ticketToUpdate.setAuthor(updatedTicket.getAuthor());

                sendResponse(res, HttpServletResponse.SC_OK, "{\"message\": \"Ticket updated\", \"id\": " + ticketToUpdate.getId() + "}");
            } else if (contentType != null && contentType.equalsIgnoreCase("application/x-www-form-urlencoded")) {

                ticketToUpdate.setTitle(req.getParameter("title"));
                ticketToUpdate.setDescription(req.getParameter("description"));
                ticketToUpdate.setAuthor(req.getParameter("author"));

                sendResponse(res, HttpServletResponse.SC_OK, "{\"message\": \"Ticket updated\", \"id\": " + ticketToUpdate.getId() + "}");
            } else {
                sendResponse(res, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "{\"message\": \"Unsupported Content-Type\"}");
            }

        } catch (NumberFormatException e) {
            sendResponse(res, HttpServletResponse.SC_BAD_REQUEST, "{\"message\": \"Invalid ticket ID\"}");
        }

    }
}
