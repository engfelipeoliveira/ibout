package br.com.smktbatch.service.message;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import br.com.smktbatch.model.remote.MessageJob;
import br.com.smktbatch.repository.remote.MessageRepository;

public class MessageServiceImplTest {

	private final MessageRepository mockMessageRepository = mock(MessageRepository.class);
	private final MessageJob mockMessage = mock(MessageJob.class);
	private final MessageServiceImpl underTest = new MessageServiceImpl(this.mockMessageRepository);

	@Test
	public void whenGetByCode_givenACode_thenReturnDescription() {
		willReturn("desc").given(mockMessage).getDescription();
		willReturn(mockMessage).given(mockMessageRepository).findByCode("code");
		
		String desc = underTest.getByCode("code");
		
		verify(mockMessageRepository).findByCode("code");
		assertThat(desc).isEqualTo("desc");
	}

}
