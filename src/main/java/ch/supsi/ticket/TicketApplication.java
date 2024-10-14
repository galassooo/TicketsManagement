package ch.supsi.ticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TicketApplication {

	public static final List<Ticket> ALL_TICKETS =new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(TicketApplication.class, args);
	}

}
