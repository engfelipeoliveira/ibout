package br.com.smktbatch.service.datasource;

import static br.com.smktbatch.enums.DataSource.TXT;
import static br.com.smktbatch.service.datasource.FileUtils.filterFile;
import static br.com.smktbatch.service.datasource.FileUtils.moveFile;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.smktbatch.model.local.Product;
import br.com.smktbatch.model.remote.Mapping;
import br.com.smktbatch.model.remote.Parameter;

@Service
public class TxtServiceImpl implements DataSourceService {
	
	@Override
	public List<Product> read(Parameter parameter, Mapping mapping) {
		List<Product> listProduct = new ArrayList<Product>();

		FilenameFilter filter = filterFile(TXT);
		File[] files = new File(parameter.getDirSource()).listFiles(filter);
		for (File file : files) {
			try {
				listProduct = readProducts(new FileInputStream(file), parameter, mapping);				
				moveFile(parameter, file);
			}  catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return listProduct;
	}

	private List<Product> readProducts(InputStream inputStream, Parameter parameter, Mapping mapping) throws IOException {
		List<Product> listProduct = new ArrayList<Product>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			if(parameter.isHeader()) {
				br.readLine();
			}
			
			while ((line = br.readLine()) != null) {
				listProduct.add(this.map(mapping, line, parameter.getFileDelimiter()));
			}
			br.close();
			inputStream.close();
		}
		return listProduct;
	}
	
	private Product map(Mapping mapping, String line, String fileDelimiter) {
		String[] columns = split(line, fileDelimiter);
		return Product.builder()
				.clientId(mapping.getClient().getId())
				.code(columns[mapping.getCode()] != null ? trimToNull(columns[mapping.getCode()]) : null)
				.brand(columns[mapping.getBrand()] != null ? trimToNull(columns[mapping.getBrand()]) : null)
				.complement(columns[mapping.getCode()] != null ? trimToNull(columns[mapping.getComplement()]) : null)
				.description(columns[mapping.getDescription()] != null ? trimToNull(columns[mapping.getDescription()]) : null)
				.name(columns[mapping.getName()] != null ? trimToNull(columns[mapping.getName()]) : null)
				.price(columns[mapping.getPrice()] != null ? trimToNull(columns[mapping.getPrice()]) : null)
				.build();
	}

}
