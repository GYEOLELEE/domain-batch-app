package com.fnf.domainbatchapp.ec.config;

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
public class ecJobConfiguration {

    private final JobRepository jobRepository;

    @Bean
    public Job unmapOrderMappingJob() {
        return new JobBuilder("UnmapOrderMappingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(unmapOrderMappingStep(null))
                .build();
    }

    @Bean
    public Step unmapOrderMappingStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("UnmapOrderMappingStep", jobRepository)
                .tasklet(unmapOrderMappingTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet unmapOrderMappingTasklet() {
        return (contribution, chunkContext) -> {
            String toDate = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("toDate");

            String fromDate = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("fromDate");

            log.info("unmapOrderMappingTasklet : {} ~ {}", toDate, fromDate);
            return RepeatStatus.FINISHED;
        };
    }


    @Bean
    public Job stockSkuCalcJob() {
        return new JobBuilder("StockSkuCalcJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(stockSkuCalcStep(null))
                .build();
    }

    @Bean
    public Step stockSkuCalcStep(@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return new StepBuilder("StockSkuCalcStep", jobRepository)
                .tasklet(stockSkuCalcTasklet(), txManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet stockSkuCalcTasklet() {
        return (contribution, chunkContext) -> {

            String second = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("second");
            int secondInt = ObjectUtils.isEmpty(second) ? 1 : Integer.parseInt(second);
            log.info("second, {}!", secondInt);
            Thread.sleep(1_000L * secondInt);

            log.info("stockSkuCalcTasklet : second, {}", second);
            return RepeatStatus.FINISHED;
        };
    }
}