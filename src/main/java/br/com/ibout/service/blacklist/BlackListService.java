package br.com.ibout.service.blacklist;

import java.util.List;

import br.com.ibout.model.remote.BlackList;

public interface BlackListService {
	
	List<BlackList> getAllByIdClient(Long idClient);

}
