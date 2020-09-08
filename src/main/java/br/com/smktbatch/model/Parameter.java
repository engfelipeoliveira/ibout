package br.com.smktbatch.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class Parameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// generic
	@Id
	private Long id;
	@OneToOne(targetEntity = Client.class)
	private Client client;
	private String dataSource;
	private Long hourJob;
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
