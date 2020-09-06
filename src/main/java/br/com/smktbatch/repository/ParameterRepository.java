package br.com.smktbatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.smktbatch.model.Parameter;

public interface ParameterRepository extends JpaRepository<Parameter , Long> {
	
	Parameter findByIdClient(Long idClient);

}
