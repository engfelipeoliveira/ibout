package br.com.smktbatch.service.parameter;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import br.com.smktbatch.enums.DataSource;
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
	public Parameter getParameterByClientToken(String token) throws Exception {
		LOG.info("getParameterByClientToken()");
		return this.parameterRepository.findByClientToken(token);
	}

	@Override
	public List<String> validateParameters(Parameter parameter) {
		LOG.info("validateParameters()");
		List<String> listErrors = new ArrayList<String>();

		if (parameter != null) {

			if (!parameter.isParameterStatus()) {
				listErrors.add(this.messageService.getMessageByCode("msg.error.validation.status.job.inactive"));
			}

			if (parameter.getHourJob() == null) {
				listErrors.add(this.messageService.getMessageByCode("msg.error.validation.hourjob.null"));
			}

			if (parameter.getHourJob() != null && (parameter.getHourJob() < 0L || parameter.getHourJob() > 23L)) {
				listErrors
						.add(this.messageService.getMessageByCode("msg.error.validation.hourjob.not.between.0.and.23"));
			}

			if (parameter.getDataSource() == null) {
				listErrors.add(this.messageService.getMessageByCode("msg.error.validation.datasource.null"));
			}

			if (parameter.getDataSource() != null && !DataSource.TXT.equals(parameter.getDataSource())
					&& !DataSource.CSV.equals(parameter.getDataSource())
					&& !DataSource.DB.equals(parameter.getDataSource())
					&& !DataSource.XLS.equals(parameter.getDataSource())) {
				listErrors.add(this.messageService.getMessageByCode("msg.error.validation.datasource.invalid"));
			}

			if (parameter.getDataSource() != null && DataSource.TXT.equals(parameter.getDataSource())
					&& StringUtils.isBlank(parameter.getFileDelimiter())) {
				listErrors.add(this.messageService.getMessageByCode("msg.error.validation.delimiter.file.null"));
			}

			if (parameter.getDataSource() != null && StringUtils.isBlank(parameter.getDirSource())
					&& (DataSource.TXT.equals(parameter.getDataSource())
							|| DataSource.CSV.equals(parameter.getDataSource())
							|| DataSource.XLS.equals(parameter.getDataSource()))) {
				listErrors.add(this.messageService.getMessageByCode("msg.error.validation.directory.source.null"));
			}

			if (parameter.getDataSource() != null && !StringUtils.isBlank(parameter.getDirSource())
					&& !new File(parameter.getDirSource()).exists()
					&& (DataSource.TXT.equals(parameter.getDataSource())
							|| DataSource.CSV.equals(parameter.getDataSource())
							|| DataSource.XLS.equals(parameter.getDataSource()))) {
				listErrors.add(this.messageService.getMessageByCode("msg.error.validation.directory.source.invalid"));
			}

			if (parameter.getDataSource() != null && StringUtils.isBlank(parameter.getDirTarget())
					&& parameter.isMoveFileAfterRead()
					&& (DataSource.TXT.equals(parameter.getDataSource())
							|| DataSource.CSV.equals(parameter.getDataSource())
							|| DataSource.XLS.equals(parameter.getDataSource()))) {
				listErrors.add(this.messageService.getMessageByCode("msg.error.validation.directory.target.null"));
			}

			if (parameter.getDataSource() != null && !StringUtils.isBlank(parameter.getDirTarget())
					&& parameter.isMoveFileAfterRead() && !new File(parameter.getDirTarget()).exists()
					&& (DataSource.TXT.equals(parameter.getDataSource())
							|| DataSource.CSV.equals(parameter.getDataSource())
							|| DataSource.XLS.equals(parameter.getDataSource()))) {
				listErrors.add(this.messageService.getMessageByCode("msg.error.validation.directory.target.invalid"));
			}

			if (parameter.getDataSource() != null && DataSource.DB.equals(parameter.getDataSource())
					&& StringUtils.isAnyBlank(
							new String[] { parameter.getSgbd(), parameter.getBdServer(), parameter.getBdPort(),
									parameter.getBdUser(), parameter.getBdPass(), parameter.getBdSql() })) {
				listErrors.add(this.messageService.getMessageByCode("msg.error.validation.database.invalid"));
			}

		}

		return listErrors;
	}

}
