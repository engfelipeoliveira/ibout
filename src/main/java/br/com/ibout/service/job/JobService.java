package br.com.ibout.service.job;

import br.com.ibout.model.remote.Job;

public interface JobService {
	
	Job createOrUpdate(Job job);

}
