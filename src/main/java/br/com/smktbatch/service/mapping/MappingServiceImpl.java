package br.com.smktbatch.service.mapping;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import br.com.smktbatch.model.remote.Mapping;
import br.com.smktbatch.repository.remote.MappingRepository;

@Service
public class MappingServiceImpl implements MappingService {

	private final MappingRepository mappingRepository;

	private static final Logger LOG = getLogger(MappingServiceImpl.class);

	MappingServiceImpl(MappingRepository mappingRepository) {
		this.mappingRepository = mappingRepository;
	}

	@Override
	public Mapping getByClientToken(String token) {
		LOG.info("getByClientToken()");
		return mappingRepository.findByClientToken(token);
	}

}
