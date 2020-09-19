package br.com.smktbatch.model.remote;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import br.com.smktbatch.enums.DataSource;
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

	@OneToOne(targetEntity = Client.class, optional = false)
	private Client client;

	@Enumerated(EnumType.STRING)
	private DataSource dataSource;
	
	private boolean active;

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
