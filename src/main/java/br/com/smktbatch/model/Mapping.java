package br.com.smktbatch.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class Mapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(targetEntity = Client.class, optional = false)
	private Client client;
	
	@Column(nullable = false)
	private Long name;
	
	@Column(nullable = false)
	private Long value;
	
	@Column(nullable = false)
	private Long sold;
	
	@Column(nullable = false)
	private Long quantity;
	

}
