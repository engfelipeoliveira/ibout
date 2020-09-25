package br.com.smktbatch.service.parameter;

import static br.com.smktbatch.enums.DataSource.CSV;
import static br.com.smktbatch.enums.DataSource.DB;
import static br.com.smktbatch.enums.DataSource.INVALIDO;
import static br.com.smktbatch.enums.DataSource.TXT;
import static br.com.smktbatch.enums.DataSource.XLS;
import static com.google.common.truth.Truth.assertThat;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.delete;
import static java.nio.file.Paths.get;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.nio.file.Path;
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
		willReturn(mockParameter).given(mockParameterRepository).findByIdClient(1L);
		
		Parameter parameter = underTest.getByIdClient(1L);
		
		verify(mockParameterRepository).findByIdClient(1L);
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
		Parameter parameter = Parameter.builder().active(false).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.status.job.inactive");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterJobHourNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.hourjob.null");
		Parameter parameter = Parameter.builder().hourJob(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.hourjob.null");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterJobHourGreaterThan23_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.hourjob.not.between.0.and.23");
		Parameter parameter = Parameter.builder().hourJob("25").build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.hourjob.not.between.0.and.23");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterJobHourSmallerThan0_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.hourjob.not.between.0.and.23");
		Parameter parameter = Parameter.builder().hourJob("-2").build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.hourjob.not.between.0.and.23");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.datasource.null");
		Parameter parameter = Parameter.builder().dataSource(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.datasource.null");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceInvalid_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.datasource.invalid");
		Parameter parameter = Parameter.builder().dataSource(INVALIDO).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.datasource.invalid");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceTxtAndFileDelimiterNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.delimiter.file.null");
		Parameter parameter = Parameter.builder().dataSource(TXT).fileDelimiter(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.delimiter.file.null");
		assertThat(listErrors).contains("msg");
	}	
	
	@Test
	public void whenValidate_givenParameterDataSourceTxtAndDirSourceNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.null");
		Parameter parameter = Parameter.builder().dataSource(TXT).dirSource(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.null");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceCsvAndDirSourceNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.null");
		Parameter parameter = Parameter.builder().dataSource(CSV).dirSource(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.null");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceXlsAndDirSourceNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.null");
		Parameter parameter = Parameter.builder().dataSource(XLS).dirSource(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.null");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceTxtAndDirSourceNotExistsNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.invalid");
		Parameter parameter = Parameter.builder().dataSource(TXT).dirSource("c:\\invalid_dir_not_exists").build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.invalid");
		assertThat(listErrors).contains("msg");
	}	
	
	@Test
	public void whenValidate_givenParameterDataSourceCsvAndDirSourceNotExistsNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.invalid");
		Parameter parameter = Parameter.builder().dataSource(CSV).dirSource("c:\\invalid_dir_not_exists").build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.invalid");
		assertThat(listErrors).contains("msg");
	}	
	
	@Test
	public void whenValidate_givenParameterDataSourceXlsAndDirSourceNotExistsNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.invalid");
		Parameter parameter = Parameter.builder().dataSource(XLS).dirSource("invalid_dir_not_exists").build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.invalid");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceTxtAndDirSourceIsEmpty_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.empty");
		Path path = get("temp_junit_dir_source");
		createDirectories(path);
		Parameter parameter = Parameter.builder().dataSource(TXT).dirSource("temp_junit_dir_source").build();
		List<String> listErrors = underTest.validate(parameter);
		delete(path);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.empty");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceCsvAndDirSourceIsEmpty_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.empty");
		Path path = get("temp_junit_dir_source");
		createDirectories(path);
		Parameter parameter = Parameter.builder().dataSource(CSV).dirSource("temp_junit_dir_source").build();
		List<String> listErrors = underTest.validate(parameter);
		delete(path);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.empty");
		assertThat(listErrors).contains("msg");
	}	
	
	@Test
	public void whenValidate_givenParameterDataSourceXlsAndDirSourceIsEmpty_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.empty");
		Path path = get("temp_junit_dir_source");
		createDirectories(path);
		Parameter parameter = Parameter.builder().dataSource(XLS).dirSource("temp_junit_dir_source").build();
		List<String> listErrors = underTest.validate(parameter);
		delete(path);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.empty");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceTxtAndDirTargetNotExistsNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.target.invalid");
		Parameter parameter = Parameter.builder().dataSource(TXT).moveFileAfterRead(true).dirTarget("invalid_dir_not_exists").build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.target.invalid");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceCsvAndDirTargetNotExistsNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.target.invalid");
		Parameter parameter = Parameter.builder().dataSource(CSV).moveFileAfterRead(true).dirTarget("invalid_dir_not_exists").build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.target.invalid");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceXlsAndDirTargetNotExistsNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.target.invalid");
		Parameter parameter = Parameter.builder().dataSource(XLS).moveFileAfterRead(true).dirTarget("invalid_dir_not_exists").build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.target.invalid");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceTxtAndDirTargetEqualsDirSource_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.target.equals");
		Parameter parameter = Parameter.builder().dataSource(TXT).moveFileAfterRead(true).dirTarget("dir_source_equals_target").dirSource("dir_source_equals_target").build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.target.equals");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceCsvAndDirTargetEqualsDirSource_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.target.equals");
		Parameter parameter = Parameter.builder().dataSource(CSV).moveFileAfterRead(true).dirTarget("dir_source_equals_target").dirSource("dir_source_equals_target").build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.target.equals");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceXlsAndDirTargetEqualsDirSource_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.directory.source.target.equals");
		Parameter parameter = Parameter.builder().dataSource(XLS).moveFileAfterRead(true).dirTarget("dir_source_equals_target").dirSource("dir_source_equals_target").build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.directory.source.target.equals");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceDbAndSgbdIsNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.database.invalid");
		Parameter parameter = Parameter.builder().dataSource(DB).sgbd(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.database.invalid");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceDbAndDbUrlIsNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.database.invalid");
		Parameter parameter = Parameter.builder().dataSource(DB).bdUrl(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.database.invalid");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceDbAndDbDriverIsNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.database.invalid");
		Parameter parameter = Parameter.builder().dataSource(DB).bdDriver(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.database.invalid");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceDbAndDbUserIsNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.database.invalid");
		Parameter parameter = Parameter.builder().dataSource(DB).bdUser(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.database.invalid");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceDbAndDbPassIsNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.database.invalid");
		Parameter parameter = Parameter.builder().dataSource(DB).bdPass(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.database.invalid");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterDataSourceDbAndDbSqlIsNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.database.invalid");
		Parameter parameter = Parameter.builder().dataSource(DB).bdSql(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.database.invalid");
		assertThat(listErrors).contains("msg");
	}
	
	@Test
	public void whenValidate_givenParameterApiSizeArrayInsertProductNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.api.size.array.insert.products.invalid");
		Parameter parameter = Parameter.builder().apiSizeArrayInsertProduct(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.api.size.array.insert.products.invalid");
		assertThat(listErrors).contains("msg");
	}	
	
	@Test
	public void whenValidate_givenParameterApiUrlInsertProductSizeNull_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.api.url.insert.products.null");
		Parameter parameter = Parameter.builder().apiUrlInsertProduct(null).build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.api.url.insert.products.null");
		assertThat(listErrors).contains("msg");
	}	
	
	@Test
	public void whenValidate_givenParameterApiUrlInsertProductSizeNotNumeric_thenReturnListError() throws Exception {
		willReturn("msg").given(mockMessageService).getByCode("msg.error.validation.api.size.array.insert.products.invalid");
		Parameter parameter = Parameter.builder().apiUrlInsertProduct("X").build();
		List<String> listErrors = underTest.validate(parameter);
		
		verify(mockMessageService).getByCode("msg.error.validation.api.size.array.insert.products.invalid");
		assertThat(listErrors).contains("msg");
	}	
	
	@Test
	public void whenValidate_givenParameterFileValid_thenReturnListEmpty() throws Exception {
		Path source = get("dir_source_test_unit");
		Path subDir = get("dir_source_test_unit/1");
		Path target = get("dir_target_test_unit");
		createDirectories(source);
		createDirectories(subDir);
		createDirectories(target);
		Parameter parameter = Parameter.builder()
				.active(true)
				.hourJob("15,20")
				.dataSource(TXT)
				.fileDelimiter(";")
				.dirSource("dir_source_test_unit")
				.dirTarget("dir_target_test_unit")
				.apiUrlInsertProduct("apiUrlInsertProduct")
				.apiSizeArrayInsertProduct("100")
				.build();
		List<String> listErrors = underTest.validate(parameter);
		delete(subDir);
		delete(source);
		delete(target);
		
		assertThat(listErrors).isEmpty();
	}
	
	@Test
	public void whenValidate_givenParameterDbValid_thenReturnListEmpty() throws Exception {
		Parameter parameter = Parameter.builder()
				.active(true)
				.hourJob("15,20")
				.dataSource(DB)
				.bdDriver("driver")
				.bdPass("pass")
				.bdUser("user")
				.bdSql("sql")
				.bdUrl("url")
				.sgbd("sgbd")
				.apiUrlInsertProduct("apiUrlInsertProduct")
				.apiSizeArrayInsertProduct("1")
				.build();
		List<String> listErrors = underTest.validate(parameter);
		
		assertThat(listErrors).isEmpty();
	}
}
