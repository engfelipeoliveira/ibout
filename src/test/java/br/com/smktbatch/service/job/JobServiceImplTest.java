package br.com.smktbatch.service.job;

import br.com.smktbatch.model.remote.Job;
import br.com.smktbatch.repository.remote.JobRepository;

import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class JobServiceImplTest {

	private final JobRepository mockJobRepository = mock(JobRepository.class);
	private final Job mockJob = mock(Job.class);
	private final JobServiceImpl underTest = new JobServiceImpl(this.mockJobRepository);

	@Test
	public void whenCreateOrUpdate_givenMockJob_thenSaveJob() {
		willReturn(mockJob).given(mockJobRepository).save(mockJob);
		
		Job jobSaved = underTest.createOrUpdate(mockJob);
		
		verify(mockJobRepository).save(mockJob);
		assertThat(jobSaved).isEqualTo(mockJob);
	}

}
