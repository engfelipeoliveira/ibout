package br.com.ibout.dto;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import br.com.ibout.model.local.Product;
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
	
	private static String getUnitFromProductDesc(Product product) {
		if((!isBlank(product.getComplement()) && containsIgnoreCase(product.getComplement(), "KG")) ||
				(!isBlank(product.getDescription()) && containsIgnoreCase(product.getDescription(), "KG"))) {
			return "KG";
		}else {
			return "UN";			
		}
	}
	
	public static RequestInsertProductDto fromProductDto(Product product) {
		return RequestInsertProductDto.builder()
				.codigo(trimToEmpty(product.getCode()))
				.descricao(trimToEmpty(product.getDescription()))
				.marca(trimToEmpty(product.getBrand()))
				.complemento(trimToEmpty(product.getComplement()))
				.grupo(trimToEmpty(product.getGroupProduct()))
				.preco(!isBlank(product.getPrice()) ? replace(product.getPrice(), ",", ".") : null)
				.preco_oferta(!isBlank(product.getSold()) ? replace(product.getPriceSold(), ",", ".") : null)
				.preco_clube(!isBlank(product.getPriceClub()) ? replace(product.getPriceClub(), ",", ".") : null)
				.oferta(!isBlank(product.getSold()) ? Long.parseLong(product.getSold()) : 0L)
				.estoque(trimToEmpty(product.getStock()))
				.codigo_interno(trimToEmpty(product.getInternalCode()))
				.vasilha(trimToEmpty(product.getBowl()))
				.id_estabelecimento(product.getIdClient())
				.foto(isBlank(product.getPhoto()) ? format("%s%s%s", "img/", product.getCode(), ".png") : product.getPhoto())
				.unidade(isBlank(product.getUnit()) ? getUnitFromProductDesc(product) : product.getUnit())
				.visivel(1L)
				.build();
	}

}
