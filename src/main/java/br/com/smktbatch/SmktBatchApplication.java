package br.com.smktbatch;

import static java.lang.Long.parseLong;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import br.com.smktbatch.service.main.MainService;

@EnableScheduling
@SpringBootApplication
public class SmktBatchApplication implements CommandLineRunner {

	private final MainService mainService;
	private String tokenClient;
	private Long idClient;

	public SmktBatchApplication(MainService mainService) {
		this.mainService = mainService;
	}

	public static void main(String[] args) {
		SpringApplication.run(SmktBatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		tokenClient = args[0];
		idClient =  parseLong(args[1]);
	}
	
	@Scheduled(initialDelay = 10000, fixedDelay = 10000)
	private void execute() throws Exception {
		mainService.execute(tokenClient, idClient);
	}

}
