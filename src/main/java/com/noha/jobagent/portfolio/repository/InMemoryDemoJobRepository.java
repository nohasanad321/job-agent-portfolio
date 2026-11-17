package com.noha.jobagent.portfolio.repository;

import com.noha.jobagent.portfolio.model.DemoJob;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryDemoJobRepository implements DemoJobRepository {

    private static final List<DemoJob> JOBS = List.of(
            new DemoJob("demo-001", "Platform Engineer", "Fictional Northstar Labs",
                    "Example Region", "REMOTE", "FULL_TIME",
                    "Build internal developer tools for a fictional cloud platform."),
            new DemoJob("demo-002", "Application Developer", "Imaginary Cedar Systems",
                    "Sample City", "HYBRID", "CONTRACT",
                    "Maintain a fictional inventory application and its service integrations."),
            new DemoJob("demo-003", "Data Operations Analyst", "Made-Up Bluebird Works",
                    "Example Region", "ONSITE", "FULL_TIME",
                    "Prepare synthetic operational reports for a fictional business."),
            new DemoJob("demo-004", "Quality Engineer", "Fictional Northstar Labs",
                    "Demo District", "REMOTE", "CONTRACT",
                    "Exercise sample web workflows and document reproducible test results."),
            new DemoJob("demo-005", "Product Designer", "Imaginary Cedar Systems",
                    "Sample City", "HYBRID", "FULL_TIME",
                    "Create prototype screens for a made-up scheduling product."),
            new DemoJob("demo-006", "Support Specialist", "Made-Up Bluebird Works",
                    "Demo District", "REMOTE", "PART_TIME",
                    "Respond to fictional product questions in a training environment.")
    );

    @Override
    public List<DemoJob> findAll() {
        return JOBS;
    }

    @Override
    public Optional<DemoJob> findById(String id) {
        return JOBS.stream()
                .filter(job -> job.id().equals(id))
                .findFirst();
    }
}
