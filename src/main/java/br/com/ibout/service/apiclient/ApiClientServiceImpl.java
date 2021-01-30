package br.com.ibout.service.apiclient;

import static java.lang.String.format;
import static org.apache.http.impl.client.HttpClients.createDefault;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.ibout.dto.RequestInsertProductDto;
import br.com.ibout.model.remote.Parameter;

@Service
public class ApiClientServiceImpl implements ApiClientService {
	
	@Override
	public String callInsertProduct(String tokenClient, Long idClient, List<RequestInsertProductDto> requestInsertProductDto, Parameter parameter) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = createDefault();
		String result = null;
		CloseableHttpResponse response = null;
		try {
			HttpPost httpPost = new HttpPost(format("%s%s/%s", parameter.getApiUrlInsertProduct(), idClient, tokenClient));
			Gson gson = new Gson();
			String json = gson.toJson(requestInsertProductDto);
			
			StringEntity entity = new StringEntity(json);
			httpPost.setEntity(entity);
			httpPost.setHeader("Content-type", "application/json");
			response = httpClient.execute(httpPost);
			
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
		} finally {
			response.close();
			httpClient.close();
		}
		
		return result;
	}

}
