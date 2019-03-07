package com.amatsuka.rememberer.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//TODO какойто костыль, заменить на норм решение
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/sql/users_controller_test_data.sql")
public class UsersControllerTest {

    @Autowired
    private MockMvc mvc;

    private Faker faker;

    public UsersControllerTest() {
        this.faker = new Faker();
    }

    @Test
    public void should_store_user() throws Exception {
        Map<String, String> params = new HashMap<String, String>() {{
            put("name", "name");
            put("username", "username");
        }};

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder request = post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(params));

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.username", is("username")));
    }

    @Test
    public void should_not_store_user_without_username() throws Exception {
        Map<String, String> params = new HashMap<String, String>() {{
            put("name", "name1");
        }};

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder request = post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(params));

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().is(422));
    }

    @Test
    public void should_not_store_user_with_non_unique_username() throws Exception {
        Map<String, String> params = new HashMap<String, String>() {{
            put("name", "name");
            put("username", "username");
        }};

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder request = post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(params));

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isCreated());

        ResultActions wrongResponse = this.mvc.perform(request);

         wrongResponse.andExpect(status().isBadRequest());

    }

    @Test
    public void should_update_user() throws Exception {
        Map<String, String> params = new HashMap<String, String>() {{
            put("name", "new_name");
            put("username", "new_username");
        }};

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder request = put("/users/" + 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(params));

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("new_name")))
                .andExpect(jsonPath("$.username", is("new_username")));

    }

    @Test
    public void should_delete_user() throws Exception {
        MockHttpServletRequestBuilder request = delete("/users/" + 1)
                .accept(MediaType.APPLICATION_JSON);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk());

    }

    @Test
    void should_get_one_user() throws Exception {
        MockHttpServletRequestBuilder request = get("/users/" + 1)
                .accept(MediaType.APPLICATION_JSON);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("name1")))
                .andExpect(jsonPath("$.username", is("username1")));
    }

    @Test
    void should_get_all_users() throws Exception {
        MockHttpServletRequestBuilder request = get("/users/")
                .accept(MediaType.APPLICATION_JSON);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk());
        //TODO проанализировать ответ на наличие всех пользователей
    }

    @Test
    void should_get_all_users_with_filter_and_sorting() throws Exception {
        Map<String, String> params = new HashMap<String, String>() {{
            put("name", "some_name");
            put("sort_by", "username");
            put("direction", "asc");
        }};
        
        MockHttpServletRequestBuilder request = get("/users/")
                .accept(MediaType.APPLICATION_JSON);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk());
        //TODO проанализировать ответ на наличие пользователей пользователей по фильтру и порядку
    }
}


