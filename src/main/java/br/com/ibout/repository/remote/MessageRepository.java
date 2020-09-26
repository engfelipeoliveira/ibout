package br.com.ibout.repository.remote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ibout.model.remote.MessageJob;

@Repository
public interface MessageRepository extends JpaRepository<MessageJob, Long> {

	MessageJob findByCode(String code);

}
