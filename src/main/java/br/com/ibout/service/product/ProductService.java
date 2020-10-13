package br.com.ibout.service.product;

import java.util.List;

import br.com.ibout.model.local.Product;

public interface ProductService {
	
	Product createOrUpdate(Product product);
	List<Product> getAll();
	void deleteAll();
	void createOrUpdateAll(List<Product> products);
	void deleteAll(List<Product> products);
}

