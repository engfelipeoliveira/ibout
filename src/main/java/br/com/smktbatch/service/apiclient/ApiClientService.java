package br.com.smktbatch.service.apiclient;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import br.com.smktbatch.dto.RequestInsertProductDto;
import br.com.smktbatch.model.remote.Parameter;

public interface ApiClientService {
	
	void callInsertProduct(String tokenClient, Long idClient, List<RequestInsertProductDto> requestInsertProductDto, Parameter parameter) throws ClientProtocolException, IOException;

}
