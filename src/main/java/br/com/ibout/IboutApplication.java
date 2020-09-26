package br.com.ibout;

import static java.lang.Long.parseLong;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import br.com.ibout.service.main.MainService;

@EnableScheduling
@SpringBootApplication
public class IboutApplication implements CommandLineRunner {

	private final MainService mainService;
	private String tokenClient;
	private Long idClient;

	public IboutApplication(MainService mainService) {
		this.mainService = mainService;
	}

	public static void main(String[] args) {
		SpringApplication.run(IboutApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		tokenClient = args[0];
		idClient =  parseLong(args[1]);
	}
	
	@Scheduled(initialDelay = 10000, fixedDelay = 3600000)
	private void execute() throws Exception {
		mainService.execute(tokenClient, idClient);
	}

}
