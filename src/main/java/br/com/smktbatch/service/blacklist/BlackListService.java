package br.com.smktbatch.service.blacklist;

import java.util.List;

import br.com.smktbatch.model.remote.BlackList;

public interface BlackListService {
	
	List<BlackList> getAllByIdClient(Long idClient);

}
