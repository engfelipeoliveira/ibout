package br.com.ibout.dto.rpinfo;

import java.io.Serializable;
import java.util.List;

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
public class RpInfoResponseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String status;
	private List<RpInfoMessageDto> messages;
	private String token;
	private String tokenExpiration;
}
