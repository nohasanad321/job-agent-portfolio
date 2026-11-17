package com.noha.jobagent.portfolio.demo;
import java.time.Duration;
import java.util.*;
import java.util.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
/** Cache-aside demonstration; Redis is best-effort, local catalog remains authoritative. */
@Service
public class DemoJobService {
 private final DemoCatalog catalog;
 private final StringRedisTemplate redis;
 private final ObjectMapper mapper;
 public DemoJobService(DemoCatalog catalog,StringRedisTemplate redis,ObjectMapper mapper){this.catalog=catalog;this.redis=redis;this.mapper=mapper;}
 public List<DemoJob> all(){
  try{
   String cached=redis.opsForValue().get("portfolio:demo:v1");
   if(cached!=null)return mapper.readValue(cached,mapper.getTypeFactory().constructCollectionType(List.class,DemoJob.class));
  }catch(Exception ignored){ /* No Redis? Demo remains usable. */ }
  List<DemoJob> jobs=catalog.all();
  try{redis.opsForValue().set("portfolio:demo:v1",mapper.writeValueAsString(jobs),Duration.ofMinutes(10));}catch(Exception ignored){ /* best effort */ }
  return jobs;
 }
 public List<DemoJob> filter(String query,String region,String type,String seniority,String skill){
  String q=norm(query),r=norm(region),t=norm(type),s=norm(seniority),k=norm(skill);
  return all().stream().filter(j->q.isEmpty() || norm(j.title()+" "+j.company()+" "+j.description()).contains(q))
  .filter(j->r.isEmpty()||norm(j.region()).equals(r))
  .filter(j->t.isEmpty()||norm(j.type()).equals(t))
  .filter(j->s.isEmpty()||norm(j.seniority()).equals(s))
  .filter(j->k.isEmpty()||j.skills().stream().anyMatch(x->norm(x).contains(k)))
  .toList();
 }
 private static String norm(String v){return v==null?"":v.toLowerCase(Locale.ROOT).trim();}
}