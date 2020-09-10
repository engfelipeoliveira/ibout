package br.com.smktbatch.service;

import br.com.smktbatch.model.Mapping;

public interface MappingService {
	
	Mapping getMappingByClientToken(String token);

}
