package br.com.ibout.service.parameter;

import java.util.List;

import br.com.ibout.model.remote.Parameter;

public interface ParameterService {
	
	Parameter getByIdClient(Long idClient) throws Exception;
	List<String> validate(Parameter parameter);

}
