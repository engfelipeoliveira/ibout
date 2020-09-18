package br.com.smktbatch.service.mapping;

import br.com.smktbatch.model.remote.Mapping;

public interface MappingService {
	
	Mapping getByClientToken(String token);

}
