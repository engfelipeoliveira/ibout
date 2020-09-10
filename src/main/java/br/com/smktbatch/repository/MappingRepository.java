package br.com.smktbatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.smktbatch.model.Mapping;

public interface MappingRepository extends JpaRepository<Mapping, Long> {
	
	Mapping findByClientToken(String token);
}
