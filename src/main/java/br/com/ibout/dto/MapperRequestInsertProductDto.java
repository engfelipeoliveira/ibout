package br.com.ibout.dto;

import static java.lang.String.format;
import static java.text.Normalizer.normalize;
import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.apache.commons.lang3.StringUtils.replaceEach;
import static org.apache.commons.lang3.StringUtils.stripAccents;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import java.math.BigDecimal;
import java.text.Normalizer;

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
	
	private static String normalizeText(String term) {
		String[] searchList = {"?","*","'"};
		String[] replacementList = {"","",""};
		
		term = trimToEmpty(term);
		term = stripAccents(term);
		term = replaceEach(term, searchList, replacementList);
		term = term.replaceAll("[^\\p{Print}]", "");
		term = normalize(term, Normalizer.Form.NFD);
		term = isBlank(term) ? " " : term;
		
		return term;
	}
	
	public static RequestInsertProductDto fromProductDto(Product product, Parameter parameter, Long visible) {
		return RequestInsertProductDto.builder()
				.codigo(normalizeText(product.getCode()))
				.descricao(normalizeText(product.getDescription()))
				.marca(normalizeText(product.getBrand()))
				.complemento(normalizeText(product.getComplement()))
				.grupo(normalizeText(product.getGroupProduct()))
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
				.visivel(visible)
				.build();
	}

}
