package com.noha.jobagent.portfolio.api;

import com.noha.jobagent.portfolio.model.DemoJob;
import com.noha.jobagent.portfolio.model.DemoJobPage;
import com.noha.jobagent.portfolio.service.DemoJobNotFoundException;
import com.noha.jobagent.portfolio.service.DemoJobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoJobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DemoJobService demoJobService;

    @Test
    void listsFilteredJobsAndPaginationMetadata() throws Exception {
        DemoJob job = job("demo-001");
        when(demoJobService.findJobs("platform", "example", 1, 2))
                .thenReturn(new DemoJobPage(List.of(job), 1, 2, 3, 2));

        mockMvc.perform(get("/api/demo/jobs")
                        .param("q", "platform")
                        .param("location", "example")
                        .param("page", "1")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobs[0].id").value("demo-001"))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(2));

        verify(demoJobService).findJobs("platform", "example", 1, 2);
    }

    @Test
    void getsJobById() throws Exception {
        when(demoJobService.findJob("demo-001")).thenReturn(job("demo-001"));

        mockMvc.perform(get("/api/demo/jobs/demo-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("demo-001"))
                .andExpect(jsonPath("$.company").value("Fictional Northstar Labs"));
    }

    @Test
    void returnsStructuredNotFoundForUnknownId() throws Exception {
        when(demoJobService.findJob("missing"))
                .thenThrow(new DemoJobNotFoundException("missing"));

        mockMvc.perform(get("/api/demo/jobs/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("DEMO_JOB_NOT_FOUND"));
    }

    @Test
    void rejectsInvalidPaginationParameters() throws Exception {
        mockMvc.perform(get("/api/demo/jobs").param("size", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    @Test
    void rejectsFiltersLongerThanTheDocumentedLimit() throws Exception {
        mockMvc.perform(get("/api/demo/jobs")
                        .param("q", "a".repeat(101))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    private DemoJob job(String id) {
        return new DemoJob(id, "Platform Engineer", "Fictional Northstar Labs",
                "Example Region", "REMOTE", "FULL_TIME", "Synthetic sample role.");
    }
}
