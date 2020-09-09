package br.com.smktbatch.service;

import static org.slf4j.LoggerFactory.getLogger;

import com.google.common.collect.Sets; 
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.cfg.SetSimpleValueTypeSecondPass;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
			br.com.smktbatch.model.Error error = br.com.smktbatch.model.Error.builder().job(job).description(MSG_ERROR_VALID_CLIENT_NOT_FOUND).build();
			job = job.toBuilder().endTime(LocalDateTime.now()).status("ERRO").errors(Sets.newHashSet(error)).build();
			LOG.info("Processo finalizado com erro");
		}else {
			job = job.toBuilder().endTime(LocalDateTime.now()).status("SUCESSO").build();
			LOG.info("Processo finalizado com sucesso");	
		}
		
		this.jobService.createOrUpdateJob(job);
		
	}

}
