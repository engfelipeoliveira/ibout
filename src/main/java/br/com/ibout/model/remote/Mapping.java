package br.com.ibout.model.remote;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long idClient;

	private Integer code;

	private Integer description;
	
	private Integer brand;
	
	private Integer complement;
	
	private Integer groupProduct;

	private Integer price;
	
	private Integer priceSold;
	
	private Integer priceClub;
	
	private Integer sold;

	private Integer stock;
	
	private Integer internalCode;
	
	private Integer bowl;
	
	private Integer photo;
	
	private Integer unit;
	
	private Integer visible;

}
