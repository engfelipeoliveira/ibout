package br.com.ibout.dto.rpinfo;

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
public class RpInfoReqAuthSessionDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String usuario;
	private String senha;
}
