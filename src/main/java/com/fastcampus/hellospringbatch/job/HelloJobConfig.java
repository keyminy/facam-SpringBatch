package com.fastcampus.hellospringbatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class HelloJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	@Bean("helloJob")
	public Job helloJob(Step helloStep) {
		return jobBuilderFactory.get("helloJob")
				.incrementer(new RunIdIncrementer())
				.start(helloStep)
				.build();
	}
	
	@JobScope //Job이 실행되는 동안에만, Step 빈의 메모리가 떠잇게함!..
	@Bean("helloStep")
	public Step helloStep(Tasklet tasklet) {
		return stepBuilderFactory.get("helloStep")
				.tasklet(tasklet)
				.build();
	}
	
	@StepScope //Step이 실행되는 동안에만, Tasklet 빈이 메모리에 떠잇게함
	@Bean
	public Tasklet tasklet() {
		return (contribution, chunkContext) -> {
			System.out.println("Hello Spring Batch");
			return RepeatStatus.FINISHED;
		};
	}
	
}
