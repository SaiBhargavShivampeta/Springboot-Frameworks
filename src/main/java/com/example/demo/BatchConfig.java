package com.example.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.core.io.PathResource;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.core.JobExecutionListener;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class BatchConfig {

    private static final Logger logger = LogManager.getLogger(BatchConfig.class); // Create the logger

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobCompletionNotificationListener jobCompletionNotificationListener;

    @Bean
    public FlatFileItemReader<User> reader() {
        logger.info("Creating FlatFileItemReader...");
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("input.csv"));
        reader.setLinesToSkip(1); // Skip the header line
        reader.setLineMapper(new DefaultLineMapper<User>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("id", "name", "email");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
                setTargetType(User.class);
            }});
        }});
        logger.info("FlatFileItemReader created successfully.");
        return reader;
    }

    @Bean
    public ItemProcessor<User, User> processor() {
        logger.info("Creating ItemProcessor...");
        return user -> {
            user.setName(user.getName().toUpperCase());
            logger.debug("Processed user: {}", user);
            return user;
        };
    }

    @Bean
    public JdbcBatchItemWriter<User> writer() {
        logger.info("Creating JdbcBatchItemWriter...");
        JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        String sql = "INSERT INTO users (id, name, email) VALUES (:id, :name, :email)";
        writer.setSql(sql);
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        logger.info("JdbcBatchItemWriter created successfully.");
        return writer;
    }

    @Bean
    public Step step1() {
        logger.info("Creating Step 1...");
        return new StepBuilder("csv-step", jobRepository)
                .<User, User>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job importUserJob() {
        logger.info("Creating importUserJob...");
        return new JobBuilder("importUserJob", jobRepository)
                .start(step1())
                .listener(jobCompletionNotificationListener)
                .build();
    }
}
