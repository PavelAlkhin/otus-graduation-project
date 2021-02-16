package ru.otus.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.ws.rs.core.MediaType;

import java.util.logging.Filter;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class MainControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    protected MockMvc mvc;

//    @Before
//    public void setup() {
//        mvc = MockMvcBuilders.webAppContextSetup(context)
//                .addFilters(springSecurityFilterChain)
//                .build();
//    }
//
//    protected RequestPostProcessor testUser() {
//        return user("user").password("userPass").roles("USER");
//    }
//
//    protected String createFoo() throws JsonProcessingException {
//        return new ObjectMapper().writeValueAsString(new Foo(randomAlphabetic(6)));
//    }

//    @Test
//    public void givenNoCsrf_whenAddFoo_thenForbidden() throws Exception {
//        mvc.perform(
//                post("/foos").contentType(MediaType.APPLICATION_JSON)
//
//                        .with(testUser())
//        ).andExpect(status().isForbidden());
//    }
}

