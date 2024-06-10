package com.fnf.domainbatchapp;

import lombok.RequiredArgsConstructor;
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
import org.springframework.util.ObjectUtils;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobConfiguration {

    private final JobRepository jobRepository;

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

    @Bean
    public Job hourJob() {
        return new JobBuilder("HourJob", jobRepository)
                .incrementer(new CustomJobParametersIncrementer())
                .start(hourStep(null))
                .build();
    }

    @Bean
    public Step hourStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("HourStep", jobRepository)
                .tasklet(hourTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet hourTasklet() {
        return (contribution, chunkContext) -> {
            log.info("hour!");
            Thread.sleep(1000 * 60 * 60);
            return RepeatStatus.FINISHED;
        };
    }


    @Bean
    public Job timeJob() {
        return new JobBuilder("TimeJob", jobRepository)
                .incrementer(new CustomJobParametersIncrementer())
                .start(timeStep(null))
                .build();
    }

    @Bean
    public Step timeStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("TimeStep", jobRepository)
                .tasklet(timeTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet timeTasklet() {
        return (contribution, chunkContext) -> {
            String second = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("second");
            int secondInt = ObjectUtils.isEmpty(second) ? 1 : Integer.parseInt(second);
            log.info("second, {}!", secondInt);
            Thread.sleep(1000L * secondInt);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Job zeroIsFailJob() {
        return new JobBuilder("ZeroIsFailJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(zeroIsFailStep(null))
                .build();
    }

    @Bean
    public Step zeroIsFailStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("ZeroIsFailStep", jobRepository)
                .tasklet(zeroIsFailTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet zeroIsFailTasklet() {
        return (contribution, chunkContext) -> {
            String number = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("number");

            boolean result = !(ObjectUtils.isEmpty(number) || "0".equals(number));
            log.info("is Fail?, {}!", result);

            if (!result) {
                throw new IllegalArgumentException("0 is not allowed");
            }

            return RepeatStatus.FINISHED;
        };
    }
}