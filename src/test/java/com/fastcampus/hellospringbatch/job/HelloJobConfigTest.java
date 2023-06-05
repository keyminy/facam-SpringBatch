package com.fastcampus.hellospringbatch.job;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fastcampus.hellospringbatch.BatchTestConfig;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class) //Spring환경에서 테스트 할 수 있게
@ActiveProfiles("test") // test yaml테스트 가능하게
@ContextConfiguration(classes = {HelloJobConfig.class,BatchTestConfig.class}) // test시, 활성화 시켜줄 설정
public class HelloJobConfigTest {
	
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	
	@Test
	public void success() throws Exception {
		//import org.springframework.batch.core.JobExecution;
		JobExecution execution = jobLauncherTestUtils.launchJob();
		Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
	}
}
