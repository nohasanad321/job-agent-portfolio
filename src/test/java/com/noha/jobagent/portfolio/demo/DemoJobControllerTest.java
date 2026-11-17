package com.noha.jobagent.portfolio.demo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc
class DemoJobControllerTest{
 @Autowired MockMvc mvc;
 @Test void syntheticFlagAndPagination() throws Exception{mvc.perform(get("/api/demo/jobs").param("size","2")).andExpect(status().isOk()).andExpect(jsonPath("$.synthetic").value(true)).andExpect(jsonPath("$.totalElements").value(8)).andExpect(jsonPath("$.content.length()").value(2));}
 @Test void regionFilter() throws Exception{mvc.perform(get("/api/demo/jobs").param("region","Worldwide")).andExpect(status().isOk()).andExpect(jsonPath("$.totalElements").value(4));}
 @Test void skillFilter() throws Exception{mvc.perform(get("/api/demo/jobs").param("skill","Redis")).andExpect(status().isOk()).andExpect(jsonPath("$.totalElements").value(2));}
 @Test void invalidPage() throws Exception{mvc.perform(get("/api/demo/jobs").param("page","-1")).andExpect(status().isBadRequest());}
 @Test void missingId() throws Exception{mvc.perform(get("/api/demo/jobs/not-real")).andExpect(status().isNotFound());}
}