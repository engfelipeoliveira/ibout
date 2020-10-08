package br.com.ibout.dto;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import java.math.BigDecimal;

import br.com.ibout.model.local.Product;
import br.com.ibout.model.remote.Parameter;
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
	
	private static String getUnitFromParameter(Product product, Parameter parameter) {
		if(isBlank(parameter.getTermMappingKg()) && !isBlank(product.getUnit())) {
			return product.getUnit();
		}else if(isBlank(parameter.getTermMappingKg()) && isBlank(product.getUnit())) {
			return "UN";
		}else if(!isBlank(parameter.getTermMappingKg()) && !isBlank(product.getUnit()) && contains(product.getUnit().toUpperCase(), parameter.getTermMappingKg().toUpperCase())){
			return "KG";
		} else {
			return "UN";
		}
	}
	
	public static RequestInsertProductDto fromProductDto(Product product, Parameter parameter) {
		return RequestInsertProductDto.builder()
				.codigo(trimToEmpty(product.getCode()))
				.descricao(trimToEmpty(product.getDescription()))
				.marca(trimToEmpty(product.getBrand()))
				.complemento(trimToEmpty(product.getComplement()))
				.grupo(trimToEmpty(product.getGroupProduct()))
				.preco(!isBlank(product.getPrice()) ? replace(product.getPrice(), ",", ".") : null)
				.preco_oferta(!isBlank(product.getSold()) ? replace(product.getPriceSold(), ",", ".") : null)
				.preco_clube(!isBlank(product.getPriceClub()) ? replace(product.getPriceClub(), ",", ".") : null)
				.oferta("S".equalsIgnoreCase(product.getSold()) ? 1L : 0L)
				.estoque(!isBlank(product.getStock()) && new BigDecimal(replace(product.getStock(), ",", ".")).compareTo(new BigDecimal("0")) > 0 ? product.getStock() : parameter.getMinStock())
				.codigo_interno(trimToEmpty(product.getInternalCode()))
				.vasilha(!isBlank(product.getBowl()) ? "S" : "N")
				.id_estabelecimento(product.getIdClient())
				.foto(isBlank(product.getPhoto()) ? format("%s%s%s", "img/", product.getCode(), ".png") : product.getPhoto())
				.unidade(getUnitFromParameter(product, parameter))
				.visivel(1L)
				.build();
	}

}
