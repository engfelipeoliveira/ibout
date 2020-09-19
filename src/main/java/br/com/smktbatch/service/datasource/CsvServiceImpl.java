package br.com.smktbatch.service.datasource;

import static br.com.smktbatch.enums.DataSource.CSV;
import static java.nio.file.Files.move;
import static java.nio.file.Paths.get;
import static java.time.LocalDateTime.now;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import java.io.File;
import java.io.FileNotFoundException;
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
		String dirTarget = parameter.getDirTarget();

		FilenameFilter txtFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toUpperCase().endsWith(CSV.toString());
			}
		};

		File[] files = new File(parameter.getDirSource()).listFiles(txtFilter);
		for (File file : files) {
			try {
				listProduct = readProducts(file, parameter, mapping);				
				if(parameter.isMoveFileAfterRead() && !isBlank(dirTarget)) {					
					move(get(file.getAbsolutePath()), get(new File(dirTarget + md5Hex(now().toString()).toUpperCase() + "_" + file.getName()).getAbsolutePath())); 					
				}				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
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
		    while ((line = csvReader.readNext()) != null) {
		    	listProduct.add(this.map(mapping, line));
		    }
		} catch (CsvValidationException e) {
			e.printStackTrace();
		}

		return listProduct;
	}
	
	private Product map(Mapping mapping, String[] line) {
		return Product.builder()
				.clientId(mapping.getClient().getId())
				.code(line[mapping.getCode()] != null ? trimToNull(line[mapping.getCode()]) : null)
				.brand(line[mapping.getBrand()] != null ? trimToNull(line[mapping.getBrand()]) : null)
				.complement(line[mapping.getCode()] != null ? trimToNull(line[mapping.getComplement()]) : null)
				.description(line[mapping.getDescription()] != null ? trimToNull(line[mapping.getDescription()]) : null)
				.name(line[mapping.getName()] != null ? trimToNull(line[mapping.getName()]) : null)
				.price(line[mapping.getPrice()] != null ? trimToNull(line[mapping.getPrice()]) : null)
				.build();
	}


}
