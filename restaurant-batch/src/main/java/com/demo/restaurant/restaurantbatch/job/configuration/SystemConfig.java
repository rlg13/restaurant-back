package com.demo.restaurant.restaurantbatch.job.configuration;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@Import({  DeliveryJobConfig.class })
@EnableBatchProcessing
@EnableScheduling
public class SystemConfig {

	private JobLauncher jobLauncher;
	
	private Job deliveryJob;
	
	@Autowired
	public SystemConfig(JobLauncher jobLauncher,
						Job deliveryJob) {
		this.jobLauncher = jobLauncher;
		this.deliveryJob = deliveryJob;
	}

	@Scheduled(cron = "0/10 * * ? * *")//10 seg	
	//@Scheduled(cron = "0 0 11 ? * *")//Todos los dias a las 11
	private void executeProcessDelivered() throws Exception {
		jobLauncher.run(deliveryJob, generateJobParameters());
	}

	private JobParameters generateJobParameters() {
		return new JobParametersBuilder().addDate("date", new Date()).toJobParameters();
	}

}
