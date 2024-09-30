package ch.supsi.webapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ch.supsi.business.TicketBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(value = "/ticket")
public class HelloServlet extends HttpServlet {

    private static final List<TicketBusiness> ticketList = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        String json = mapper.writeValueAsString(ticketList);

        res.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if(req.getHeader("content-type").equals("application/json")){
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            try (BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            TicketBusiness newTicket = mapper.readValue(jsonBuilder.toString(), TicketBusiness.class);
            ticketList.add(newTicket);

        } else if(req.getHeader("content-type").equals("application/x-www-form-urlencoded")){
            TicketBusiness request = new TicketBusiness(req.getParameter("title"), req.getParameter("description"), req.getParameter("author"));
            ticketList.add(request);
        }
    }
}
