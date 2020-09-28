package br.com.ibout.service.datasource;

import static br.com.ibout.enums.DataSource.XLS;
import static br.com.ibout.enums.DataSource.XLSX;
import static br.com.ibout.service.datasource.FileUtils.filterFile;
import static br.com.ibout.service.datasource.FileUtils.moveFile;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.apache.poi.ss.usermodel.CellType.STRING;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import br.com.ibout.model.local.Product;
import br.com.ibout.model.remote.Mapping;
import br.com.ibout.model.remote.Parameter;

@Service
public class XlsServiceImpl implements DataSourceService {

	@Override
	public List<Product> read(Parameter parameter, Mapping mapping) {
		List<Product> listProduct = new ArrayList<Product>();

		FilenameFilter filter = filterFile(XLS, XLSX);
		File[] files = new File(parameter.getDirSource()).listFiles(filter);
		for (File file : files) {
			try {
				listProduct = readProducts(file, parameter, mapping);				
				moveFile(parameter, file);
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		return listProduct;
	}

	private List<Product> readProducts(File file, Parameter parameter, Mapping mapping) throws IOException {
		List<Product> listProduct = new ArrayList<Product>();
		
		FileInputStream fileInputStream = new FileInputStream(file);
		Workbook workbook = new XSSFWorkbook(fileInputStream);
		Sheet sheet;
		
		if(parameter.getExcelSheet() != null) {
			String[] sheets = split(parameter.getExcelSheet(), ",");
			for(String sheetName : sheets) {
				sheet = workbook.getSheet(sheetName);
				Iterator<Row> rowIterator = sheet.iterator();
				if(parameter.isHeader()) {
					rowIterator.next();
				}
				while (rowIterator.hasNext()) {
					listProduct.add(this.map(mapping, rowIterator.next()));
				}
			}
		}else {
			sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			if(parameter.isHeader()) {
				rowIterator.next();
			}
			while (rowIterator.hasNext()) {
				listProduct.add(this.map(mapping, rowIterator.next()));
			}
		}
		workbook.close();
		fileInputStream.close();
			
		return listProduct;
	}
	
	private Product map(Mapping mapping, Row row) {
		if(mapping.getCode() != null) { row.getCell(mapping.getCode()).setCellType(STRING); }
		if(mapping.getDescription() != null) { row.getCell(mapping.getDescription()).setCellType(STRING); }
		if(mapping.getBrand() != null) { row.getCell(mapping.getBrand()).setCellType(STRING); }
		if(mapping.getComplement() != null) { row.getCell(mapping.getComplement()).setCellType(STRING); }
		if(mapping.getGroupProduct()!= null) { row.getCell(mapping.getGroupProduct()).setCellType(STRING); }
		if(mapping.getPrice() != null) { row.getCell(mapping.getPrice()).setCellType(STRING); }
		if(mapping.getPriceClub() != null) { row.getCell(mapping.getPriceClub()).setCellType(STRING); }
		if(mapping.getPriceSold() != null) { row.getCell(mapping.getPriceSold()).setCellType(STRING); }
		if(mapping.getSold() != null) { row.getCell(mapping.getSold()).setCellType(STRING); }
		if(mapping.getStock() != null) { row.getCell(mapping.getStock()).setCellType(STRING); }
		if(mapping.getInternalCode() != null) { row.getCell(mapping.getInternalCode()).setCellType(STRING); }
		if(mapping.getBowl() != null) { row.getCell(mapping.getBowl()).setCellType(STRING); }
		if(mapping.getUnit() != null) { row.getCell(mapping.getUnit()).setCellType(STRING); }
		
		return Product.builder()
				.idClient(mapping.getIdClient())
				.code(mapping.getCode() != null && row.getCell(mapping.getCode()) != null ? trimToNull(row.getCell(mapping.getCode()).getStringCellValue()) : null)
				.description(mapping.getDescription() != null && row.getCell(mapping.getDescription()) != null ? trimToNull(row.getCell(mapping.getDescription()).getStringCellValue()) : null)
				.brand(mapping.getBrand() != null && row.getCell(mapping.getBrand()) != null ? trimToNull(row.getCell(mapping.getBrand()).getStringCellValue()) : null)
				.complement(mapping.getComplement() != null && row.getCell(mapping.getCode()) != null ? trimToNull(row.getCell(mapping.getComplement()).getStringCellValue()) : null)
				.groupProduct(mapping.getGroupProduct() != null && row.getCell(mapping.getGroupProduct()) != null ? trimToNull(row.getCell(mapping.getGroupProduct()).getStringCellValue()) : null)
				.price(mapping.getPrice() != null && row.getCell(mapping.getPrice()) != null ? trimToNull(row.getCell(mapping.getPrice()).getStringCellValue()) : null)
				.priceSold(mapping.getPriceSold() != null && row.getCell(mapping.getPriceSold()) != null ? trimToNull(row.getCell(mapping.getPriceSold()).getStringCellValue()) : null)
				.priceClub(mapping.getPriceClub() != null && row.getCell(mapping.getPriceClub()) != null ? trimToNull(row.getCell(mapping.getPriceClub()).getStringCellValue()) : null)
				.sold(mapping.getSold() != null && row.getCell(mapping.getSold()) != null ? trimToNull(row.getCell(mapping.getSold()).getStringCellValue()) : null)
				.stock(mapping.getStock() != null && row.getCell(mapping.getStock()) != null ? trimToNull(row.getCell(mapping.getStock()).getStringCellValue()) : null)
				.internalCode(mapping.getInternalCode() != null && row.getCell(mapping.getInternalCode()) != null ? trimToNull(row.getCell(mapping.getInternalCode()).getStringCellValue()) : null)
				.bowl(mapping.getBowl() != null && row.getCell(mapping.getBowl()) != null ? trimToNull(row.getCell(mapping.getBowl()).getStringCellValue()) : null)
				.photo(mapping.getPhoto() != null && row.getCell(mapping.getPhoto()) != null ? trimToNull(row.getCell(mapping.getPhoto()).getStringCellValue()) : null)
				.unit(mapping.getUnit() != null && row.getCell(mapping.getUnit()) != null ? trimToNull(row.getCell(mapping.getUnit()).getStringCellValue()) : null)
				.build();
	}

}
