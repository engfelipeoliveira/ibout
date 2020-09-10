package br.com.smktbatch.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import br.com.smktbatch.model.ErrorJob;
import br.com.smktbatch.model.Job;
import br.com.smktbatch.model.Parameter;

@Service
public class MainServiceImpl implements MainService {

	private final ParameterService parameterService;
	private final JobService jobService;

	@Value("${msg.error.validation.client.not.found}")
	private String MSG_ERROR_VALID_CLIENT_NOT_FOUND;
	
	private static final Logger LOG = getLogger(MainServiceImpl.class);

	MainServiceImpl(ParameterService parameterService, JobService jobService) {
		this.parameterService = parameterService;
		this.jobService = jobService;
	}

	@Override
	public void execute(String tokenClient) throws Exception {
		LOG.info(String.format("execute(%s)", tokenClient));
		
		Job job = Job.builder().startTime(LocalDateTime.now()).status("INICIADO").build();
		job = this.jobService.createOrUpdateJob(job);

		Parameter parameter = this.parameterService.getParameterByClientToken(tokenClient);
		
		if(parameter == null) {
			ErrorJob error = br.com.smktbatch.model.ErrorJob.builder().job(job).description(MSG_ERROR_VALID_CLIENT_NOT_FOUND).build();
			job = job.toBuilder().endTime(LocalDateTime.now()).status("ERRO").errors(Sets.newHashSet(error)).build();
			LOG.info("Processo finalizado com erro");
		}else {
			List<String> listError = this.parameterService.validateParameters(parameter);
			if(listError.isEmpty()) {
				
				String dataSource = parameter.getDataSource();
				DataSourceService dataSourceService = null;
				
				if("TXT".equalsIgnoreCase(dataSource)) {
					dataSourceService = new TxtServiceImpl(); 
				}else if("CSV".equalsIgnoreCase(dataSource)) {
					dataSourceService = new CsvServiceImpl();
				}else if("XLS".equalsIgnoreCase(dataSource)) {
					dataSourceService = new XlsServiceImpl();
				}else if("BD".equalsIgnoreCase(dataSource)) {
					dataSourceService = new BdServiceImpl();
				}else {
					throw new Exception("tipo de arquivo nao implementado");
				}
				dataSourceService.read(parameter);
				
				job = job.toBuilder().endTime(LocalDateTime.now()).status("SUCESSO").build();
				LOG.info("Processo finalizado com sucesso");				
			}else {
				Set<ErrorJob> errors = new HashSet<ErrorJob>();
				for(String e : listError) {
					errors.add(ErrorJob.builder().job(job).description(e).build());
				}
				
				job = job.toBuilder().endTime(LocalDateTime.now()).status("ERRO").errors(errors).build();
				LOG.info("Processo finalizado com erro");
			}
		}
		
		this.jobService.createOrUpdateJob(job);
		
	}

}
