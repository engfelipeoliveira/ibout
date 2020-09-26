package br.com.ibout.service.blacklist;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ibout.model.remote.BlackList;
import br.com.ibout.repository.remote.BlackListRepository;

@Service
public class BlackListServiceImpl implements BlackListService {

	private final BlackListRepository blackListRepository;

	BlackListServiceImpl(BlackListRepository blackListRepository) {
		this.blackListRepository = blackListRepository;
	}

	@Override
	public List<BlackList> getAllByIdClient(Long idClient) {
		return this.blackListRepository.findAllByIdClient(idClient);
	}

}
