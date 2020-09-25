package br.com.smktbatch.service.datasource;

import static br.com.smktbatch.enums.DataSource.CSV;
import static br.com.smktbatch.service.datasource.FileUtils.filterFile;
import static br.com.smktbatch.service.datasource.FileUtils.moveFile;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import br.com.smktbatch.model.local.Product;
import br.com.smktbatch.model.remote.Mapping;
import br.com.smktbatch.model.remote.Parameter;

@Service
public class CsvServiceImpl implements DataSourceService {

	@Override
	public List<Product> read(Parameter parameter, Mapping mapping) {
		List<Product> listProduct = new ArrayList<Product>();

		FilenameFilter filter = filterFile(CSV);
		File[] files = new File(parameter.getDirSource()).listFiles(filter);
		for (File file : files) {
			try {
				listProduct = readProducts(file, parameter, mapping);				
				moveFile(parameter, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return listProduct;
	}

	private List<Product> readProducts(File file, Parameter parameter, Mapping mapping) throws IOException {
		List<Product> listProduct = new ArrayList<Product>();
		
		try (CSVReader csvReader = new CSVReader(new FileReader(file));) {
		    String[] line = null;
		    if(parameter.isHeader()) {
		    	csvReader.readNext();
			}
		    
		    while ((line = csvReader.readNext()) != null) {
		    	listProduct.add(this.map(mapping, line));
		    }
		    csvReader.close();
		} catch (CsvValidationException e) {
			e.printStackTrace();
		}

		return listProduct;
	}
	
	private Product map(Mapping mapping, String[] line) {
		return Product.builder()
				.idClient(mapping.getIdClient())
				.code(line[mapping.getCode()] != null ? trimToNull(line[mapping.getCode()]) : null)
				.brand(line[mapping.getBrand()] != null ? trimToNull(line[mapping.getBrand()]) : null)
				.complement(line[mapping.getCode()] != null ? trimToNull(line[mapping.getComplement()]) : null)
				.description(line[mapping.getDescription()] != null ? trimToNull(line[mapping.getDescription()]) : null)
				.price(line[mapping.getPrice()] != null ? trimToNull(line[mapping.getPrice()]) : null)
				.build();
	}


}
