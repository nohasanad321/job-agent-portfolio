package com.noha.jobagent.portfolio.demo;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/api/demo/jobs")
public class DemoJobController {
 private final DemoJobService service;
 public DemoJobController(DemoJobService service){this.service=service;}
 @GetMapping
 public Map<String,Object> search(@RequestParam(required=false) String query,@RequestParam(required=false) String region,
  @RequestParam(required=false) String type,@RequestParam(required=false) String seniority,@RequestParam(required=false) String skill,
  @RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="5") int size){
  if(page<0||size<1||size>100)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"page must be >= 0; size between 1 and 100");
  List<DemoJob> matches=service.filter(query,region,type,seniority,skill);
  long offset=(long)page*size;
  List<DemoJob> content=offset>=matches.size()?List.of():matches.subList((int)offset,(int)Math.min(offset+size,matches.size()));
  Map<String,Object> result=new LinkedHashMap<>();
  result.put("synthetic",true);result.put("source","DEMO_DATA_ONLY");result.put("page",page);result.put("size",size);
  result.put("totalElements",matches.size());result.put("totalPages",(matches.size()+size-1)/size);result.put("content",content);
  return result;
 }
 @GetMapping("/{id}")
 public DemoJob byId(@PathVariable String id){
  return service.all().stream().filter(j->j.id().equalsIgnoreCase(id)).findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Demo job not found"));
 }
}