package br.com.smktbatch.service.product;

import java.util.List;

import br.com.smktbatch.model.local.Product;

public interface ProductService {
	
	Product createOrUpdateProduct(Product product);
	
	List<Product> findAll();

}
