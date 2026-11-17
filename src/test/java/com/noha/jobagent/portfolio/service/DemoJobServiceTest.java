package com.noha.jobagent.portfolio.service;

import com.noha.jobagent.portfolio.model.DemoJob;
import com.noha.jobagent.portfolio.model.DemoJobPage;
import com.noha.jobagent.portfolio.repository.DemoJobRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DemoJobServiceTest {

    @Mock
    private DemoJobRepository repository;

    @Test
    void appliesCaseInsensitiveQueryAndLocationFiltersBeforePagination() {
        DemoJob first = job("demo-001", "Platform Engineer", "Example Region");
        DemoJob second = job("demo-002", "Platform Analyst", "Sample City");
        DemoJob third = job("demo-003", "Platform Developer", "Example District");
        when(repository.findAll()).thenReturn(List.of(first, second, third));
        DemoJobService service = new DemoJobService(repository);

        DemoJobPage result = service.findJobs("PLATFORM", "example", 1, 1);

        assertThat(result.jobs()).containsExactly(third);
        assertThat(result.page()).isEqualTo(1);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.totalElements()).isEqualTo(2);
        assertThat(result.totalPages()).isEqualTo(2);
        verify(repository).findAll();
    }

    @Test
    void returnsAnEmptyPageWhenOffsetExceedsFilteredResults() {
        when(repository.findAll()).thenReturn(List.of(job("demo-001", "Engineer", "Example Region")));
        DemoJobService service = new DemoJobService(repository);

        DemoJobPage result = service.findJobs(null, null, 2, 10);

        assertThat(result.jobs()).isEmpty();
        assertThat(result.totalElements()).isEqualTo(1);
        assertThat(result.totalPages()).isEqualTo(1);
    }

    @Test
    void retrievesAJobThroughTheRepositoryBoundary() {
        DemoJob expected = job("demo-001", "Engineer", "Example Region");
        when(repository.findById(expected.id())).thenReturn(Optional.of(expected));
        DemoJobService service = new DemoJobService(repository);

        assertThat(service.findJob(expected.id())).isEqualTo(expected);
    }

    @Test
    void reportsMissingJobsExplicitly() {
        when(repository.findById("missing")).thenReturn(Optional.empty());
        DemoJobService service = new DemoJobService(repository);

        assertThatThrownBy(() -> service.findJob("missing"))
                .isInstanceOf(DemoJobNotFoundException.class)
                .hasMessageContaining("missing");
    }

    private DemoJob job(String id, String title, String location) {
        return new DemoJob(id, title, "Fictional Example Co", location,
                "REMOTE", "FULL_TIME", "Synthetic record for a demo.");
    }
}
