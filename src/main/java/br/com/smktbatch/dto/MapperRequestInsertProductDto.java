package br.com.smktbatch.dto;

import static org.apache.commons.lang3.StringUtils.replace;

import br.com.smktbatch.model.local.Product;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class MapperRequestInsertProductDto {
	
	public static RequestInsertProductDto fromProductDto(Product product) {
		return RequestInsertProductDto.builder()
				.codigo(product.getCode())
				.descricao(product.getDescription())
				.marca(product.getBrand())
				.complemento(product.getComplement())
				.grupo(product.getGroupProduct())
				.preco(product.getPrice() != null ? replace(product.getPrice(), ",", ".") : null)
				.preco_oferta(product.getPriceSold() != null ? replace(product.getPriceSold(), ",", ".") : null)
				.preco_clube(product.getPriceClub() != null ? replace(product.getPriceClub(), ",", ".") : null)
				.oferta(0L) //.oferta(product.getSold())
				.estoque(product.getStock())
				.codigo_interno(product.getInternalCode())
				.vasilha(product.getBowl())
				.id_estabelecimento(product.getIdClient())
				.foto("img/" + product.getCode() + ".png")
				.unidade("UN")
				.visivel(1L)
				.build();
	}

}
