package com.fnf.domainbatchapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.util.ObjectUtils;

@Slf4j
@EnableTask
@SpringBootApplication
public class DomainBatchAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(DomainBatchAppApplication.class, args);
    }


}
