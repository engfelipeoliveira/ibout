package br.com.smktbatch.service.mapping;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import br.com.smktbatch.model.remote.Mapping;
import br.com.smktbatch.repository.remote.MappingRepository;

public class MappingServiceImplTest {

	private final MappingRepository mockMappingRepository = mock(MappingRepository.class);
	private final Mapping mockMapping = mock(Mapping.class);
	private final MappingServiceImpl underTest = new MappingServiceImpl(this.mockMappingRepository);

	@Test
	public void whenGetByClientConde_givenAToken_thenReturnMapping() {
		willReturn(mockMapping).given(mockMappingRepository).findByIdClient(1L);
		
		Mapping mapping = underTest.getByIdClient(1L);
		
		verify(mockMappingRepository).findByIdClient(1L);
		assertThat(mapping).isEqualTo(mockMapping);
	}

}
