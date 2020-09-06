package br.com.smktbatch.service;

import br.com.smktbatch.model.Parameter;

public interface ParameterService {
	
	Parameter getParameterByClient(Long idClient);

}
