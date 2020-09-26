package br.com.ibout.repository.remote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ibout.model.remote.Parameter;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter , Long> {
	
	Parameter findByIdClient(Long idClient);

}
