package br.com.smktbatch.service.main;

import static org.slf4j.LoggerFactory.getLogger;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import br.com.smktbatch.enums.DataSource;
import br.com.smktbatch.enums.StatusJob;
import br.com.smktbatch.model.ErrorJob;
import br.com.smktbatch.model.Job;
import br.com.smktbatch.model.Mapping;
import br.com.smktbatch.model.Parameter;
import br.com.smktbatch.service.datasource.CsvServiceImpl;
import br.com.smktbatch.service.datasource.DataSourceService;
import br.com.smktbatch.service.datasource.DbServiceImpl;
import br.com.smktbatch.service.datasource.TxtServiceImpl;
import br.com.smktbatch.service.datasource.XlsServiceImpl;
import br.com.smktbatch.service.job.JobService;
import br.com.smktbatch.service.mapping.MappingService;
import br.com.smktbatch.service.message.MessageService;
import br.com.smktbatch.service.parameter.ParameterService;

@Service
public class MainServiceImpl implements MainService {

	private final ParameterService parameterService;
	private final JobService jobService;
	private final MappingService mappingService;
	private final MessageService messageService;

	private static final Logger LOG = getLogger(MainServiceImpl.class);

	MainServiceImpl(ParameterService parameterService, JobService jobService, MappingService mappingService, MessageService messageService) {
		this.parameterService = parameterService;
		this.jobService = jobService;
		this.mappingService = mappingService;
		this.messageService = messageService;
	}

	@Override
	public void execute(String tokenClient) throws Exception {
		LOG.info(String.format("execute(%s)", tokenClient));

		Job job = Job.builder().startTime(LocalDateTime.now()).status(StatusJob.INICIADO).build();
		job = this.jobService.createOrUpdateJob(job);

		Parameter parameter = this.parameterService.getParameterByClientToken(tokenClient);

		if (parameter == null) {
			ErrorJob error = ErrorJob.builder().job(job).description(messageService.getMessageByCode("msg.error.validation.parameters.not.found")).build();
			job = job.toBuilder().endTime(LocalDateTime.now()).status(StatusJob.ERRO).errors(Sets.newHashSet(error)).build();
			LOG.info("Processo finalizado com erro");

		} else {
			List<String> listError = this.parameterService.validateParameters(parameter);
			if (listError.isEmpty()) {

				Mapping mapping = mappingService.getMappingByClientToken(tokenClient);
				if (mapping == null) {
					ErrorJob error = ErrorJob.builder().job(job).description(messageService.getMessageByCode("msg.error.validation.mapping.not.found")).build();
					job = job.toBuilder().endTime(LocalDateTime.now()).status(StatusJob.ERRO).errors(Sets.newHashSet(error))
							.build();
					LOG.info("Processo finalizado com erro");

				} else {
					DataSource dataSource = parameter.getDataSource();
					DataSourceService dataSourceService = null;

					if (DataSource.TXT.equals(dataSource)) {
						dataSourceService = new TxtServiceImpl();
					} else if (DataSource.CSV.equals(dataSource)) {
						dataSourceService = new CsvServiceImpl();
					} else if (DataSource.XLS.equals(dataSource)) {
						dataSourceService = new XlsServiceImpl();
					} else if (DataSource.DB.equals(dataSource)) {
						dataSourceService = new DbServiceImpl();
					} else {
						throw new Exception("tipo de arquivo nao implementado");
					}
					
					try {
						dataSourceService.read(parameter, mapping);	
						job = job.toBuilder().endTime(LocalDateTime.now()).status(StatusJob.SUCESSO).build();
						LOG.info("Processo finalizado com sucesso");
					} catch (Exception e) {
						ErrorJob error = ErrorJob.builder().job(job).stackTrace(e.getMessage()).description(messageService.getMessageByCode("msg.error.read.file")).build();
						job = job.toBuilder().endTime(LocalDateTime.now()).status(StatusJob.ERRO).errors(Sets.newHashSet(error))
								.build();
						LOG.info("Processo finalizado com erro");
					}
				}

			} else {
				Set<ErrorJob> errors = new HashSet<ErrorJob>();
				for (String e : listError) {
					errors.add(ErrorJob.builder().job(job).description(e).build());
				}

				job = job.toBuilder().endTime(LocalDateTime.now()).status(StatusJob.ERRO).errors(errors).build();
				LOG.info("Processo finalizado com erro");
			}
		}

		this.jobService.createOrUpdateJob(job);

	}

}
