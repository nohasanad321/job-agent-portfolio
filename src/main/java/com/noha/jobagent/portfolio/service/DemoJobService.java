package com.noha.jobagent.portfolio.service;

import com.noha.jobagent.portfolio.model.DemoJob;
import com.noha.jobagent.portfolio.model.DemoJobPage;
import com.noha.jobagent.portfolio.repository.DemoJobRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class DemoJobService {

    private final DemoJobRepository demoJobRepository;

    public DemoJobService(DemoJobRepository demoJobRepository) {
        this.demoJobRepository = demoJobRepository;
    }

    public DemoJobPage findJobs(String query, String location, int page, int size) {
        List<DemoJob> filteredJobs = demoJobRepository.findAll().stream()
                .filter(job -> matchesQuery(job, query))
                .filter(job -> containsIgnoreCase(job.location(), location))
                .toList();

        long offset = (long) page * size;
        List<DemoJob> pageJobs = offset >= filteredJobs.size()
                ? List.of()
                : filteredJobs.subList((int) offset, (int) Math.min(offset + size, filteredJobs.size()));
        int totalPages = (int) ((filteredJobs.size() + (long) size - 1) / size);

        return new DemoJobPage(pageJobs, page, size, filteredJobs.size(), totalPages);
    }

    public DemoJob findJob(String id) {
        return demoJobRepository.findById(id)
                .orElseThrow(() -> new DemoJobNotFoundException(id));
    }

    private boolean matchesQuery(DemoJob job, String query) {
        if (query == null || query.isBlank()) {
            return true;
        }
        return containsIgnoreCase(job.title(), query)
                || containsIgnoreCase(job.company(), query)
                || containsIgnoreCase(job.summary(), query);
    }

    private boolean containsIgnoreCase(String value, String query) {
        return query == null || query.isBlank()
                || value.toLowerCase(Locale.ROOT).contains(query.strip().toLowerCase(Locale.ROOT));
    }
}
