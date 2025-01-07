package com.example.demo;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final String OUTPUT_FILE = "output.ack";

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            try {
                createAckFile();
                System.out.println("Acknowledgment file created: " + OUTPUT_FILE);
            } catch (IOException e) {
                System.err.println("Failed to create acknowledgment file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void createAckFile() throws IOException {
        // Ensure any previous file is deleted before writing a new one
        Files.deleteIfExists(Paths.get(OUTPUT_FILE));
        
        // Write the acknowledgment message to the file
        try (FileWriter writer = new FileWriter(OUTPUT_FILE)) {
            writer.write("Job completed successfully.");
        }
    }
}

