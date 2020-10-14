package br.com.ibout.service.datasource;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.apache.http.impl.client.HttpClients.createDefault;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.ibout.dto.rpinfo.RpInfoProductDto;
import br.com.ibout.dto.rpinfo.RpInfoReqAuthSessionDto;
import br.com.ibout.dto.rpinfo.RpInfoResListProductsDto;
import br.com.ibout.dto.rpinfo.RpInfoResponseDto;
import br.com.ibout.model.local.Product;
import br.com.ibout.model.remote.Mapping;
import br.com.ibout.model.remote.Parameter;

@Service
public class RpInfoServiceImpl implements DataSourceService {
	
	private final String PATH_RP_AUTH_SESSION = "/v1.1/auth";
	private final String PATH_RP_LOGOUT = "/v1.1/logout";
	private final String PATH_RP_LIST_PRODUCTS = "/v2.0/produtounidade/listaprodutos/0/unidade/%s/detalhado/marcados";
	
	private RpInfoResponseDto loginRp(Parameter parameter) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = createDefault();
		RpInfoResponseDto result = null;
		try {
			HttpPost httpPost = new HttpPost(parameter.getRpiRpUrl() + PATH_RP_AUTH_SESSION);
			Gson gson = new Gson();
			String json = gson.toJson(RpInfoReqAuthSessionDto.builder().user(parameter.getRpiRpUser()).pass(parameter.getRpiRpPass()).build());
			
			StringEntity entity = new StringEntity(json);
			httpPost.setEntity(entity);
			httpPost.setHeader("Content-type", "application/json");
			CloseableHttpResponse response = httpClient.execute(httpPost);

			if (response.getStatusLine().getStatusCode() == 200) {
				result = new Gson().fromJson(EntityUtils.toString(response.getEntity()), RpInfoResponseDto.class);
			}
			response.close();
		} finally {
			httpClient.close();
		}

		return result;
	}
	
	private void logoutRp(Parameter parameter, String token) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = createDefault();
		try {
			HttpGet httpGet = new HttpGet(parameter.getRpiRpUrl() + PATH_RP_LOGOUT);
			httpGet.setHeader("Content-type", "application/json");
			httpGet.setHeader("token", token);
			
			CloseableHttpResponse response = httpClient.execute(httpGet);
			response.close();
		} finally {
			httpClient.close();
		}
	}

	
	private RpInfoResListProductsDto listRpProducts(Parameter parameter, String token) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = createDefault();
		RpInfoResListProductsDto result = null;
		try {
			HttpGet httpGet = new HttpGet(parameter.getRpiRpUrl() + format(PATH_RP_LOGOUT, parameter.getCnpjClient()));
			httpGet.setHeader("Content-type", "application/json");
			httpGet.setHeader("token", token);
			
			CloseableHttpResponse response = httpClient.execute(httpGet);

			if (response.getStatusLine().getStatusCode() == 200) {
				result = new Gson().fromJson(EntityUtils.toString(response.getEntity()), RpInfoResListProductsDto.class);
			}
			response.close();
		} finally {
			httpClient.close();
		}
		
		return result;
	}
	
	private Product map(Parameter parameter, RpInfoProductDto rpInfoProduct)  {
		return Product.builder()
				.idClient(parameter.getIdClient())
				.code(trimToNull(rpInfoProduct.getCode()))
				.description(trimToNull(rpInfoProduct.getDescription()))
				.brand(trimToNull(rpInfoProduct.getBrand()))
				.complement(trimToNull(rpInfoProduct.getComplement()))
				.groupProduct(trimToNull(rpInfoProduct.getGroup()))
//				.price(mapping.getPrice() != null && rs.getString(mapping.getPrice() + 1) != null ? trimToNull(rs.getString(mapping.getPrice() + 1)) : null)
//				.priceSold(mapping.getPriceSold() != null && rs.getString(mapping.getPriceSold() + 1) != null ? trimToNull(rs.getString(mapping.getPriceSold() + 1)) : null)
//				.priceClub(mapping.getPriceClub() != null && rs.getString(mapping.getPriceClub() + 1) != null ? trimToNull(rs.getString(mapping.getPriceClub() + 1)) : null)
//				.sold(mapping.getSold() != null && rs.getString(mapping.getSold() + 1) != null ? trimToNull(rs.getString(mapping.getSold() + 1)) : null)
//				.stock(mapping.getStock() != null && rs.getString(mapping.getStock() + 1) != null ? trimToNull(rs.getString(mapping.getStock() + 1)) : null)
//				.internalCode(mapping.getInternalCode() != null && rs.getString(mapping.getInternalCode() + 1) != null ? trimToNull(rs.getString(mapping.getInternalCode() + 1)) : null)
//				.bowl(mapping.getBowl() != null && rs.getString(mapping.getBowl() + 1) != null ? trimToNull(rs.getString(mapping.getBowl() + 1)) : null)
//				.photo(mapping.getPhoto() != null && rs.getString(mapping.getPhoto() + 1) != null ? trimToNull(rs.getString(mapping.getPhoto() + 1)) : null)
//				.unit(mapping.getUnit() != null && rs.getString(mapping.getUnit() + 1) != null ? trimToNull(rs.getString(mapping.getUnit() + 1)) : null)
				.build();
	}

	@Override
	public List<Product> read(Parameter parameter, Mapping mapping) {
		List<Product> listProduct = new ArrayList<Product>();
		
		try {
			RpInfoResponseDto rpInfoResponseDto = loginRp(parameter);
			
			RpInfoResListProductsDto listRpProducts = listRpProducts(parameter, rpInfoResponseDto.getToken());
			listRpProducts.getProducts().stream().filter(p -> p.getActive()).forEach(rpInfoProduct -> {
				
				
				
				listProduct.add(map(parameter, rpInfoProduct));	
			});
			
			logoutRp(parameter, rpInfoResponseDto.getToken()); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listProduct;
	}
	

}
