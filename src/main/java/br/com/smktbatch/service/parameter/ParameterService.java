package br.com.smktbatch.service.parameter;

import java.util.List;

import br.com.smktbatch.model.remote.Parameter;

public interface ParameterService {
	
	Parameter getByIdClient(Long idClient) throws Exception;
	List<String> validate(Parameter parameter);

}
