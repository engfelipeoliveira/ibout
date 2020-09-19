package br.com.smktbatch.service.datasource;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.smktbatch.model.local.Product;
import br.com.smktbatch.model.remote.Mapping;
import br.com.smktbatch.model.remote.Parameter;

@Service
public class CsvServiceImpl implements DataSourceService {

	@Override
	public List<Product> read(Parameter parameter, Mapping mapping) {
		return null;
	}

}
