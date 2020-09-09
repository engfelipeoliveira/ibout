package br.com.smktbatch.service;

import br.com.smktbatch.model.Job;

public interface JobService {
	
	Job createOrUpdateJob(Job job);

}
