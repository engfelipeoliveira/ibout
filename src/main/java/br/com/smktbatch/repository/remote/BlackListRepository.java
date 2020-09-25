package br.com.smktbatch.repository.remote;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.smktbatch.model.remote.BlackList;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {
	
	List<BlackList> findAllByIdClient(Long idClient);
}
