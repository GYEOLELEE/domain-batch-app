package com.fnf.domainbatchapp.ec.incrementer;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * incrementer 구현
 */
public class MinuteJobTimestamper implements JobParametersIncrementer {
    static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-hhmm");

    @Override
    public JobParameters getNext(JobParameters jobParameters) {
        String id = format.format(new Date());

        return new JobParametersBuilder().addString("run.id", id).toJobParameters();
    }
}
