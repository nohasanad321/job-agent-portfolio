package com.noha.jobagent.portfolio.api.dto;

import com.noha.jobagent.portfolio.model.DemoJobPage;

import java.util.List;

public record DemoJobPageResponse(
        List<DemoJobResponse> jobs,
        int page,
        int size,
        long totalElements,
        int totalPages
) {

    public static DemoJobPageResponse from(DemoJobPage jobPage) {
        return new DemoJobPageResponse(
                jobPage.jobs().stream().map(DemoJobResponse::from).toList(),
                jobPage.page(),
                jobPage.size(),
                jobPage.totalElements(),
                jobPage.totalPages()
        );
    }
}
