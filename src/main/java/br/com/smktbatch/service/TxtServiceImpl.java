package br.com.smktbatch.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.FilenameFilter;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import br.com.smktbatch.model.Parameter;

@Service
public class TxtServiceImpl implements DataSourceService {

	private static final Logger LOG = getLogger(TxtServiceImpl.class);

	@Override
	public void read(Parameter parameter) {
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
			System.out.println(file.getName());
		}

	}

}
