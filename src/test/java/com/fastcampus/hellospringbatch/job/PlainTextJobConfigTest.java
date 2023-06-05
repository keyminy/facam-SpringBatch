package com.fastcampus.hellospringbatch.job;


import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
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
import com.fastcampus.hellospringbatch.core.domain.PlainText;
import com.fastcampus.hellospringbatch.core.repository.PlainTextRepository;
import com.fastcampus.hellospringbatch.core.repository.ResultTextRepository;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class) //Spring환경에서 테스트 할 수 있게
@ActiveProfiles("test") // test yaml테스트 가능하게
@ContextConfiguration(classes = {PlainTextJobConfig.class,BatchTestConfig.class}) // test시, 활성화 시켜줄 설정
public class PlainTextJobConfigTest {
	
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	@Autowired
	private PlainTextRepository plainTextRepository;
	@Autowired
	private ResultTextRepository resultTextRepository;	
	
	//사용한 repository에 대해서는 데이터를 clean up
	//데이터가 생성됬거나 수정됬거나 이런거 다 삭제해버림..
	@AfterEach //각각의 테스트 메소드가 수행된 다음에 수행됨
	public void tearDown() {
		plainTextRepository.deleteAll();
		resultTextRepository.deleteAll();
	}
	
	//주어진 plain_text가 없을때 success함
	@Test
	public void success_givenNoPlainText() throws Exception {
		//given
		//no plaintext
		
		//when
		JobExecution execution = jobLauncherTestUtils.launchJob();
		
		//then
		Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
		//resultTextRepository에 데이터 0개
		Assertions.assertEquals(resultTextRepository.count(), 0);
		
	}
	
	//주어진 plain_text가 있을 때 success함
	@Test
	public void success_givenPlainText() throws Exception {
		//given
		givenPlainTexts(12);
		
		//when
		JobExecution execution = jobLauncherTestUtils.launchJob();
		
		//then
		Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
		//resultTextRepository에 데이터 12개
		Assertions.assertEquals(resultTextRepository.count(),12);
		
	}
	
	private void givenPlainTexts(Integer count) {
		//count만큼 plain_text(읽을 데이터가 존재)
		IntStream.range(0, count)
		.forEach(num -> plainTextRepository.save(new PlainText(null,"text" + num)));
	}
}
