package br.com.smktbatch.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
		LOG.info(String.format("getParameterByClientToken(%s)", token));
		Parameter parameter = this.parameterRepository.findByClientToken(token);
		if(parameter == null) {
			throw new Exception("Parametros nao encotrados para o cliente");
		}
		
		return parameter;
	}

	@Override
	public List<String> validateParameters(Parameter parameter) {
		List<String> listErrors = new ArrayList<String>();
		
		if(parameter != null) {
			LOG.info(String.format("validateParameters(%s)", parameter.toString()));
			
			if(parameter.getHourJob() == null) {
				listErrors.add("hora do job nao informado");
			}	
			
			if(parameter.getHourJob() != null && parameter.getHourJob() < 1L || parameter.getHourJob() > 23L) {
				listErrors.add("hora do job deve estar entre 0 e 23");
			}
			
			if(StringUtils.isBlank(parameter.getDataSource())) {
				listErrors.add("origem dos dados nao informado");
			}
			
			if(!StringUtils.isBlank(parameter.getDataSource()) && 
				"TXT".equalsIgnoreCase(parameter.getDataSource()) && "CSV".equalsIgnoreCase(parameter.getDataSource()) &&
				"BD".equalsIgnoreCase(parameter.getDataSource()) &&	"XLS".equalsIgnoreCase(parameter.getDataSource())) {
				listErrors.add("origem dos dados nao valido");
			}
			
			if(!StringUtils.isBlank(parameter.getDataSource()) 
				&& "TXT".equalsIgnoreCase(parameter.getDataSource()) && StringUtils.isBlank(parameter.getFileDelimiter())) {
				listErrors.add("especificar o delimitador de colunas para arquivo txt");
			}
			
			if(!StringUtils.isBlank(parameter.getDataSource()) && StringUtils.isBlank(parameter.getDirSource()) && 
				("TXT".equalsIgnoreCase(parameter.getDataSource()) || "CSV".equalsIgnoreCase(parameter.getDataSource()) ||
				 "XLS".equalsIgnoreCase(parameter.getDataSource()))) {
				listErrors.add("diretorio de origem e obrigatorio");
			}
			
			if(!StringUtils.isBlank(parameter.getDataSource()) && StringUtils.isBlank(parameter.getDirTarget()) && parameter.isMoveFileAfterRead() && 
				("TXT".equalsIgnoreCase(parameter.getDataSource()) || "CSV".equalsIgnoreCase(parameter.getDataSource()) ||
				 "XLS".equalsIgnoreCase(parameter.getDataSource()))) {
				listErrors.add("diretorio de destino e obrigatorio");
			}
			
			if(!StringUtils.isBlank(parameter.getDataSource()) && "BD".equalsIgnoreCase(parameter.getDataSource()) &&
				StringUtils.isAnyBlank(new String[]{parameter.getSgbd(), parameter.getBdServer(), parameter.getBdPort(), parameter.getBdUser(), parameter.getBdPass(), parameter.getBdSql()})) {
				listErrors.add("informacoes para conexao com o banco de dados estao incompletas");
			}
			
		}
		
		return listErrors;
	}
	

}
