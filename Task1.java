package com.zeebe.workers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.spring.client.EnableZeebeClient;
import io.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@EnableZeebeClient
@Log4j2
public class Task1 {
    @Autowired
    private ZeebeClient client;
    public static void main(final String[] args) {

        SpringApplication.run(Task1.class, args);
    }
    @ZeebeWorker(type = "mkodnanitask1")
    public void handler(final JobClient client, final ActivatedJob job) throws InterruptedException {
        // Fetch input variables as Map
        final Map<String, Object> inputParams = job.getVariablesAsMap();
        final String keywords = (String) inputParams.getOrDefault("keywords", "");
        // Complete the job.
        client.newCompleteCommand(job.getKey()).variables(keywords).send().join();
    }
}
