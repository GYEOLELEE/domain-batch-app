package com.fnf.eccloudtaskapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class JobConfiguration {

    private final JobRepository jobRepository;

    public JobConfiguration(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Bean
    public Job sayHelloJob() {
        return new JobBuilder("SayHelloJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(sayHelloStep(null))
                .build();
    }

    @Bean
    public Step sayHelloStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("SayHelloStep", jobRepository)
                .tasklet(sayHelloTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet sayHelloTasklet() {
        return (contribution, chunkContext) -> {
            String name = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("name");
            log.info("Hello, {}!", name == null ? "Cloud Task" : name);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Job sayGoodByeJob() {
        return new JobBuilder("SayGoodByeJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(sayGoodByeStep(null))
                .build();
    }

    @Bean
    public Step sayGoodByeStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("SayGoodByeStep", jobRepository)
                .tasklet(sayGoodByeTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet sayGoodByeTasklet() {
        return (contribution, chunkContext) -> {
            String name = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("name");
            log.info("GoodBye, {}!", name == null ? "Cloud Task" : name);
            return RepeatStatus.FINISHED;
        };
    }
}