package com.example.ytspringbatch.batch;

import com.example.ytspringbatch.entity.WinEntity;
import com.example.ytspringbatch.repository.WinRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.Map;

// 조건을 통해 읽어온 데이터를 처리하는 배치
@Configuration
public class SecondBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final WinRepository winRepository;

    public SecondBatch(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, WinRepository winRepository) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.winRepository = winRepository;
    }

    @Bean
    public Job secondJob() {
        return new JobBuilder("secondJob", jobRepository)
                .start(secondStep())
                .build();
    }

    @Bean
    public Step secondStep() {
        return new StepBuilder("secondStep", jobRepository)
                .<WinEntity, WinEntity> chunk(10, platformTransactionManager)
                .reader(winReader())
                .processor(trueProcessor())
                .writer(winWriter())
                .build();
    }

    // 어떻게 읽어올지 설정
    @Bean
    public RepositoryItemReader<WinEntity> winReader() {
        return new RepositoryItemReaderBuilder<WinEntity>()
                .name("winReader")
                .pageSize(10)
                .methodName("findByWinGreaterThanEqual")
                .arguments(Collections.singletonList(10L)) // 10 이상인 것만 읽어옴
                .repository(winRepository)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    // 읽어온 데이터를 처리
    @Bean
    public ItemProcessor<WinEntity, WinEntity> trueProcessor() {
        return item -> {
            item.setReward(true); // reward를 true로 변경
            return item;
        };
    }

    // 처리한 데이터를 저장
    @Bean
    public RepositoryItemWriter<WinEntity> winWriter() {
        return new RepositoryItemWriterBuilder<WinEntity>()
                .repository(winRepository)
                .methodName("save")
                .build();
    }

}