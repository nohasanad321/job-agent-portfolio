package com.noha.jobagent.portfolio.model;

import java.util.List;

public record DemoJobPage(
        List<DemoJob> jobs,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
