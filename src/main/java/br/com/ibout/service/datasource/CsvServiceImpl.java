package br.com.ibout.service.datasource;

import static br.com.ibout.enums.DataSource.CSV;
import static br.com.ibout.service.datasource.FileUtils.filterFile;
import static br.com.ibout.service.datasource.FileUtils.moveFile;
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

import br.com.ibout.model.local.Product;
import br.com.ibout.model.remote.Mapping;
import br.com.ibout.model.remote.Parameter;

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
				.code(mapping.getCode() != null && line[mapping.getCode()] != null ? trimToNull(line[mapping.getCode()]) : null)
				.description(mapping.getDescription() != null && line[mapping.getDescription()] != null ? trimToNull(line[mapping.getDescription()]) : null)
				.brand(mapping.getBrand() != null && line[mapping.getBrand()] != null ? trimToNull(line[mapping.getBrand()]) : null)
				.complement(mapping.getComplement() != null && line[mapping.getCode()] != null ? trimToNull(line[mapping.getComplement()]) : null)
				.groupProduct(mapping.getGroupProduct() != null && line[mapping.getGroupProduct()] != null ? trimToNull(line[mapping.getGroupProduct()]) : null)
				.price(mapping.getPrice() != null && line[mapping.getPrice()] != null ? trimToNull(line[mapping.getPrice()]) : null)
				.priceSold(mapping.getPriceSold() != null && line[mapping.getPriceSold()] != null ? trimToNull(line[mapping.getPriceSold()]) : null)
				.priceClub(mapping.getPriceClub() != null && line[mapping.getPriceClub()] != null ? trimToNull(line[mapping.getPriceClub()]) : null)
				.sold(mapping.getSold() != null && line[mapping.getSold()] != null ? trimToNull(line[mapping.getSold()]) : null)
				.stock(mapping.getStock() != null && line[mapping.getStock()] != null ? trimToNull(line[mapping.getStock()]) : null)
				.internalCode(mapping.getInternalCode() != null && line[mapping.getInternalCode()] != null ? trimToNull(line[mapping.getInternalCode()]) : null)
				.bowl(mapping.getBowl() != null && line[mapping.getBowl()] != null ? trimToNull(line[mapping.getBowl()]) : null)
				.photo(mapping.getPhoto() != null && line[mapping.getPhoto()] != null ? trimToNull(line[mapping.getPhoto()]) : null)
				.unit(mapping.getUnit() != null && line[mapping.getUnit()] != null ? trimToNull(line[mapping.getUnit()]) : null)
				.visible(mapping.getVisible() != null && line[mapping.getVisible()] != null ? trimToNull(line[mapping.getVisible()]) : null)
				.build();
		
		
		
		
		
	}


}
