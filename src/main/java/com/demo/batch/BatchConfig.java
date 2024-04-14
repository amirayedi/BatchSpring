package com.demo.batch;


import com.demo.batch.step.step1;
import com.demo.batch.step.step2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfig {

    private final JobRepository jobRepository;
    private final step1 step1;
    private final step2 step2;


    public BatchConfig(JobRepository jobRepository, com.demo.batch.step.step1 step1, com.demo.batch.step.step2 step2) {
        this.jobRepository = jobRepository;
        this.step1 = step1;
        this.step2 = step2;
    }

    @Bean
    public Job runJob() {
        return new JobBuilder("importStudents", jobRepository)
                .start(step1.stepCsvImport())
                .next(step2.stepCsvRead())
                .build();

    }
}
