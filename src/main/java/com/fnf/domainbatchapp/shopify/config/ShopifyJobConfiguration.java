package com.fnf.domainbatchapp.shopify.config;

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
public class ShopifyJobConfiguration {

    private final JobRepository jobRepository;

    @Bean
    public Job shopifyOrderToECJob() {
        return new JobBuilder("ShopifyOrderToECJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(shopifyOrderToECStep(null))
                .build();
    }

    @Bean
    public Step shopifyOrderToECStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("ShopifyOrderToECStep", jobRepository)
                .tasklet(shopifyOrderToECTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet shopifyOrderToECTasklet() {
        return (contribution, chunkContext) -> {
            String toDate = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("toDate");

            String fromDate = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("fromDate");

            log.info("쇼피파이 주문수집 shopifyOrderToECTasklet - 기간 : {} ~ {}", toDate, fromDate);
            return RepeatStatus.FINISHED;
        };
    }


    @Bean
    public Job shopifySkuMappingJob() {
        return new JobBuilder("ShopifySkuMappingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(shopifySkuMappingStep(null))
                .build();
    }

    @Bean
    public Step shopifySkuMappingStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("ShopifySkuMappingStep", jobRepository)
                .tasklet(shopifySkuMappingTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet shopifySkuMappingTasklet() {
        return (contribution, chunkContext) -> {

            String second = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("second");
            int secondInt = ObjectUtils.isEmpty(second) ? 1 : Integer.parseInt(second);
            log.info("second, {}!", secondInt);
            Thread.sleep(1_000L * secondInt);

            log.info("쇼피파이 스큐매핑 shopifySkuMappingTasklet : second, {}", second);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Job shopifyStockJob() {
        return new JobBuilder("ShopifyStockJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(shopifyStockStep(null))
                .build();
    }

    @Bean
    public Step shopifyStockStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("ShopifyStockStep", jobRepository)
                .tasklet(shopifyStockTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet shopifyStockTasklet() {
        return (contribution, chunkContext) -> {

            String second = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("second");
            int secondInt = ObjectUtils.isEmpty(second) ? 1 : Integer.parseInt(second);
            log.info("second, {}!", secondInt);
            Thread.sleep(1_000L * secondInt);

            log.info("쇼피파이 재고전송 shopiStockTasklet : second, {}", second);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Job shopifyCancelJob() {
        return new JobBuilder("ShopifyCancelJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(shopifyCancelStep(null))
                .build();
    }

    @Bean
    public Step shopifyCancelStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("ShopifyCancelStep", jobRepository)
                .tasklet(shopifyCancelTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet shopifyCancelTasklet() {
        return (contribution, chunkContext) -> {

            String second = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("second");
            int secondInt = ObjectUtils.isEmpty(second) ? 1 : Integer.parseInt(second);
            log.info("second, {}!", secondInt);
            Thread.sleep(1_000L * secondInt);

            log.info("쇼피파이 취소수집 shopifyCancelTasklet : second, {}", second);
            return RepeatStatus.FINISHED;
        };
    }
}