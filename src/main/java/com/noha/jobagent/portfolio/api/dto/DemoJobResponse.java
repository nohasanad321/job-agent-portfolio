package com.noha.jobagent.portfolio.api.dto;

import com.noha.jobagent.portfolio.model.DemoJob;

public record DemoJobResponse(
        String id,
        String title,
        String company,
        String location,
        String workArrangement,
        String employmentType,
        String summary
) {

    public static DemoJobResponse from(DemoJob job) {
        return new DemoJobResponse(
                job.id(),
                job.title(),
                job.company(),
                job.location(),
                job.workArrangement(),
                job.employmentType(),
                job.summary()
        );
    }
}
