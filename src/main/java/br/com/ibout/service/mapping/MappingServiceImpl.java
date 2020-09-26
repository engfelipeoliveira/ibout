package br.com.ibout.service.mapping;

import org.springframework.stereotype.Service;

import br.com.ibout.model.remote.Mapping;
import br.com.ibout.repository.remote.MappingRepository;

@Service
public class MappingServiceImpl implements MappingService {

	private final MappingRepository mappingRepository;

	MappingServiceImpl(MappingRepository mappingRepository) {
		this.mappingRepository = mappingRepository;
	}

	@Override
	public Mapping getByIdClient(Long idClient) {
		return mappingRepository.findByIdClient(idClient);
	}

}
