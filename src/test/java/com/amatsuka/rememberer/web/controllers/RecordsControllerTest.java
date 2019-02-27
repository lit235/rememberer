package com.amatsuka.rememberer.web.controllers;

import com.amatsuka.rememberer.resources.RecordResource;
import com.amatsuka.rememberer.sevices.RecordEncryptService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("record_controller_test_data.sql")
public class RecordsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RecordEncryptService recordEncryptService;

    @Before
    public void before() {

    }

    @Test
    public void should_store_record() throws Exception {

        Map<String, String> recordParams = new HashMap<String, String>() {{
            put("text", "test text");
        }};

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder request = post("/records")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(recordParams));

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk()).andExpect(jsonPath("$.text", is("test text")));
    }

    @Test
    public void should_not_store_record_without_text() throws Exception {
        Map<String, String> recordParams = new HashMap<String, String>() {{
            put("text", "");
        }};

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder request = post("/records")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(recordParams));

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().is(422));
    }

    @Test
    public void should_store_record_with_password() throws Exception {
        Map<String, String> recordParams = new HashMap<String, String>() {{
            put("text", "test text");
            put("password", "secret");
        }};

       RecordResource resource = new RecordResource(null, recordParams.get("text"), null, null);
        RecordResource encryptedRecord = this.recordEncryptService.encrypt(resource, recordParams.get("password"));

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder request = post("/records")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(recordParams));

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(encryptedRecord.getText())))
                .andExpect(jsonPath("$.passwordHash", is(encryptedRecord.getPasswordHash())));
    }

    @Test
    public void should_give_record_by_code() {

    }

    @Test
    public void should_not_give_record_by_wrong_code() {

    }

    @Test
    public void should_give_record_with_password() {

    }

    @Test
    public void should_not_give_record_with_wrong_password() {

    }
}


