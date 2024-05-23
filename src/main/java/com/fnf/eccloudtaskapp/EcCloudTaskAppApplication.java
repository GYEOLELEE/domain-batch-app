package com.fnf.eccloudtaskapp;

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
public class EcCloudTaskAppApplication implements CommandLineRunner {

	@Autowired
	private JobRegistry jobRegistry;

	@Autowired
	private JobLauncher jobLauncher;

	@Value("${job.name:#{null}}")
	private String jobName;

	@Value("${name:#{null}}")
	private String name;

	public static void main(String[] args) {
		SpringApplication.run(EcCloudTaskAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Job name: {}", jobName);
		log.info("Name: {}", name);

		if (ObjectUtils.isEmpty(jobName)) {
			log.error("Job name is empty.");
			return ;
		}

		Job job = jobRegistry.getJob(jobName);
		if (ObjectUtils.isEmpty(job)) {
			log.error("Job '{}' not found.", jobName);
			return ;
		}

		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();

		if(name != null) {
			jobParametersBuilder.addString("name", name);
		}

		jobLauncher.run(job, jobParametersBuilder.toJobParameters());
	}

}
