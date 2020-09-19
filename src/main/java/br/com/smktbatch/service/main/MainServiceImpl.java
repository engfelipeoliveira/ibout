package br.com.smktbatch.service.main;

import static br.com.smktbatch.enums.DataSource.CSV;
import static br.com.smktbatch.enums.DataSource.DB;
import static br.com.smktbatch.enums.DataSource.TXT;
import static br.com.smktbatch.enums.DataSource.XLS;
import static br.com.smktbatch.enums.StatusJob.ERRO;
import static br.com.smktbatch.enums.StatusJob.INICIADO;
import static br.com.smktbatch.enums.StatusJob.SUCESSO;
import static com.google.common.collect.Sets.newHashSet;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.split;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import br.com.smktbatch.enums.DataSource;
import br.com.smktbatch.model.local.Product;
import br.com.smktbatch.model.remote.ErrorJob;
import br.com.smktbatch.model.remote.Job;
import br.com.smktbatch.model.remote.Mapping;
import br.com.smktbatch.model.remote.Parameter;
import br.com.smktbatch.service.datasource.CsvServiceImpl;
import br.com.smktbatch.service.datasource.DataSourceService;
import br.com.smktbatch.service.datasource.DbServiceImpl;
import br.com.smktbatch.service.datasource.TxtServiceImpl;
import br.com.smktbatch.service.datasource.XlsServiceImpl;
import br.com.smktbatch.service.job.JobService;
import br.com.smktbatch.service.mapping.MappingService;
import br.com.smktbatch.service.message.MessageService;
import br.com.smktbatch.service.parameter.ParameterService;
import br.com.smktbatch.service.product.ProductService;

@Service
public class MainServiceImpl implements MainService {

	private final ParameterService parameterService;
	private final JobService jobService;
	private final MappingService mappingService;
	private final MessageService messageService;
	private final ProductService productService;

	private static final Logger LOG = getLogger(MainServiceImpl.class);

	MainServiceImpl(ParameterService parameterService, JobService jobService, MappingService mappingService,
			MessageService messageService, ProductService productService) {
		this.parameterService = parameterService;
		this.jobService = jobService;
		this.mappingService = mappingService;
		this.messageService = messageService;
		this.productService = productService;
	}

	@Override
	public void execute(String tokenClient) throws Exception {
		LOG.info(format("execute(%s)", tokenClient));
		
		Job job = Job.builder().startTime(now()).status(INICIADO).build();
		Parameter parameter = this.parameterService.getByClientToken(tokenClient);
		if (parameter == null) {
			ErrorJob error = ErrorJob.builder().job(job).description(messageService.getByCode("msg.error.validation.parameters.not.found")).build();
			job = job.toBuilder().endTime(now()).status(ERRO).errors(newHashSet(error)).build();
			LOG.error("Processo finalizado com erro");
			this.jobService.createOrUpdate(job);
		} else {
			List<String> listError = this.parameterService.validate(parameter);
			if (listError.isEmpty()) {
				if(parameter.isActive() && asList(split(parameter.getHourJob(), ",")).contains(now().format(ofPattern("HH")))){
					job = this.jobService.createOrUpdate(job);
					Mapping mapping = mappingService.getByClientToken(tokenClient);
					if (mapping == null) {
						ErrorJob error = ErrorJob.builder().job(job).description(messageService.getByCode("msg.error.validation.mapping.not.found")).build();
						job = job.toBuilder().endTime(now()).status(ERRO).errors(newHashSet(error)).build();
						LOG.error("Processo finalizado com erro");
					} else {
						try {
							List<Product> listProduct = dataSourceFactory(parameter.getDataSource()).read(parameter, mapping);
							listProduct.stream().forEach(product ->{
								productService.createOrUpdate(product);	
							});
							
							job = job.toBuilder().endTime(now()).status(SUCESSO).build();
							LOG.info("Processo finalizado com sucesso");
						} catch (Exception e) {
							ErrorJob error = ErrorJob.builder().job(job).stackTrace(e.getMessage()).description(messageService.getByCode("msg.error.read.file")).build();
							job = job.toBuilder().endTime(now()).status(ERRO).errors(newHashSet(error)).build();
							LOG.error("Processo finalizado com erro");
						}
					}
					this.jobService.createOrUpdate(job);
				}else {
					LOG.info("Status inativo ou nao e a hora de executar");
					LOG.info("Processo finalizado sem execucao");
				}
			} else {
				this.jobService.createOrUpdate(job);
				Set<ErrorJob> errors = new HashSet<ErrorJob>();
				for (String e : listError) {
					errors.add(ErrorJob.builder().job(job).description(e).build());
				}

				job = job.toBuilder().endTime(now()).status(ERRO).errors(errors).build();
				LOG.error("Processo finalizado com erro");
				this.jobService.createOrUpdate(job);
			}
		}
	}
	
	private DataSourceService dataSourceFactory(DataSource dataSource) throws Exception {
		if (TXT.equals(dataSource)) {
			return new TxtServiceImpl();
		} else if (CSV.equals(dataSource)) {
			return new CsvServiceImpl();
		} else if (XLS.equals(dataSource)) {
			return new XlsServiceImpl();
		} else if (DB.equals(dataSource)) {
			return new DbServiceImpl();
		}else {
			LOG.error("Tipo de arquivo nao implementado");
			throw new Exception("Tipo de arquivo nao implementado");
		}
	}

}
