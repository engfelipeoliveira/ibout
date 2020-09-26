package br.com.ibout.dto;

import java.io.Serializable;

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
public class RequestInsertProductDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String codigo;
	private String descricao;
	private String marca;
	private String complemento;
	private String grupo;
	private String preco;
	private String preco_oferta;
	private String preco_clube;
	private Long oferta;
	private String estoque;
	private String codigo_interno;
	private String vasilha;
	private Long id_estabelecimento;
	private String foto;
	private String unidade;
	private Long visivel;
}
