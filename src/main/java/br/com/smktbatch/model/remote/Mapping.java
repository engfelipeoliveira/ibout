package br.com.smktbatch.model.remote;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

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
	
	private Long idClient;

	private int code;

	private int description;
	
	private int brand;
	
	private int complement;
	
	private int groupProduct;

	private int price;
	
	private int priceSold;
	
	private int priceClub;
	
	private int sold;

	private int stock;
	
	private int internalCode;
	
	private int bowl;
	
	private int photo;
	
	private int unit;
	
	private int visible;

}
