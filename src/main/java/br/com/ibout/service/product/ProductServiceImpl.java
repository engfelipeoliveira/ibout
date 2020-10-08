package br.com.ibout.service.product;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ibout.model.local.Product;
import br.com.ibout.repository.local.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override 
	public Product createOrUpdate(Product product) {
		return productRepository.save(product);
	}
	
	@Override 
	public void createOrUpdateAll(List<Product> products) {
		productRepository.saveAll(products);
	}

	@Override
	public List<Product> getAll() {
		return this.productRepository.findAll();
	}

	@Override
	public void deleteAll() {
		this.productRepository.deleteAll();
	}

}
