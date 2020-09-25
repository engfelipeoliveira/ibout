package br.com.smktbatch.service.mapping;

import org.springframework.stereotype.Service;

import br.com.smktbatch.model.remote.Mapping;
import br.com.smktbatch.repository.remote.MappingRepository;

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
