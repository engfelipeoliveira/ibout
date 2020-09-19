package br.com.smktbatch.service.product;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.smktbatch.model.local.Product;
import br.com.smktbatch.repository.local.ProductRepository;

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
	public List<Product> getAll() {
		return this.productRepository.findAll();
	}

	@Override
	public void deleteAll() {
		this.productRepository.deleteAll();
	}

}
