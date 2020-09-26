package br.com.ibout.service.datasource;

import static java.nio.file.Files.move;
import static java.nio.file.Paths.get;
import static java.time.LocalDateTime.now;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import br.com.ibout.enums.DataSource;
import br.com.ibout.model.remote.Parameter;

public class FileUtils {

	public static FilenameFilter filterFile(DataSource... dataSources) {
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				for (DataSource dataSource : dataSources) {
		             if (name.toUpperCase().endsWith(dataSource.toString())) {
		                 return true;
		             }
		        }
				return false;
			}
		};
		return filter;
	}
	
	public static void moveFile(Parameter parameter, File file) throws IOException {
		if(parameter.isMoveFileAfterRead() && !isBlank(parameter.getDirTarget())) {					
			move(get(file.getAbsolutePath()), get(new File(parameter.getDirTarget() + md5Hex(now().toString()).toUpperCase() + "_" + file.getName()).getAbsolutePath())); 					
		}
	}
	
}
