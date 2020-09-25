package br.com.smktbatch.model.local;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Exclude
	private Long id;
	
	private String code;

	private String description;
	
	private String brand;
	
	private String complement;
	
	private String groupProduct;

	private String price;
	
	private String priceSold;
	
	private String priceClub;
	
	private String sold;

	private String stock;
	
	private String internalCode;
	
	private Long idClient;
	
	private String bowl;
	
	private String photo;
	
	private String unit;
	
	private String visible;
	
}
