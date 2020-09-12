package br.com.smktbatch.service.datasource;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import br.com.smktbatch.model.Mapping;
import br.com.smktbatch.model.Parameter;

@Service
public class DbServiceImpl implements DataSourceService {

	private static final Logger LOG = getLogger(DbServiceImpl.class);

	@Override
	public void read(Parameter parameter, Mapping mapping) {
		LOG.info("read()");

	}

}
