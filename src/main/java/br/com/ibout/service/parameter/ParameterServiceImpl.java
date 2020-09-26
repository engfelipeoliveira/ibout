package br.com.ibout.service.parameter;

import static br.com.ibout.enums.DataSource.CSV;
import static br.com.ibout.enums.DataSource.DB;
import static br.com.ibout.enums.DataSource.TXT;
import static br.com.ibout.enums.DataSource.XLS;
import static java.lang.Long.parseLong;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isAnyBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.split;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import br.com.ibout.model.remote.Parameter;
import br.com.ibout.repository.remote.ParameterRepository;
import br.com.ibout.service.message.MessageService;

@Service
public class ParameterServiceImpl implements ParameterService {

	private final ParameterRepository parameterRepository;
	private final MessageService messageService;

	private static final Logger LOG = getLogger(ParameterServiceImpl.class);

	ParameterServiceImpl(ParameterRepository parameterRepository, MessageService messageService) {
		this.parameterRepository = parameterRepository;
		this.messageService = messageService;
	}

	@Override
	public Parameter getByIdClient(Long idClient) throws Exception {
		return this.parameterRepository.findByIdClient(idClient);
	}

	@Override
	public List<String> validate(Parameter parameter) {
		List<String> listErrors = new ArrayList<String>();

		if (parameter != null) {

			if (!parameter.isActive()) {
				String msg = this.messageService.getByCode("msg.error.validation.status.job.inactive");
				LOG.error(msg);
				listErrors.add(msg);
			}

			if (parameter.getHourJob() == null) {
				String msg = this.messageService.getByCode("msg.error.validation.hourjob.null");
				LOG.error(msg);
				listErrors.add(msg);
			}

			if (parameter.getHourJob() != null) {
				asList(split(parameter.getHourJob(), ",")).stream().forEach(h -> {
					try {
						Long hLong = parseLong(h);
						if(hLong < 0L || hLong > 23L) {
							String msg = this.messageService.getByCode("msg.error.validation.hourjob.not.between.0.and.23");
							LOG.error(msg);
							listErrors.add(msg);
						}						
					} catch (Exception e) {
						String msg = this.messageService.getByCode("msg.error.validation.hourjob.not.between.0.and.23");
						LOG.error(msg);
						listErrors.add(msg);
					}
				});
			}

			if (parameter.getDataSource() == null) {
				String msg = this.messageService.getByCode("msg.error.validation.datasource.null");
				LOG.error(msg);
				listErrors.add(msg);
			}

			if (parameter.getDataSource() != null && !TXT.equals(parameter.getDataSource()) && !CSV.equals(parameter.getDataSource())
					&& !DB.equals(parameter.getDataSource()) && !XLS.equals(parameter.getDataSource())) {
				String msg = this.messageService.getByCode("msg.error.validation.datasource.invalid");
				LOG.error(msg);
				listErrors.add(msg);
			}

			if (parameter.getDataSource() != null && TXT.equals(parameter.getDataSource()) && isBlank(parameter.getFileDelimiter())) {
				String msg = this.messageService.getByCode("msg.error.validation.delimiter.file.null");
				LOG.error(msg);
				listErrors.add(msg);
			}

			if (parameter.getDataSource() != null && isBlank(parameter.getDirSource()) && (TXT.equals(parameter.getDataSource())
					|| CSV.equals(parameter.getDataSource()) || XLS.equals(parameter.getDataSource()))) {
				String msg = this.messageService.getByCode("msg.error.validation.directory.source.null");
				LOG.error(msg);
				listErrors.add(msg);
			}

			if (parameter.getDataSource() != null && !isBlank(parameter.getDirSource()) && !new File(parameter.getDirSource()).exists()
					&& (TXT.equals(parameter.getDataSource()) || CSV.equals(parameter.getDataSource()) || XLS.equals(parameter.getDataSource()))) {
				String msg = this.messageService.getByCode("msg.error.validation.directory.source.invalid");
				LOG.error(msg);
				listErrors.add(msg);
			}
			
			if (parameter.getDataSource() != null && !isBlank(parameter.getDirSource()) && new File(parameter.getDirSource()).exists() && 
					new File(parameter.getDirSource()).listFiles().length == 0 && (TXT.equals(parameter.getDataSource()) 
					|| CSV.equals(parameter.getDataSource()) || XLS.equals(parameter.getDataSource()))) {
				
					String msg = this.messageService.getByCode("msg.error.validation.directory.source.empty");
					LOG.error(msg);
					listErrors.add(msg);	
			}

			if (parameter.getDataSource() != null && isBlank(parameter.getDirTarget()) && parameter.isMoveFileAfterRead()
					&& (TXT.equals(parameter.getDataSource()) || CSV.equals(parameter.getDataSource()) || XLS.equals(parameter.getDataSource()))) {
				String msg = this.messageService.getByCode("msg.error.validation.directory.target.null");
				LOG.error(msg);
				listErrors.add(msg);
			}

			if (parameter.getDataSource() != null && !isBlank(parameter.getDirTarget()) && parameter.isMoveFileAfterRead() && !new File(parameter.getDirTarget()).exists()
					&& (TXT.equals(parameter.getDataSource()) || CSV.equals(parameter.getDataSource()) || XLS.equals(parameter.getDataSource()))) {
				String msg = this.messageService.getByCode("msg.error.validation.directory.target.invalid");
				LOG.error(msg);
				listErrors.add(msg);
			}

			if (parameter.getDataSource() != null && !isBlank(parameter.getDirSource()) && !isBlank(parameter.getDirTarget()) 
					&& parameter.isMoveFileAfterRead() && parameter.getDirSource().equalsIgnoreCase(parameter.getDirTarget())
					&& (TXT.equals(parameter.getDataSource()) || CSV.equals(parameter.getDataSource()) || XLS.equals(parameter.getDataSource()))) {
				String msg = this.messageService.getByCode("msg.error.validation.directory.source.target.equals");
				LOG.error(msg);
				listErrors.add(msg);
			}
			
			if (parameter.getDataSource() != null && DB.equals(parameter.getDataSource())
					&& isAnyBlank(new String[] { parameter.getSgbd(), parameter.getBdUrl(), parameter.getBdDriver(), parameter.getBdUser(), parameter.getBdPass(), parameter.getBdSql() })) {
				String msg = this.messageService.getByCode("msg.error.validation.database.invalid");
				LOG.error(msg);
				listErrors.add(msg);
			}
			
			if (isBlank(parameter.getApiUrlInsertProduct())) {
				String msg = this.messageService.getByCode("msg.error.validation.api.url.insert.products.null");
				LOG.error(msg);
				listErrors.add(msg);
			}
			
			if (isBlank(parameter.getApiSizeArrayInsertProduct())) {
				String msg = this.messageService.getByCode("msg.error.validation.api.size.array.insert.products.invalid");
				LOG.error(msg);
				listErrors.add(msg);
			}else {
				try {
					parseLong(parameter.getApiSizeArrayInsertProduct());	
				} catch (Exception e) {
					String msg = this.messageService.getByCode("msg.error.validation.api.size.array.insert.products.invalid");
					LOG.error(msg);
					listErrors.add(msg);
				}
				
			}
		}

		return listErrors;
	}

}
