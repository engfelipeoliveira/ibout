package br.com.ibout.service.datasource;

import java.util.List;

import br.com.ibout.model.local.Product;
import br.com.ibout.model.remote.Mapping;
import br.com.ibout.model.remote.Parameter;

public interface DataSourceService {

	List<Product> read(Parameter parameter, Mapping mapping);

}
