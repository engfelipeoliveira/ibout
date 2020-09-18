package br.com.smktbatch.service.datasource;

import br.com.smktbatch.model.remote.Mapping;
import br.com.smktbatch.model.remote.Parameter;

public interface DataSourceService {

	void read(Parameter parameter, Mapping mapping);

}
