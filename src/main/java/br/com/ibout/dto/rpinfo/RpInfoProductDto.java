package br.com.ibout.dto.rpinfo;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class RpInfoProductDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("Codigo")
	private String code;
	
	@SerializedName("CodigoBarras")
	private String barCode;
	
	@SerializedName("Descricao")
	private String description;
	
	@SerializedName("Complemento")
	private String complement;
	
	@SerializedName("Marca")
	private String brand;
	
	@SerializedName("Grupo")
	private String group;
	
	@SerializedName("CodigoDepartamento")
	private Long codeDepartment;
	
	@SerializedName("Ativo")
	private Boolean active;
}
