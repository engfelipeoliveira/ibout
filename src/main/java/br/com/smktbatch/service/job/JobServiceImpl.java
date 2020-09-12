package br.com.smktbatch.service.job;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import br.com.smktbatch.model.Job;
import br.com.smktbatch.repository.JobRepository;

@Service
public class JobServiceImpl implements JobService {

	private final JobRepository jobRepository;

	private static final Logger LOG = getLogger(JobServiceImpl.class);

	JobServiceImpl(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Override
	public Job createOrUpdateJob(Job job) {
		LOG.info("createOrUpdateJob()");
		return jobRepository.save(job);
	}

}
