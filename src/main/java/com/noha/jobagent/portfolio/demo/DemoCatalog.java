package com.noha.jobagent.portfolio.demo;
import java.util.List;
import org.springframework.stereotype.Component;
/** Synthetic examples only. Never presents fictitious listings as live opportunities. */
@Component
public class DemoCatalog {
 private final List<DemoJob> jobs=List.of(
  new DemoJob("DEMO-001","Senior Java Backend Engineer","Northstar Labs","Worldwide","Contract","Senior",List.of("Java","Spring Boot","REST"),"Build resilient APIs for a fictional distributed platform."),
  new DemoJob("DEMO-002","Cloud Platform Developer","Skyforge Systems","Europe","Full-time","Mid",List.of("Java","Docker","Kubernetes"),"Maintain containerized services at a fictional company."),
  new DemoJob("DEMO-003","Integration Architect","Bridgeworks","Middle East","Contract","Lead",List.of("Java","SOAP","Oracle"),"Design enterprise integrations for a fictional client."),
  new DemoJob("DEMO-004","Senior Kotlin Engineer","Juniper Digital","Worldwide","Full-time","Senior",List.of("Kotlin","Spring Boot","Kafka"),"Deliver event-driven services for a fictional product."),
  new DemoJob("DEMO-005","Backend Tech Lead","Riverstone Software","Americas","Contract","Lead",List.of("Java","Redis","AWS"),"Lead API reliability improvements at a fictional company."),
  new DemoJob("DEMO-006","Java API Engineer","Lantern Tech","Worldwide","Full-time","Mid",List.of("Java","REST","PostgreSQL"),"Create REST interfaces for a fictional SaaS team."),
  new DemoJob("DEMO-007","Microservices Engineer","Cloud Harbor","Europe","Contract","Senior",List.of("Spring Boot","Docker","Redis"),"Modernize fictional services using managed infrastructure."),
  new DemoJob("DEMO-008","Distributed Systems Engineer","Atlas Code","Worldwide","Contract","Lead",List.of("Java","Kafka","AWS"),"Implement asynchronous workflows for a fictional business.")
 );
 public List<DemoJob> all(){return jobs;}
}