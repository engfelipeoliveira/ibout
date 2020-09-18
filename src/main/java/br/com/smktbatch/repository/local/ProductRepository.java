package br.com.smktbatch.repository.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.smktbatch.model.local.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	
}
