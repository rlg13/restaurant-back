package com.demo.restaurant.restaurantbatch.Job.Configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.demo.restaurant.restaurantbatch.Job.Tasklet.TaskletProcess;

public class DeliveryJobConfig {
	private JobBuilderFactory jobBuilderFactory;
	
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DeliveryJobConfig(JobBuilderFactory jobBuilderFactory,
						   StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}
	
	@Bean
	private Step deliveryStep(TaskletProcess taskletProcess) {
		return stepBuilderFactory
			   .get("Delivery Step")
			   .tasklet(taskletProcess)
			   .build();
	}

	@Bean
	private Job unziperJob(Step deliveryStep) {
		return jobBuilderFactory
			   .get("Delivery Job")
			   .incrementer(new RunIdIncrementer())
			   .flow(deliveryStep)
			   .end()
			   .build();
	}

}
