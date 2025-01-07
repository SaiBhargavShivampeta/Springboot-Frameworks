// Improved MyReader.java
package com.example.demo;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class MyReader extends FlatFileItemReader<User> {
    
    public MyReader() {
        setName("userItemReader");
        setResource(new FileSystemResource("C:\\Users\\SAI BHARGHAV\\Downloads\\javaproject\\javaproject\\demo\\src\\main\\resources\\input.csv"));
        setLinesToSkip(1); // Skip the header row
        
        // Set up the line mapper
        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();
        
        // Configure how to split the CSV line
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("id", "name", "email");
        
        // Configure how to create a User from the tokens
        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);
        
        // Wire the components together
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        
        setLineMapper(lineMapper);
    }
}