package com.noha.jobagent.portfolio.demo;
import java.util.List;
public record DemoJob(String id, String title, String company, String region, String type, String seniority, List<String> skills, String description) {}