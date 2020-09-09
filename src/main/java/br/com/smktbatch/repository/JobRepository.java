package br.com.smktbatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.smktbatch.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
}
