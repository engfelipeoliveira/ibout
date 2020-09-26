package br.com.ibout.service.job;

import org.springframework.stereotype.Service;

import br.com.ibout.model.remote.Job;
import br.com.ibout.repository.remote.JobRepository;

@Service
public class JobServiceImpl implements JobService {

	private final JobRepository jobRepository;

	JobServiceImpl(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Override
	public Job createOrUpdate(Job job) {
		return jobRepository.save(job);
	}

}
