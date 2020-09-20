package br.com.smktbatch.service.parameter;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import br.com.smktbatch.model.remote.Parameter;
import br.com.smktbatch.repository.remote.ParameterRepository;
import br.com.smktbatch.service.message.MessageService;

public class ParameterServiceImplTest {

	private final ParameterRepository mockParameterRepository = mock(ParameterRepository.class);
	private final Parameter mockParameter = mock(Parameter.class);
	private final MessageService mockMessageService= mock(MessageService.class);
	private final ParameterServiceImpl underTest = new ParameterServiceImpl(this.mockParameterRepository, mockMessageService);

	@Test
	public void whenGetByClientToken_givenAToken_thenReturnParameter() throws Exception {
		willReturn(mockParameter).given(mockParameterRepository).findByClientToken("token");
		
		Parameter parameter = underTest.getByClientToken("token");
		
		verify(mockParameterRepository).findByClientToken("token");
		assertThat(parameter).isEqualTo(mockParameter);
	}
}
