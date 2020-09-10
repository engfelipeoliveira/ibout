package br.com.smktbatch.service;

import br.com.smktbatch.model.Mapping;
import br.com.smktbatch.model.Parameter;

public interface DataSourceService {

	void read(Parameter parameter, Mapping mapping);

}
