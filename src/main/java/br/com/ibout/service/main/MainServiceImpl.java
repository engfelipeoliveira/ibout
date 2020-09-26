package br.com.ibout.service.main;

import static br.com.ibout.dto.MapperRequestInsertProductDto.fromProductDto;
import static br.com.ibout.enums.DataSource.CSV;
import static br.com.ibout.enums.DataSource.DB;
import static br.com.ibout.enums.DataSource.TXT;
import static br.com.ibout.enums.DataSource.XLS;
import static br.com.ibout.enums.StatusJob.ERRO;
import static br.com.ibout.enums.StatusJob.INICIADO;
import static br.com.ibout.enums.StatusJob.SUCESSO;
import static com.google.common.collect.Sets.newHashSet;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.length;
import static org.apache.commons.lang3.StringUtils.split;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.ibout.dto.RequestInsertProductDto;
import br.com.ibout.enums.DataSource;
import br.com.ibout.model.local.Product;
import br.com.ibout.model.remote.BlackList;
import br.com.ibout.model.remote.ErrorJob;
import br.com.ibout.model.remote.Job;
import br.com.ibout.model.remote.Mapping;
import br.com.ibout.model.remote.Parameter;
import br.com.ibout.service.apiclient.ApiClientService;
import br.com.ibout.service.blacklist.BlackListService;
import br.com.ibout.service.datasource.CsvServiceImpl;
import br.com.ibout.service.datasource.DataSourceService;
import br.com.ibout.service.datasource.DbServiceImpl;
import br.com.ibout.service.datasource.TxtServiceImpl;
import br.com.ibout.service.datasource.XlsServiceImpl;
import br.com.ibout.service.job.JobService;
import br.com.ibout.service.mapping.MappingService;
import br.com.ibout.service.message.MessageService;
import br.com.ibout.service.parameter.ParameterService;
import br.com.ibout.service.product.ProductService;

@Service
public class MainServiceImpl implements MainService {

	private final ParameterService parameterService;
	private final JobService jobService;
	private final MappingService mappingService;
	private final MessageService messageService;
	private final ProductService productService;
	private final ApiClientService apiClientService;
	private final BlackListService blackListService;

	private static final Logger LOG = getLogger(MainServiceImpl.class);

	MainServiceImpl(ParameterService parameterService, JobService jobService, MappingService mappingService,
			MessageService messageService, ProductService productService, ApiClientService apiClientService, BlackListService blackListService) {
		this.parameterService = parameterService;
		this.jobService = jobService;
		this.mappingService = mappingService;
		this.messageService = messageService;
		this.productService = productService;
		this.apiClientService = apiClientService;
		this.blackListService = blackListService;
	}

	@Override
	public void execute(String tokenClient, Long idClient) throws Exception {
		LOG.info(format("execute(%s,%s)", tokenClient, idClient));
		
		Job job = Job.builder().startTime(now()).status(INICIADO).build();
		Parameter parameter = this.parameterService.getByIdClient(idClient);
		if (parameter == null) {
			this.jobService.createOrUpdate(job);
			String msg = messageService.getByCode("msg.error.validation.parameters.not.found");
			ErrorJob error = ErrorJob.builder().job(job).description(msg).build();
			job = job.toBuilder().endTime(now()).status(ERRO).errors(newHashSet(error)).build();
			LOG.error(msg);
			LOG.error("Processo finalizado com erro");
			this.jobService.createOrUpdate(job);
		} else {
			LOG.info(parameter.toString());
			List<String> listError = this.parameterService.validate(parameter);
			if (listError.isEmpty()) {
				if(parameter.isActive() && asList(split(parameter.getHourJob(), ",")).contains(now().format(ofPattern("HH")))){
					job = this.jobService.createOrUpdate(job);
					Mapping mapping = mappingService.getByIdClient(idClient);
					if (mapping == null) {
						String msg = messageService.getByCode("msg.error.validation.mapping.not.found");
						ErrorJob error = ErrorJob.builder().job(job).description(msg).build();
						job = job.toBuilder().endTime(now()).status(ERRO).errors(newHashSet(error)).build();
						LOG.error(msg);
						LOG.error("Processo finalizado com erro");
					} else {
						LOG.info(mapping.toString());
						try {
							if(parameter.isImportAll()) {
								LOG.info("Deletando produtos do banco de dados local");
								productService.deleteAll();
							}
							
							createProduct(parameter, mapping, tokenClient, idClient, job);
							job = job.toBuilder().status(job.getStatus() != null ? job.getStatus() : SUCESSO).endTime(now()).build();
							this.jobService.createOrUpdate(job);
							
						} catch (Exception e) {
							String msg = messageService.getByCode("msg.error.read.file");
							ErrorJob error = ErrorJob.builder().job(job).stackTrace(e.getMessage()).description(msg).build();
							job = job.toBuilder().endTime(now()).status(ERRO).errors(newHashSet(error)).build();
							LOG.error("Processo finalizado com erro");
							this.jobService.createOrUpdate(job);
						}
					}
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
		LOG.info("Aguardando a proxima execucao");
	}
	
	private void createProduct(Parameter parameter, Mapping mapping, String tokenClient, Long idClient, Job job) throws Exception {
		LOG.info("Comparando e criando produtos");
		List<RequestInsertProductDto> listRequestInsertProductDto = new ArrayList<RequestInsertProductDto>();
		List<BlackList> blackList = this.blackListService.getAllByIdClient(idClient);
		List<Product> productsSaved = this.productService.getAll();
		List<Product> productsToSave = dataSourceFactory(parameter.getDataSource()).read(parameter, mapping);
		LOG.info("total de produtos no banco de dados local " + productsSaved.size());
		LOG.info("total de produtos no arquivo " + productsToSave.size());
		
		productsToSave.stream().forEach(product ->{
			
			if(!blackList.stream().filter(bl -> bl.getCode().equalsIgnoreCase(product.getCode())).findFirst().isPresent()) {
				Product productSaved = productsSaved.stream().filter(p -> p.getCode().equalsIgnoreCase(product.getCode())).findFirst().orElse(null);
				if(productSaved == null) {
					listRequestInsertProductDto.add(fromProductDto(productService.createOrUpdate(product)));
				}else if(!productSaved.equals(product)){
					product.setId(productSaved.getId());
					listRequestInsertProductDto.add(fromProductDto(productService.createOrUpdate(product)));
				}
			}
			
			if(listRequestInsertProductDto != null && !listRequestInsertProductDto.isEmpty() && listRequestInsertProductDto.size() == parseInt(parameter.getApiSizeArrayInsertProduct())) {
				callApiClientService(parameter, mapping, tokenClient, idClient, job, listRequestInsertProductDto);
				listRequestInsertProductDto.clear();
			}
		});
		
		if(listRequestInsertProductDto != null && !listRequestInsertProductDto.isEmpty()) {
			callApiClientService(parameter, mapping, tokenClient, idClient, job, listRequestInsertProductDto);	
			listRequestInsertProductDto.clear();
		}
	}
	
	private String callApiClientService(Parameter parameter, Mapping mapping, String tokenClient, Long idClient, Job job, List<RequestInsertProductDto> listRequestInsertProductDto) {
		LOG.info("Chamando API");
		String returnApi = null;
		try {
			returnApi = this.apiClientService.callInsertProduct(tokenClient, idClient, listRequestInsertProductDto, parameter);
			
			if(length(returnApi) > 500) {
				LOG.error(new Gson().toJson(listRequestInsertProductDto));
				LOG.error(returnApi);
				String msg = messageService.getByCode("msg.error.call.api.insert.product");
				ErrorJob error = ErrorJob.builder().job(job).description(msg).build();
				job = job.toBuilder().status(ERRO).errors(newHashSet(error)).build();
				LOG.error(msg);
			}
		} catch (ClientProtocolException e) {
			String msg = messageService.getByCode("msg.error.call.api.insert.product");
			ErrorJob error = ErrorJob.builder().job(job).stackTrace(e.getMessage()).description(msg).build();
			job = job.toBuilder().status(ERRO).errors(newHashSet(error)).build();
			LOG.error(msg);
		} catch (IOException e) {
			String msg = messageService.getByCode("msg.error.call.api.insert.product");
			ErrorJob error = ErrorJob.builder().job(job).stackTrace(e.getMessage()).description(msg).build();
			job = job.toBuilder().status(ERRO).errors(newHashSet(error)).build();
			LOG.error(msg);
		}
		
		this.jobService.createOrUpdate(job);
		
		return returnApi;
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
