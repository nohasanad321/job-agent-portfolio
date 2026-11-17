package com.noha.jobagent.portfolio.service;

public class DemoJobNotFoundException extends RuntimeException {

    public DemoJobNotFoundException(String id) {
        super("Demo job not found: " + id);
    }
}
