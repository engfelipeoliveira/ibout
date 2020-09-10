package br.com.smktbatch.service;

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

import br.com.smktbatch.model.Mapping;
import br.com.smktbatch.model.Parameter;

@Service
public class TxtServiceImpl implements DataSourceService {

	private static final Logger LOG = getLogger(TxtServiceImpl.class);

	@Override
	public void read(Parameter parameter, Mapping mapping) {
		LOG.info("read()");
		File dirSource = new File(parameter.getDirSource());
		String dirTarget = parameter.getDirTarget();x
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
				readProducts(new FileInputStream(file), fileDelimiter);
				
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

	private void readProducts(InputStream inputStream, String fileDelimiter) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] columns = StringUtils.split(line, fileDelimiter);
				System.out.println(columns[1]);
			}
			br.close();
			inputStream.close();
		}
	}

}
