package br.com.smktbatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.smktbatch.model.MessageJob;

public interface MessageRepository extends JpaRepository<MessageJob, Long> {

	MessageJob findByCode(String code);

}
