package br.com.smktbatch.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import br.com.smktbatch.model.Parameter;
import br.com.smktbatch.repository.ParameterRepository;

@Service
public class ParameterServiceImpl implements ParameterService {

	private final ParameterRepository parameterRepository;
	
	private static final Logger LOG = getLogger(ParameterServiceImpl.class);
	
	ParameterServiceImpl(ParameterRepository parameterRepository){
		this.parameterRepository = parameterRepository;
	}
	
	@Override
	public Parameter getParameterByClientToken(String token) throws Exception {
		LOG.info("Lendo parametros");
		Parameter parameter = this.parameterRepository.findByClientToken(token);
		if(parameter == null) {
			throw new Exception(String.format("Parametros nao encotrados para o cliente. Verifique o token %s", token));
		}
		
		LOG.info(parameter.toString());
		validateParameters(parameter);
		return parameter;
	}

	private List<String> validateParameters(Parameter parameter) {
		List<String> listErrors = new ArrayList<String>();
		if(parameter.getHourJob() == null) {
			listErrors.add("hora do job nao informado");
		}	
		
		return listErrors;
	}
	

}
