package br.com.smktbatch.service.product;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Test;

import br.com.smktbatch.model.local.Product;
import br.com.smktbatch.repository.local.ProductRepository;

public class ProductServiceImplTest {

	private final ProductRepository mockProductRepository = mock(ProductRepository.class);
	private final Product mockProduct = mock(Product.class);
	private final ProductServiceImpl underTest = new ProductServiceImpl(this.mockProductRepository);

	@Test
	public void whenCreateOrUpdate_givenMockProduct_thenSaveProduct() {
		willReturn(mockProduct).given(mockProductRepository).save(mockProduct);
		
		Product productSaved = underTest.createOrUpdate(mockProduct);
		
		verify(mockProductRepository).save(mockProduct);
		assertThat(productSaved).isEqualTo(mockProduct);
	}

	@Test
	public void whenGetAll_givenNoArgs_thenReturnListOfProducts() {
		willReturn(asList(mockProduct)).given(mockProductRepository).findAll();
		
		List<Product> products = underTest.getAll();
		
		verify(mockProductRepository).findAll();
		assertThat(products).containsExactlyElementsIn(asList(mockProduct));
	}

	@Test
	public void whenDeleteAll_givenNoArgs_thenDeleteAllProducts() {
		underTest.deleteAll();
		
		verify(mockProductRepository).deleteAll();
	}	

}
