package br.com.smktbatch.service;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import br.com.smktbatch.model.Parameter;

@Service
public class CsvServiceImpl implements DataSourceService {

	private static final Logger LOG = getLogger(CsvServiceImpl.class);

	@Override
	public void read(Parameter parameter) {
		LOG.info("read()");

	}

}
