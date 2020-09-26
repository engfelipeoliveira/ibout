package br.com.ibout.repository.remote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ibout.model.remote.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
}
