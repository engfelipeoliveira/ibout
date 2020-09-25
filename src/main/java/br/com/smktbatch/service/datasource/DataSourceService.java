package br.com.smktbatch.service.datasource;

import java.util.List;

import br.com.smktbatch.model.local.Product;
import br.com.smktbatch.model.remote.Mapping;
import br.com.smktbatch.model.remote.Parameter;

public interface DataSourceService {

	List<Product> read(Parameter parameter, Mapping mapping);

}
