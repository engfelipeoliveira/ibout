package br.com.smktbatch.service;

import java.util.List;

import br.com.smktbatch.model.Parameter;

public interface ParameterService {
	
	Parameter getParameterByClientToken(String token) throws Exception;
	List<String> validateParameters(Parameter parameter);

}
