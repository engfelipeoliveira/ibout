package br.com.smktbatch.repository.remote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.smktbatch.model.remote.Parameter;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter , Long> {
	
	Parameter findByClientToken(String token);

}
