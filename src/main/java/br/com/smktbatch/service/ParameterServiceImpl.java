package br.com.smktbatch.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.smktbatch.model.Parameter;
import br.com.smktbatch.repository.ParameterRepository;

@Service
public class ParameterServiceImpl implements ParameterService {

	private final ParameterRepository parameterRepository;
	
	@Value("${msg.error.validation.hourjob.null}")
    private String MSG_ERROR_VALID_HOUR_JOB_NULL;
	
	@Value("${msg.error.validation.hourjob.not.between.0.and.23}")
    private String MSG_ERROR_VALID_HOUR_JOB_NOT_BET_0_23;
	
	@Value("${msg.error.validation.datasource.null}")
    private String MSG_ERROR_VALID_DATASOURCE_NULL;
	
	@Value("${msg.error.validation.datasource.invalid}")
    private String MSG_ERROR_VALID_DATASOURCE_INVALID;
	
	@Value("${msg.error.validation.delimiter.file.null}")
    private String MSG_ERROR_VALID_DELIM_FILE_NULL;
	
	@Value("${msg.error.validation.directory.source.null}")
    private String MSG_ERROR_VALID_DIR_SOURCE_NULL;
	
	@Value("${msg.error.validation.directory.source.invalid}")
    private String MSG_ERROR_VALID_DIR_SOURCE_INVALID;
	
	@Value("${msg.error.validation.directory.target.null}")
    private String MSG_ERROR_VALID_DIR_TARGET_NULL;
	
	@Value("${msg.error.validation.directory.target.invalid}")
    private String MSG_ERROR_VALID_DIR_TARGET_INVALID;
	
	@Value("${msg.error.validation.database.invalid}")
    private String MSG_ERROR_VALID_DATABASE_INVALID;
	
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
		
		
		List<String> listErrors = validateParameters(parameter);
		System.out.println(listErrors);
		
		return parameter;
	}

	@Override
	public List<String> validateParameters(Parameter parameter) {
		List<String> listErrors = new ArrayList<String>();
		
		if(parameter != null) {
			LOG.info(String.format("validateParameters(%s)", parameter.toString()));
			
			if(parameter.getHourJob() == null) {
				listErrors.add(MSG_ERROR_VALID_HOUR_JOB_NULL);
			}	
			
			if(parameter.getHourJob() != null && (parameter.getHourJob() < 0L || parameter.getHourJob() > 23L)) {
				listErrors.add(MSG_ERROR_VALID_HOUR_JOB_NOT_BET_0_23);
			}
			
			if(StringUtils.isBlank(parameter.getDataSource())) {
				listErrors.add(MSG_ERROR_VALID_DATASOURCE_NULL);
			}
			
			if(!StringUtils.isBlank(parameter.getDataSource()) && 
				!"TXT".equalsIgnoreCase(parameter.getDataSource()) && !"CSV".equalsIgnoreCase(parameter.getDataSource()) &&
				!"BD".equalsIgnoreCase(parameter.getDataSource()) && !"XLS".equalsIgnoreCase(parameter.getDataSource())) {
				listErrors.add(MSG_ERROR_VALID_DATASOURCE_INVALID);
			}
			
			if(!StringUtils.isBlank(parameter.getDataSource()) && 
				"TXT".equalsIgnoreCase(parameter.getDataSource()) && StringUtils.isBlank(parameter.getFileDelimiter())) {
				listErrors.add(MSG_ERROR_VALID_DELIM_FILE_NULL);
			}
			
			if(!StringUtils.isBlank(parameter.getDataSource()) && StringUtils.isBlank(parameter.getDirSource()) && 
				("TXT".equalsIgnoreCase(parameter.getDataSource()) || "CSV".equalsIgnoreCase(parameter.getDataSource()) ||
				 "XLS".equalsIgnoreCase(parameter.getDataSource()))) {
				listErrors.add(MSG_ERROR_VALID_DIR_SOURCE_NULL);
			}
			
			if(!StringUtils.isBlank(parameter.getDataSource()) && !StringUtils.isBlank(parameter.getDirSource()) && 
				!new File(parameter.getDirSource()).exists() && 	
				("TXT".equalsIgnoreCase(parameter.getDataSource()) || "CSV".equalsIgnoreCase(parameter.getDataSource()) ||
				 "XLS".equalsIgnoreCase(parameter.getDataSource()))) {
				listErrors.add(MSG_ERROR_VALID_DIR_SOURCE_INVALID);
			}
			
			if(!StringUtils.isBlank(parameter.getDataSource()) && StringUtils.isBlank(parameter.getDirTarget()) && parameter.isMoveFileAfterRead() && 
				("TXT".equalsIgnoreCase(parameter.getDataSource()) || "CSV".equalsIgnoreCase(parameter.getDataSource()) ||
				 "XLS".equalsIgnoreCase(parameter.getDataSource()))) {
				listErrors.add(MSG_ERROR_VALID_DIR_TARGET_NULL);
			}
			
			if(!StringUtils.isBlank(parameter.getDataSource()) && !StringUtils.isBlank(parameter.getDirTarget()) && 
				parameter.isMoveFileAfterRead() && !new File(parameter.getDirTarget()).exists() && 
				("TXT".equalsIgnoreCase(parameter.getDataSource()) || "CSV".equalsIgnoreCase(parameter.getDataSource()) ||
				 "XLS".equalsIgnoreCase(parameter.getDataSource()))) {
				listErrors.add(MSG_ERROR_VALID_DIR_TARGET_INVALID);
			}
			
			if(!StringUtils.isBlank(parameter.getDataSource()) && "BD".equalsIgnoreCase(parameter.getDataSource()) &&
				StringUtils.isAnyBlank(new String[]{parameter.getSgbd(), parameter.getBdServer(), parameter.getBdPort(), parameter.getBdUser(), parameter.getBdPass(), parameter.getBdSql()})) {
				listErrors.add(MSG_ERROR_VALID_DATABASE_INVALID);
			}
			
		}
		
		return listErrors;
	}
	

}
