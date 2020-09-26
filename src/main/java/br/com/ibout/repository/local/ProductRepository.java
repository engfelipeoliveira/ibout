package br.com.ibout.repository.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ibout.model.local.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {	
}