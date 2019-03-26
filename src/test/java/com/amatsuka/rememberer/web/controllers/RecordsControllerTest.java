package com.amatsuka.rememberer.web.controllers;

import com.amatsuka.rememberer.dto.ApiClientDto;
import com.amatsuka.rememberer.dto.RecordDto;
import com.amatsuka.rememberer.services.ApiClientsService;
import com.amatsuka.rememberer.services.RecordEncryptService;
import com.amatsuka.rememberer.util.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//TODO какойто костыль, заменить на норм решение
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/sql/record_controller_test_data.sql")
public class RecordsControllerTest extends BaseTest {

    final static String ENCRYPTED_RECORD_CODE = "test_encrypted_code";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RecordEncryptService recordEncryptService;

    @Autowired
    private ApiClientsService apiClientsService;

    private String apiToken;

    @Before
    public void beforeTest() {
        ApiClientDto apiClientDto = ApiClientDto.builder().clientId("testclientid").name("testclient").build();
        apiClientDto = apiClientsService.create(apiClientDto);

        apiToken = apiClientsService.generateToken(apiClientDto);
    }

    @Test
    public void should_store_record() throws Exception {

        Map<String, String> recordParams = new HashMap<String, String>() {{
            put("text", "test text");
        }};

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder request = post("/api/records")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(recordParams));

        request = appendAuthorizationToken(request, apiToken);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk()).andExpect(jsonPath("$.text", is("test text")));
    }

    @Test
    public void should_not_store_record_without_text() throws Exception {
        Map<String, String> recordParams = new HashMap<String, String>() {{
            put("text", "");
        }};

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder request = post("/api/records")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(recordParams));

        request = appendAuthorizationToken(request, apiToken);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().is(422));
    }

    @Test
    public void should_store_record_with_password() throws Exception {
        Map<String, String> recordParams = new HashMap<String, String>() {{
            put("text", "test text");
            put("password", "secret");
        }};

        //@TODO Заменить на builder
       RecordDto resource = new RecordDto(null, recordParams.get("text"), null, null, null);
        RecordDto encryptedRecord = this.recordEncryptService.encrypt(resource, recordParams.get("password"));

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder request = post("/api/records")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(recordParams));

        request = appendAuthorizationToken(request, apiToken);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(encryptedRecord.getText())))
                .andExpect(jsonPath("$.passwordHash", is(encryptedRecord.getPasswordHash())));
    }

    @Test
    public void should_give_record_by_code() throws Exception {
        MockHttpServletRequestBuilder request = get("/api/records/" + "test1_code")
                .accept(MediaType.APPLICATION_JSON);

        request = appendAuthorizationToken(request, apiToken);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("test1")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void should_not_give_record_by_wrong_code() throws Exception {
        MockHttpServletRequestBuilder request = get("/api/records/" + "wrong_code")
                .accept(MediaType.APPLICATION_JSON);

        request = appendAuthorizationToken(request, apiToken);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isNotFound());
    }

    @Test
    public void should_give_record_with_password() throws Exception {
        MockHttpServletRequestBuilder request = get("/api/records/" + ENCRYPTED_RECORD_CODE).
                param("password", "secret")
                .accept(MediaType.APPLICATION_JSON);

        request = appendAuthorizationToken(request, apiToken);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("test text")))
                .andExpect(jsonPath("$.id", is(4)));

    }

    @Test
    public void should_not_give_record_with_wrong_password() throws Exception {
        MockHttpServletRequestBuilder request = get("/api/records/" + ENCRYPTED_RECORD_CODE).
                param("password", "worng_secret")
                .accept(MediaType.APPLICATION_JSON);

        request = appendAuthorizationToken(request, apiToken);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isNotFound());
    }
}


