package br.com.smktbatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.smktbatch.service.ParameterService;

@SpringBootApplication
public class SmktBatchApplication implements CommandLineRunner{

	@Autowired
	private ParameterService parameterService;
	
	public static void main(String[] args) {
		SpringApplication.run(SmktBatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		args[0] = "1";
		
		Long idClient = Long.parseLong(args[0]);
		parameterService.getParameterByClient(idClient);
	}

}
