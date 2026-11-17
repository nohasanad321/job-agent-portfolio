package com.noha.jobagent.portfolio.api;

import com.noha.jobagent.portfolio.api.dto.DemoJobPageResponse;
import com.noha.jobagent.portfolio.api.dto.DemoJobResponse;
import com.noha.jobagent.portfolio.service.DemoJobService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/demo/jobs")
public class DemoJobController {

    private final DemoJobService demoJobService;

    public DemoJobController(DemoJobService demoJobService) {
        this.demoJobService = demoJobService;
    }

    @GetMapping
    public DemoJobPageResponse findJobs(
            @RequestParam(required = false) @Size(max = 100) String q,
            @RequestParam(required = false) @Size(max = 100) String location,
            @RequestParam(defaultValue = "0") @Min(0) @Max(10_000) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size
    ) {
        return DemoJobPageResponse.from(demoJobService.findJobs(q, location, page, size));
    }

    @GetMapping("/{id}")
    public DemoJobResponse findJob(@PathVariable @Size(min = 1, max = 40) String id) {
        return DemoJobResponse.from(demoJobService.findJob(id));
    }
}
