package br.com.smktbatch.service.apiclient;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.smktbatch.dto.RequestInsertProductDto;
import br.com.smktbatch.model.remote.Parameter;

@Service
public class ApiClientServiceImpl implements ApiClientService {
	
	@Override
	public void callInsertProduct(String tokenClient, Long idClient, List<RequestInsertProductDto> requestInsertProductDto, Parameter parameter) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(parameter.getApiUrlInsertProduct() + idClient + "/" + tokenClient);
			Gson gson = new Gson();
			String json = gson.toJson(requestInsertProductDto); 
			
			StringEntity entity = new StringEntity(json);
			httpPost.setEntity(entity);
			httpPost.setHeader("Content-type", "application/json");
			CloseableHttpResponse response = httpClient.execute(httpPost);

			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity());
			}
		} finally {
			httpClient.close();
		}
		
		
	}


}
