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
		LOG.info("getByClientToken()");
		return this.parameterRepository.findByClientToken(token);
	}

	@Override
	public List<String> validate(Parameter parameter) {
		LOG.info("validate()");
		List<String> listErrors = new ArrayList<String>();

		if (parameter != null) {

			if (!parameter.isParameterStatus()) {
				listErrors.add(this.messageService.getByCode("msg.error.validation.status.job.inactive"));
			}

			if (parameter.getHourJob() == null) {
				listErrors.add(this.messageService.getByCode("msg.error.validation.hourjob.null"));
			}

			if (parameter.getHourJob() != null) {
				List<String> listHour = asList(split(parameter.getHourJob(), ","));
				listHour.stream().forEach(h -> {
					try {
						Long hLong = parseLong(h);
						if(hLong < 0L || hLong > 23L) {
							listErrors.add(this.messageService.getByCode("msg.error.validation.hourjob.not.between.0.and.23"));
						}						
					} catch (Exception e) {
						listErrors.add(this.messageService.getByCode("msg.error.validation.hourjob.not.between.0.and.23"));
					}
				});
			}

			if (parameter.getDataSource() == null) {
				listErrors.add(this.messageService.getByCode("msg.error.validation.datasource.null"));
			}

			if (parameter.getDataSource() != null && !TXT.equals(parameter.getDataSource())
					&& !CSV.equals(parameter.getDataSource())
					&& !DB.equals(parameter.getDataSource())
					&& !XLS.equals(parameter.getDataSource())) {
				listErrors.add(this.messageService.getByCode("msg.error.validation.datasource.invalid"));
			}

			if (parameter.getDataSource() != null && TXT.equals(parameter.getDataSource())
					&& isBlank(parameter.getFileDelimiter())) {
				listErrors.add(this.messageService.getByCode("msg.error.validation.delimiter.file.null"));
			}

			if (parameter.getDataSource() != null && isBlank(parameter.getDirSource())
					&& (TXT.equals(parameter.getDataSource())
							|| CSV.equals(parameter.getDataSource())
							|| XLS.equals(parameter.getDataSource()))) {
				listErrors.add(this.messageService.getByCode("msg.error.validation.directory.source.null"));
			}

			if (parameter.getDataSource() != null && !isBlank(parameter.getDirSource())
					&& !new File(parameter.getDirSource()).exists()
					&& (TXT.equals(parameter.getDataSource())
							|| CSV.equals(parameter.getDataSource())
							|| XLS.equals(parameter.getDataSource()))) {
				listErrors.add(this.messageService.getByCode("msg.error.validation.directory.source.invalid"));
			}

			if (parameter.getDataSource() != null && isBlank(parameter.getDirTarget())
					&& parameter.isMoveFileAfterRead()
					&& (TXT.equals(parameter.getDataSource())
							|| CSV.equals(parameter.getDataSource())
							|| XLS.equals(parameter.getDataSource()))) {
				listErrors.add(this.messageService.getByCode("msg.error.validation.directory.target.null"));
			}

			if (parameter.getDataSource() != null && !isBlank(parameter.getDirTarget())
					&& parameter.isMoveFileAfterRead() && !new File(parameter.getDirTarget()).exists()
					&& (TXT.equals(parameter.getDataSource())
							|| CSV.equals(parameter.getDataSource())
							|| XLS.equals(parameter.getDataSource()))) {
				listErrors.add(this.messageService.getByCode("msg.error.validation.directory.target.invalid"));
			}

			if (parameter.getDataSource() != null && DB.equals(parameter.getDataSource())
					&& isAnyBlank(new String[] { parameter.getSgbd(), parameter.getBdServer(), parameter.getBdPort(),
									parameter.getBdUser(), parameter.getBdPass(), parameter.getBdSql() })) {
				listErrors.add(this.messageService.getByCode("msg.error.validation.database.invalid"));
			}

		}

		return listErrors;
	}

}
