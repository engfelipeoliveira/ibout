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
import static org.slf4j.LoggerFactory.getLogger;

import java.time.LocalDateTime;
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
		
		Job job = Job.builder().startTime(LocalDateTime.now()).status(INICIADO).build();
		job = this.jobService.createOrUpdate(job);

		Parameter parameter = this.parameterService.getByClientToken(tokenClient);

		if (parameter == null) {
			ErrorJob error = ErrorJob.builder().job(job).description(messageService.getByCode("msg.error.validation.parameters.not.found")).build();
			job = job.toBuilder().endTime(LocalDateTime.now()).status(ERRO).errors(newHashSet(error)).build();
			LOG.info("Processo finalizado com erro");
		} else {
			List<String> listError = this.parameterService.validate(parameter);
			if (listError.isEmpty()) {
				Mapping mapping = mappingService.getByClientToken(tokenClient);
				if (mapping == null) {
					ErrorJob error = ErrorJob.builder().job(job).description(messageService.getByCode("msg.error.validation.mapping.not.found")).build();
					job = job.toBuilder().endTime(LocalDateTime.now()).status(ERRO).errors(newHashSet(error)).build();
					LOG.info("Processo finalizado com erro");
				} else {
					DataSource dataSource = parameter.getDataSource();
					DataSourceService dataSourceService = dataSourceFactory(dataSource);

					try {
						List<Product> listProduct = dataSourceService.read(parameter, mapping);
						listProduct.stream().forEach(product ->{
							productService.createOrUpdate(product);	
						});
						
						job = job.toBuilder().endTime(LocalDateTime.now()).status(SUCESSO).build();
						LOG.info("Processo finalizado com sucesso");
					} catch (Exception e) {
						ErrorJob error = ErrorJob.builder().job(job).stackTrace(e.getMessage()).description(messageService.getByCode("msg.error.read.file")).build();
						job = job.toBuilder().endTime(LocalDateTime.now()).status(ERRO).errors(newHashSet(error)).build();
						LOG.info("Processo finalizado com erro");
					}
				}

			} else {
				Set<ErrorJob> errors = new HashSet<ErrorJob>();
				for (String e : listError) {
					errors.add(ErrorJob.builder().job(job).description(e).build());
				}

				job = job.toBuilder().endTime(LocalDateTime.now()).status(ERRO).errors(errors).build();
				LOG.info("Processo finalizado com erro");
			}
		}

		this.jobService.createOrUpdate(job);

	}
	
	private DataSourceService dataSourceFactory(DataSource dataSource) throws Exception {
		DataSourceService dataSourceService = null;

		if (TXT.equals(dataSource)) {
			dataSourceService = new TxtServiceImpl();
		} else if (CSV.equals(dataSource)) {
			dataSourceService = new CsvServiceImpl();
		} else if (XLS.equals(dataSource)) {
			dataSourceService = new XlsServiceImpl();
		} else if (DB.equals(dataSource)) {
			dataSourceService = new DbServiceImpl();
		}else {
			throw new Exception("tipo de arquivo nao implementado");
		}
		return dataSourceService; 
	}

}
