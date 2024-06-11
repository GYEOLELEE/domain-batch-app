package com.fnf.domainbatchapp.head.config;

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
public class HeadJobConfiguration {

    private final JobRepository jobRepository;

    @Bean
    public Job headOrderToECJob() {
        return new JobBuilder("HeadOrderToECJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(headOrderToECStep(null))
                .build();
    }

    @Bean
    public Step headOrderToECStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("HeadOrderToECStep", jobRepository)
                .tasklet(headOrderToECTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet headOrderToECTasklet() {
        return (contribution, chunkContext) -> {
            String toDate = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("toDate");

            String fromDate = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("fromDate");
            log.info("자사몰 주문수집 headOrderToECTasklet - 기간: {} ~ {}", toDate, fromDate);
            return RepeatStatus.FINISHED;
        };
    }


    @Bean
    public Job headSkuMappingJob() {
        return new JobBuilder("HeadSkuMappingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(headSkuMappingStep(null))
                .build();
    }

    @Bean
    public Step headSkuMappingStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("HeadSkuMappingStep", jobRepository)
                .tasklet(headSkuMappingTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet headSkuMappingTasklet() {
        return (contribution, chunkContext) -> {

            String second = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("second");
            int secondInt = ObjectUtils.isEmpty(second) ? 1 : Integer.parseInt(second);
            log.info("second, {}!", secondInt);
            Thread.sleep(1_000L * secondInt);

            log.info("자사몰 스큐매핑 headSkuMappingTasklet : second, {}", second);
            return RepeatStatus.FINISHED;
        };
    }
    @Bean
    public Job headStockJob() {
        return new JobBuilder("HeadStockJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(headStockStep(null))
                .build();
    }

    @Bean
    public Step headStockStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("HeadStockStep", jobRepository)
                .tasklet(headStockTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet headStockTasklet() {
        return (contribution, chunkContext) -> {

            String second = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("second");
            int secondInt = ObjectUtils.isEmpty(second) ? 1 : Integer.parseInt(second);
            log.info("second, {}!", secondInt);
            Thread.sleep(1_000L * secondInt);

            log.info("자사몰 재고전송 headStockTasklet : second, {}", second);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Job headCancelJob() {
        return new JobBuilder("HeadCancelJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(headCancelStep(null))
                .build();
    }

    @Bean
    public Step headCancelStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("HeadCancelStep", jobRepository)
                .tasklet(headCancelTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet headCancelTasklet() {
        return (contribution, chunkContext) -> {

            String second = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("second");
            int secondInt = ObjectUtils.isEmpty(second) ? 1 : Integer.parseInt(second);
            log.info("second, {}!", secondInt);
            Thread.sleep(1_000L * secondInt);

            log.info("자사몰 취소수집 headCancelTasklet : second, {}", second);
            return RepeatStatus.FINISHED;
        };
    }
}