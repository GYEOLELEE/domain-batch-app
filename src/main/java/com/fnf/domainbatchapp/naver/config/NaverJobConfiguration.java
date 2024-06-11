package com.fnf.domainbatchapp.naver.config;

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
public class NaverJobConfiguration {

    private final JobRepository jobRepository;

    @Bean
    public Job naverCommerceOrderToECJob() {
        return new JobBuilder("NaverCommerceOrderToECJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(naverCommerceOrderToECStep(null))
                .build();
    }

    @Bean
    public Step naverCommerceOrderToECStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("NaverCommerceOrderToECStep", jobRepository)
                .tasklet(naverCommerceOrderToECTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet naverCommerceOrderToECTasklet() {
        return (contribution, chunkContext) -> {
            String toDate = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("toDate");

            String fromDate = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("fromDate");
            log.info("네이버 주문수집 naverCommerceOrderToECTasklet - 기간: {} ~ {}", toDate, fromDate);
            return RepeatStatus.FINISHED;
        };
    }


    @Bean
    public Job naverCommerceSkuMappingJob() {
        return new JobBuilder("NaverCommerceSkuMappingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(naverCommerceSkuMappingStep(null))
                .build();
    }

    @Bean
    public Step naverCommerceSkuMappingStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("NaverCommerceSkuMappingStep", jobRepository)
                .tasklet(naverCommerceSkuMappingTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet naverCommerceSkuMappingTasklet() {
        return (contribution, chunkContext) -> {

            String second = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("second");
            int secondInt = ObjectUtils.isEmpty(second) ? 1 : Integer.parseInt(second);
            log.info("second, {}!", secondInt);
            Thread.sleep(1_000L * secondInt);

            log.info("네이버 스큐매핑 naverCommerceSkuMappingTasklet : second, {}", second);
            return RepeatStatus.FINISHED;
        };
    }
    @Bean
    public Job naverCommerceStockJob() {
        return new JobBuilder("NaverCommerceStockJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(naverCommerceStockStep(null))
                .build();
    }

    @Bean
    public Step naverCommerceStockStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("NaverCommerceStockStep", jobRepository)
                .tasklet(naverCommerceStockTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet naverCommerceStockTasklet() {
        return (contribution, chunkContext) -> {

            String second = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("second");
            int secondInt = ObjectUtils.isEmpty(second) ? 1 : Integer.parseInt(second);
            log.info("second, {}!", secondInt);
            Thread.sleep(1_000L * secondInt);

            log.info("네이버 재고전송 naverCommerceStockTasklet : second, {}", second);
            return RepeatStatus.FINISHED;
        };
    }
}