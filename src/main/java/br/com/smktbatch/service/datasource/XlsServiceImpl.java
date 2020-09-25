package br.com.smktbatch.service.datasource;

import static br.com.smktbatch.enums.DataSource.XLS;
import static br.com.smktbatch.enums.DataSource.XLSX;
import static br.com.smktbatch.service.datasource.FileUtils.filterFile;
import static br.com.smktbatch.service.datasource.FileUtils.moveFile;
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

import br.com.smktbatch.model.local.Product;
import br.com.smktbatch.model.remote.Mapping;
import br.com.smktbatch.model.remote.Parameter;

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
		row.getCell(mapping.getCode()).setCellType(STRING);
		row.getCell(mapping.getBrand()).setCellType(STRING);
		row.getCell(mapping.getComplement()).setCellType(STRING);
		row.getCell(mapping.getDescription()).setCellType(STRING);
		row.getCell(mapping.getPrice()).setCellType(STRING);
		return Product.builder()
				.idClient(mapping.getIdClient())
				.code(row.getCell(mapping.getCode()) != null ? trimToNull(row.getCell(mapping.getCode()).getStringCellValue()) : null)
				.brand(row.getCell(mapping.getBrand()) != null ? trimToNull(row.getCell(mapping.getBrand()).getStringCellValue()) : null)
				.complement(row.getCell(mapping.getCode()) != null ? trimToNull(row.getCell(mapping.getComplement()).getStringCellValue()) : null)
				.description(row.getCell(mapping.getDescription()) != null ? trimToNull(row.getCell(mapping.getDescription()).getStringCellValue()) : null)
				.price(row.getCell(mapping.getPrice()) != null ? trimToNull(row.getCell(mapping.getPrice()).getStringCellValue()) : null)
				.build();
	}

}
