package br.com.smktbatch.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Parameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// generic
	@Id
	private Long id;
	private Long idClient;
	private String dataSource;
	private String hourJob;
	private boolean importAll;
	
	// csv and txt
	private String fileDelimiter;
	private String dirSource;
	private String dirTarget;
	private boolean moveFileAfterRead;
	private boolean header;
	
	// excel
	private String excelSheet;
		
	// database
	private String sgbd;
	private String bdServer;
	private String bdPort;
	private String bdUser;
	private String bdPass;
	private String bdSql;
	

}
