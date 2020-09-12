package br.com.smktbatch.service.mapping;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import br.com.smktbatch.model.Mapping;
import br.com.smktbatch.repository.MappingRepository;

@Service
public class MappingServiceImpl implements MappingService {

	private final MappingRepository mappingRepository;

	private static final Logger LOG = getLogger(MappingServiceImpl.class);

	MappingServiceImpl(MappingRepository mappingRepository) {
		this.mappingRepository = mappingRepository;
	}

	@Override
	public Mapping getMappingByClientToken(String token) {
		LOG.info("getMappingByClientToken()");
		return mappingRepository.findByClientToken(token);
	}

}
