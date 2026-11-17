package com.noha.jobagent.portfolio.repository;

import com.noha.jobagent.portfolio.model.DemoJob;

import java.util.List;
import java.util.Optional;

public interface DemoJobRepository {

    List<DemoJob> findAll();

    Optional<DemoJob> findById(String id);
}
