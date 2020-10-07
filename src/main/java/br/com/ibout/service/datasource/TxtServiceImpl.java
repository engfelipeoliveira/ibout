package br.com.ibout.service.datasource;

import static br.com.ibout.enums.DataSource.TXT;
import static br.com.ibout.service.datasource.FileUtils.filterFile;
import static br.com.ibout.service.datasource.FileUtils.moveFile;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import br.com.ibout.model.local.Product;
import br.com.ibout.model.remote.Mapping;
import br.com.ibout.model.remote.Parameter;

@Service
public class TxtServiceImpl implements DataSourceService {
	
	private static final Logger LOG = getLogger(TxtServiceImpl.class);
	
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

	private List<Product> readProducts(InputStream inputStream, Parameter parameter, Mapping mapping)  {
		List<Product> listProduct = new ArrayList<Product>();
		String line = null;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			if(parameter.isHeader()) {
				br.readLine();
			}
			
			while ((line = br.readLine()) != null) {
				try {
					listProduct.add(this.map(mapping, line, parameter.getFileDelimiter()));	
				} catch (Exception e) {
					LOG.error(format("Falha ao ler linha do arquivo. Processo continua : %s %s", line, e));
				}
			}
			br.close();
			inputStream.close();
		} catch (Exception e) {
			LOG.error(format("Falha ao ler linha do arquivo. Processo continua : %s %s", line, e));
		}
		return listProduct;
	}
	
	private Product map(Mapping mapping, String line, String fileDelimiter) {
		String[] columns = split(line, fileDelimiter);
		return Product.builder()
				.idClient(mapping.getIdClient())
				.code(mapping.getCode() != null && columns[mapping.getCode()] != null ? trimToNull(columns[mapping.getCode()]) : null)
				.description(mapping.getDescription() != null && columns[mapping.getDescription()] != null ? trimToNull(columns[mapping.getDescription()]) : null)
				.brand(mapping.getBrand() != null && columns[mapping.getBrand()] != null ? trimToNull(columns[mapping.getBrand()]) : null)
				.complement(mapping.getComplement() != null && columns[mapping.getCode()] != null ? trimToNull(columns[mapping.getComplement()]) : null)
				.groupProduct(mapping.getGroupProduct() != null && columns[mapping.getGroupProduct()] != null ? trimToNull(columns[mapping.getGroupProduct()]) : null)
				.price(mapping.getPrice() != null && columns[mapping.getPrice()] != null ? trimToNull(columns[mapping.getPrice()]) : null)
				.priceSold(mapping.getPriceSold() != null && columns[mapping.getPriceSold()] != null ? trimToNull(columns[mapping.getPriceSold()]) : null)
				.priceClub(mapping.getPriceClub() != null && columns[mapping.getPriceClub()] != null ? trimToNull(columns[mapping.getPriceClub()]) : null)
				.sold(mapping.getSold() != null && columns[mapping.getSold()] != null ? trimToNull(columns[mapping.getSold()]) : null)
				.stock(mapping.getStock() != null && columns[mapping.getStock()] != null ? trimToNull(columns[mapping.getStock()]) : null)
				.internalCode(mapping.getInternalCode() != null && columns[mapping.getInternalCode()] != null ? trimToNull(columns[mapping.getInternalCode()]) : null)
				.bowl(mapping.getBowl() != null && columns[mapping.getBowl()] != null ? trimToNull(columns[mapping.getBowl()]) : null)
				.photo(mapping.getPhoto() != null && columns[mapping.getPhoto()] != null ? trimToNull(columns[mapping.getPhoto()]) : null)
				.unit(mapping.getUnit() != null && columns[mapping.getUnit()] != null ? trimToNull(columns[mapping.getUnit()]) : null)
				.build();
		
	}

}
