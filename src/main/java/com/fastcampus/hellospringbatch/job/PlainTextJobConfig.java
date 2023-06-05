package com.fastcampus.hellospringbatch.job;

import java.util.Collections;

import java.util.List;



import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import com.fastcampus.hellospringbatch.core.domain.PlainText;
import com.fastcampus.hellospringbatch.core.domain.ResultText;
import com.fastcampus.hellospringbatch.core.repository.PlainTextRepository;
import com.fastcampus.hellospringbatch.core.repository.ResultTextRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PlainTextJobConfig {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final PlainTextRepository plainTextRepository;
	private final ResultTextRepository resultTextRepository;
	
	@Bean("plainTextJob")
	public Job plainTextJob(Step plainTextStep) {
		return jobBuilderFactory.get("plainTextJob")
				.incrementer(new RunIdIncrementer())
				.start(plainTextStep)
				.build();
	}
	
	@JobScope //Job이 실행되는 동안에만, Step 빈의 메모리가 떠잇게함!..
	@Bean("plainTextStep")
	public Step plainTextStep(ItemReader plainTextReader,
							ItemProcessor plainTextProcessor,
							ItemWriter plainTextWriter) {
		return stepBuilderFactory.get("plainTextStep")
				//input : PlainText타입, Output : String
				.<PlainText,String>chunk(5) // chunksize 5만큼 읽음
				.reader(plainTextReader)
				.processor(plainTextProcessor)
				.writer(plainTextWriter)
				.build();
	}
	
	//DB에서 데이터 읽음 : RepositoryItemReader를 통해서..
	@StepScope
	@Bean
	public RepositoryItemReader<PlainText> plainTextReader(){
		return new RepositoryItemReaderBuilder<PlainText>()
				.name("plainTextReader")
				.repository(plainTextRepository)
				.methodName("findBy") //Repository에서, findBy메소드 수행
				.pageSize(5) //pageSize : 읽게되는 commit interval을 의미
				.arguments(List.of()) //빈 list
				.sorts(Collections.singletonMap("id", Sort.Direction.DESC)) //id를 DESC방향으로 sort하여 가져옴
				.build()
				; 
	}
	
	//processor생성
	@StepScope
	@Bean
	public ItemProcessor<PlainText, String> plainTextProcessor(){
//		return new ItemProcessor<PlainText, String>() {
//			@Override
//			public String process(PlainText item) throws Exception {
//				return "processed " + item.getText();
//			}
//		};
		return item -> "processed " + item.getText();
	}
	
	@StepScope
	@Bean
	public ItemWriter<String> plainTextWriter(){
//		return new ItemWriter<String>() {
//			@Override
//			public void write(List<? extends String> items) throws Exception {
//				items.forEach(System.out::println);
//				System.out.println("========= chunk is finished");
//			}
//		};
		return items -> {
//			items.forEach(System.out::println);
			
			/*result_text에 저장하는 것으로 변경*/
			items.forEach(item -> resultTextRepository.save(new ResultText(null,item)));
			//하나의 chunk가 끝남
			System.out.println("========= chunk is finished");
		};
	}
}
