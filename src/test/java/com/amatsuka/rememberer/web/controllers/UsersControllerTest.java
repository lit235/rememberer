package com.amatsuka.rememberer.web.controllers;

import com.amatsuka.rememberer.dto.UserDto;
import com.amatsuka.rememberer.sevices.UsersService;
import com.amatsuka.rememberer.util.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
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
public class UsersControllerTest extends BaseTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    UsersService usersService;

    private Faker faker;

    private String apiToken;

    public UsersControllerTest() {
        this.faker = new Faker();
    }

    @Before
    public void beforeTest() {
        Optional<UserDto> userDto = usersService.findOne(1L);

        apiToken = usersService.generateToken(userDto.orElseThrow(() -> new UsernameNotFoundException("Username not found")));
    }


    @Test
    public void should_store_user() throws Exception {
        Map<String, String> params = new HashMap<String, String>() {{
            put("name", "test_name");
            put("username", "test_username");
        }};

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder request = post("/admin/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(params));

        request = appendAuthorizationToken(request, apiToken);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("test_name")))
                .andExpect(jsonPath("$.username", is("test_username")));
    }

    @Test
    public void should_not_store_user_without_username() throws Exception {
        Map<String, String> params = new HashMap<String, String>() {{
            put("name", "name1");
        }};

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder request = post("/admin/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(params));

        request = appendAuthorizationToken(request, apiToken);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().is(422));
    }

    @Test
    public void should_not_store_user_with_non_unique_username() throws Exception {
        Map<String, String> params = new HashMap<String, String>() {{
            put("name", "test_name");
            put("username", "test_username");
        }};

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder request = post("/admin/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(params));

        request = appendAuthorizationToken(request, apiToken);

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

        MockHttpServletRequestBuilder request = put("/admin/users/" + 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(params));

        request = appendAuthorizationToken(request, apiToken);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("new_name")))
                .andExpect(jsonPath("$.username", is("new_username")));

    }

    @Test
    public void should_delete_user() throws Exception {
        MockHttpServletRequestBuilder request = delete("/admin/users/" + 2)
                .accept(MediaType.APPLICATION_JSON);

        request = appendAuthorizationToken(request, apiToken);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk());

       request = get("/admin/users/" + 2)
                .accept(MediaType.APPLICATION_JSON);

        request = appendAuthorizationToken(request, apiToken);

        response = this.mvc.perform(request);

        response.andExpect(status().isNotFound());

    }

    @Test
   public void should_get_one_user() throws Exception {
        MockHttpServletRequestBuilder request = get("/admin/users/" + 1)
                .accept(MediaType.APPLICATION_JSON);

        request = appendAuthorizationToken(request, apiToken);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("name1")))
                .andExpect(jsonPath("$.username", is("username1")));
    }

    @Test
    public void should_get_all_users() throws Exception {
        MockHttpServletRequestBuilder request = get("/admin/users/")
                .accept(MediaType.APPLICATION_JSON);

        request = appendAuthorizationToken(request, apiToken);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk());
        String responseContent = response.andReturn().getResponse().getContentAsString();
        List<HashMap<String, String>> users = retrieveResourceCollectionFromResponse(responseContent);

        assertThat(users.get(0).get("id"), is("1"));
        assertThat(users.get(1).get("id"), is("2"));
        assertThat(users.get(2).get("id"), is("3"));
        assertThat(users.get(3).get("id"), is("4"));
    }

    @Test
    public void should_get_all_users_with_filter_and_sorting() throws Exception {
        
        MockHttpServletRequestBuilder request = get("/admin/users/")
                .param("name", "some_name")
                .param("sortBy", "id")
                .param("direction", "desc")
                .accept(MediaType.APPLICATION_JSON);

        request = appendAuthorizationToken(request, apiToken);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk());

        String responseContent = response.andReturn().getResponse().getContentAsString();
        List<HashMap<String, String>> users = retrieveResourceCollectionFromResponse(responseContent);

        assertThat(users.get(0).get("id"), is("4"));
        assertThat(users.get(1).get("id"), is("3"));
    }
}


