package br.com.smktbatch.service.product;

import java.util.List;

import br.com.smktbatch.model.local.Product;

public interface ProductService {
	
	Product createOrUpdate(Product product);
	List<Product> getAll();
	void deleteAll();
}
