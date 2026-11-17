package com.noha.jobagent.portfolio.model;

public record DemoJob(
        String id,
        String title,
        String company,
        String location,
        String workArrangement,
        String employmentType,
        String summary
) {
}
