package br.com.smktbatch.service.datasource;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import br.com.smktbatch.model.local.Product;
import br.com.smktbatch.model.remote.Mapping;
import br.com.smktbatch.model.remote.Parameter;

@Service
public class TxtServiceImpl implements DataSourceService {

	private static final Logger LOG = getLogger(TxtServiceImpl.class);

	@Override
	public void read(Parameter parameter, Mapping mapping) {
		LOG.info("read()");
		File dirSource = new File(parameter.getDirSource());
		String dirTarget = parameter.getDirTarget();
		String fileDelimiter = parameter.getFileDelimiter();
		boolean moveFileAfterRead = parameter.isMoveFileAfterRead();

		FilenameFilter txtFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		};

		File[] files = dirSource.listFiles(txtFilter);
		for (File file : files) {
			try {
				readProducts(new FileInputStream(file), fileDelimiter, mapping);
				
				if(moveFileAfterRead && !StringUtils.isBlank(dirTarget)) {
					//Files.move(file.toPath(), new File(dirTarget).toPath(), StandardCopyOption.ATOMIC_MOVE);
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void readProducts(InputStream inputStream, String fileDelimiter, Mapping mapping) throws IOException {
		LOG.info("readProducts()");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				Product productDto = map(mapping, line, fileDelimiter);
				
				System.out.println(productDto.toString());
			}
			br.close();
			inputStream.close();
		}
	}
	
	private Product map(Mapping mapping, String line, String fileDelimiter) {
		String[] columns = StringUtils.split(line, fileDelimiter);
		return Product.builder()
				.clientId(mapping.getClient().getId())
				.code(columns[mapping.getCode()] != null ? StringUtils.trimToNull(columns[mapping.getCode()]) : null)
				.brand(columns[mapping.getBrand()] != null ? StringUtils.trimToNull(columns[mapping.getBrand()]) : null)
				.complement(columns[mapping.getCode()] != null ? StringUtils.trimToNull(columns[mapping.getComplement()]) : null)
				.description(columns[mapping.getDescription()] != null ? StringUtils.trimToNull(columns[mapping.getDescription()]) : null)
				.name(columns[mapping.getName()] != null ? StringUtils.trimToNull(columns[mapping.getName()]) : null)
				.price(columns[mapping.getPrice()] != null ? StringUtils.trimToNull(columns[mapping.getPrice()]) : null)
				.build();
	}

}