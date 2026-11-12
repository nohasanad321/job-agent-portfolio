package com.noha.jobagent.portfolio;

import com.noha.jobagent.core.CoreConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CoreConfiguration.class)
public class JobAgentPortfolioApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobAgentPortfolioApplication.class, args);
    }
}
