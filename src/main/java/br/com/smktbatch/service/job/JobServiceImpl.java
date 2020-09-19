package br.com.smktbatch.service.job;

import org.springframework.stereotype.Service;

import br.com.smktbatch.model.remote.Job;
import br.com.smktbatch.repository.remote.JobRepository;

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
