package br.com.smktbatch.service.job;

import br.com.smktbatch.model.remote.Job;

public interface JobService {
	
	Job createOrUpdateJob(Job job);

}