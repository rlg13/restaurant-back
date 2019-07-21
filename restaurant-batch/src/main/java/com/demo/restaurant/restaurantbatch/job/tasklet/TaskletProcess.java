package com.demo.restaurant.restaurantbatch.job.tasklet;

import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.restaurant.rest.api.controller.beans.OrderRest;
import com.demo.restaurant.rest.api.controller.beans.UserRest;
import com.demo.restaurant.restaurantbatch.job.exception.ProcessException;
import com.demo.restaurant.restaurantbatch.job.service.CallApiService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TaskletProcess implements Tasklet {

	@Autowired
	private CallApiService callApiService;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		log.debug("Executing job");

		try {
			UserRest sessionStablished = callApiService.stablishSession();

			List<OrderRest> orders = callApiService.getAllOrdersToServeByDay(sessionStablished.getSessionId());

			for (OrderRest order : orders) {
				log.info("Processing Order {}", order.getId());
				try {
					callApiService.processOrder(order, sessionStablished.getSessionId());
					log.info("Order {} processed", order.getId());
				} catch (ProcessException exp) {
					log.error(exp.getMessage(), exp);
				}
			}

			callApiService.endSession(sessionStablished);
		} catch (ProcessException exp) {
			log.error("Error Conection server", exp);
			throw new UnexpectedJobExecutionException(exp.getMessage(), exp);
		}

		return RepeatStatus.FINISHED;
	}

}
