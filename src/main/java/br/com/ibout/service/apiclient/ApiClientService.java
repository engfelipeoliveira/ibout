package br.com.ibout.service.apiclient;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import br.com.ibout.dto.RequestInsertProductDto;
import br.com.ibout.model.remote.Parameter;

public interface ApiClientService {
	
	String callInsertProduct(String tokenClient, Long idClient, List<RequestInsertProductDto> requestInsertProductDto, Parameter parameter) throws ClientProtocolException, IOException;

}
