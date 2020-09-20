package br.com.smktbatch.service.parameter;

import static br.com.smktbatch.enums.DataSource.CSV;
import static br.com.smktbatch.enums.DataSource.INVALIDO;
import static br.com.smktbatch.enums.DataSource.TXT;
import static br.com.smktbatch.enums.DataSource.XLS;
import static br.com.smktbatch.model.remote.Parameter.builder;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

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
	
	@Test
	public void whenValidate_givenNullParameter_thenReturnEmptyListError() throws Exception {
		List<String> listErrors = underTest.validate(null);
		
		assertThat(listErrors).isEmpty();
	}
	
	@Test
	public void whenValidate_givenParameterActiveFalse_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.status.job.inactive");
		Parameter parameter = builder().active(false).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.status.job.inactive");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterJobHourNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.hourjob.null");
		Parameter parameter = builder().hourJob(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.hourjob.null");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterJobHourGreaterThan23_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.hourjob.not.between.0.and.23");
		Parameter parameter = builder().hourJob("25").build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.hourjob.not.between.0.and.23");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterJobHourSmallerThan0_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.hourjob.not.between.0.and.23");
		Parameter parameter = builder().hourJob("-2").build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.hourjob.not.between.0.and.23");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.datasource.null");
		Parameter parameter = builder().dataSource(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.datasource.null");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceInvalid_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.datasource.invalid");
		Parameter parameter = builder().dataSource(INVALIDO).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.datasource.invalid");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceTxtAndFileDelimiterNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.delimiter.file.null");
		Parameter parameter = builder().dataSource(TXT).fileDelimiter(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.delimiter.file.null");
		assertThat(listErrors).contains("msg");
	}	
	
	@Test
	public void whenValidate_givenParameterDataSourceTxtAndDirSourceNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.null");
		Parameter parameter = builder().dataSource(TXT).dirSource(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.null");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceCsvAndDirSourceNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.null");
		Parameter parameter = builder().dataSource(CSV).dirSource(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.null");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceXlsAndDirSourceNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.null");
		Parameter parameter = builder().dataSource(XLS).dirSource(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.null");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceTxtAndDirSourceNotExistsNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.invalid");
		Parameter parameter = builder().dataSource(TXT).dirSource("c:\\invalid_dir_not_exists").fileDelimiter(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.invalid");
		assertThat(listErrors).contains("msg");
	}	
	
	@Test
	public void whenValidate_givenParameterDataSourceCsvAndDirSourceNotExistsNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.invalid");
		Parameter parameter = builder().dataSource(CSV).dirSource("c:\\invalid_dir_not_exists").fileDelimiter(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.invalid");
		assertThat(listErrors).contains("msg");
	}	
	
	@Test
	public void whenValidate_givenParameterDataSourceXlsAndDirSourceNotExistsNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.invalid");
		Parameter parameter = builder().dataSource(XLS).dirSource("c:\\invalid_dir_not_exists").fileDelimiter(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.invalid");
		assertThat(listErrors).contains("msg");
	}
}
