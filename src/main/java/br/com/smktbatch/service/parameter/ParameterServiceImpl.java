package br.com.smktbatch.service.parameter;

import static br.com.smktbatch.enums.DataSource.CSV;
import static br.com.smktbatch.enums.DataSource.DB;
import static br.com.smktbatch.enums.DataSource.TXT;
import static br.com.smktbatch.enums.DataSource.XLS;
import static java.lang.Long.parseLong;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isAnyBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.split;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import br.com.smktbatch.model.remote.Parameter;
import br.com.smktbatch.repository.remote.ParameterRepository;
import br.com.smktbatch.service.message.MessageService;

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
	public Parameter getByClientToken(String token) throws Exception {
		return this.parameterRepository.findByClientToken(token);
	}

	@Override
	public List<String> validate(Parameter parameter) {
		List<String> listErrors = new ArrayList<String>();

		if (parameter != null) {

			if (!parameter.isActive()) {
				LOG.error(this.messageService.getByCode("msg.error.validation.status.job.inactive"));
				listErrors.add(this.messageService.getByCode("msg.error.validation.status.job.inactive"));
			}

			if (parameter.getHourJob() == null) {
				LOG.error(this.messageService.getByCode("msg.error.validation.hourjob.null"));
				listErrors.add(this.messageService.getByCode("msg.error.validation.hourjob.null"));
			}

			if (parameter.getHourJob() != null) {
				asList(split(parameter.getHourJob(), ",")).stream().forEach(h -> {
					try {
						Long hLong = parseLong(h);
						if(hLong < 0L || hLong > 23L) {
							LOG.error(this.messageService.getByCode("msg.error.validation.hourjob.not.between.0.and.23"));
							listErrors.add(this.messageService.getByCode("msg.error.validation.hourjob.not.between.0.and.23"));
						}						
					} catch (Exception e) {
						LOG.error(this.messageService.getByCode("msg.error.validation.hourjob.not.between.0.and.23"));
						listErrors.add(this.messageService.getByCode("msg.error.validation.hourjob.not.between.0.and.23"));
					}
				});
			}

			if (parameter.getDataSource() == null) {
				LOG.error(this.messageService.getByCode("msg.error.validation.datasource.null"));
				listErrors.add(this.messageService.getByCode("msg.error.validation.datasource.null"));
			}

			if (parameter.getDataSource() != null && !TXT.equals(parameter.getDataSource()) && !CSV.equals(parameter.getDataSource())
					&& !DB.equals(parameter.getDataSource()) && !XLS.equals(parameter.getDataSource())) {
				LOG.error(this.messageService.getByCode("msg.error.validation.datasource.invalid"));
				listErrors.add(this.messageService.getByCode("msg.error.validation.datasource.invalid"));
			}

			if (parameter.getDataSource() != null && TXT.equals(parameter.getDataSource()) && isBlank(parameter.getFileDelimiter())) {
				LOG.error(this.messageService.getByCode("msg.error.validation.delimiter.file.null"));
				listErrors.add(this.messageService.getByCode("msg.error.validation.delimiter.file.null"));
			}

			if (parameter.getDataSource() != null && isBlank(parameter.getDirSource()) && (TXT.equals(parameter.getDataSource())
					|| CSV.equals(parameter.getDataSource()) || XLS.equals(parameter.getDataSource()))) {
				LOG.error(this.messageService.getByCode("msg.error.validation.directory.source.null"));
				listErrors.add(this.messageService.getByCode("msg.error.validation.directory.source.null"));
			}

			if (parameter.getDataSource() != null && !isBlank(parameter.getDirSource()) && !new File(parameter.getDirSource()).exists()
					&& (TXT.equals(parameter.getDataSource()) || CSV.equals(parameter.getDataSource()) || XLS.equals(parameter.getDataSource()))) {
				LOG.error(this.messageService.getByCode("msg.error.validation.directory.source.invalid"));
				listErrors.add(this.messageService.getByCode("msg.error.validation.directory.source.invalid"));
			}
			
			if (parameter.getDataSource() != null && !isBlank(parameter.getDirSource()) && (TXT.equals(parameter.getDataSource()) 
					|| CSV.equals(parameter.getDataSource()) || XLS.equals(parameter.getDataSource()))) {
				
				FilenameFilter filter = new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.toUpperCase().contains(parameter.getDataSource().toString());
					}
				};

				if(new File(parameter.getDirSource()).listFiles(filter).length == 0) {
					LOG.error(this.messageService.getByCode("msg.error.validation.directory.source.empty"));
					listErrors.add(this.messageService.getByCode("msg.error.validation.directory.source.empty"));	
				}
			}

			if (parameter.getDataSource() != null && isBlank(parameter.getDirTarget()) && parameter.isMoveFileAfterRead()
					&& (TXT.equals(parameter.getDataSource()) || CSV.equals(parameter.getDataSource()) || XLS.equals(parameter.getDataSource()))) {
				LOG.error(this.messageService.getByCode("msg.error.validation.directory.target.null"));
				listErrors.add(this.messageService.getByCode("msg.error.validation.directory.target.null"));
			}

			if (parameter.getDataSource() != null && !isBlank(parameter.getDirTarget()) && parameter.isMoveFileAfterRead() && !new File(parameter.getDirTarget()).exists()
					&& (TXT.equals(parameter.getDataSource()) || CSV.equals(parameter.getDataSource()) || XLS.equals(parameter.getDataSource()))) {
				LOG.error(this.messageService.getByCode("msg.error.validation.directory.target.invalid"));
				listErrors.add(this.messageService.getByCode("msg.error.validation.directory.target.invalid"));
			}

			if (parameter.getDataSource() != null && !isBlank(parameter.getDirSource()) && !isBlank(parameter.getDirTarget()) 
					&& parameter.isMoveFileAfterRead() && parameter.getDirSource().equalsIgnoreCase(parameter.getDirTarget())
					&& (TXT.equals(parameter.getDataSource()) || CSV.equals(parameter.getDataSource()) || XLS.equals(parameter.getDataSource()))) {
				LOG.error(this.messageService.getByCode("msg.error.validation.directory.source.target.equals"));
				listErrors.add(this.messageService.getByCode("msg.error.validation.directory.source.target.equals"));
			}
			
			if (parameter.getDataSource() != null && DB.equals(parameter.getDataSource())
					&& isAnyBlank(new String[] { parameter.getSgbd(), parameter.getBdServer(), parameter.getBdPort(), parameter.getBdUser(), parameter.getBdPass(), parameter.getBdSql() })) {
				LOG.error(this.messageService.getByCode("msg.error.validation.database.invalid"));
				listErrors.add(this.messageService.getByCode("msg.error.validation.database.invalid"));
			}

		}

		return listErrors;
	}

}
