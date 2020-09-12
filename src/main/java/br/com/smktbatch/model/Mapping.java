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
public class Mapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@OneToOne(targetEntity = Client.class, optional = false)
	private Client client;

	private Long code;

	private Long name;

	private Long brand;

	private Long description;

	private Long complement;

	private Long price;

}
