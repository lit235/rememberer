package com.amatsuka.rememberer.web.controller;

import com.amatsuka.rememberer.dto.ApiClientDto;
import com.amatsuka.rememberer.dto.UserDto;
import com.amatsuka.rememberer.service.ApiClientsService;
import com.amatsuka.rememberer.service.UsersService;
import com.amatsuka.rememberer.util.BaseTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
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
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//TODO какойто костыль, заменить на норм решение
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/sql/users_controller_test_data.sql")
public class AuthControllerTest extends BaseTest {

    @Autowired
    private MockMvc mvc;

    private Faker faker;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ApiClientsService apiClientsService;

    public AuthControllerTest() {
        this.faker = new Faker();
    }

    @Test
    public void should_register_user() throws Exception {
        Map<String, String> params = new HashMap<String, String>() {{
            put("username", "newusername");
            put("password", "secret");
        }};

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder request = post("/admin/auth/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(params));

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("newusername")));
    }

    @Test
    public void should_auth_user() throws Exception {
       /* SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String s = Base64.getEncoder().encodeToString(key.getEncoded());*/

        Map<String, String> params = new HashMap<String, String>() {{
            put("username", "username");
            put("password", "secret");
        }};

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder request = post("/admin/auth/signin")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(params));

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk());

        String json = response.andReturn().getResponse().getContentAsString();

        Map<String, String> authResponse = mapper.readValue(json, new TypeReference<Map<String, String>>(){});

        MockHttpServletRequestBuilder testAuthWorkingRequest = post("/admin/auth/profile")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(params));

        testAuthWorkingRequest = appendAuthorizationToken(testAuthWorkingRequest, authResponse.get("token"));

        ResultActions testAuthWorkingResponse = this.mvc.perform(testAuthWorkingRequest);

        testAuthWorkingResponse.andExpect(status().isOk());

    }

    @Test
    public void should_generate_token() throws Exception {
        Optional<UserDto> userDto = usersService.findOne(1L);

        String apiToken = usersService.generateToken(userDto.orElseThrow(() -> new UsernameNotFoundException("Username not found")));

        ApiClientDto apiClientDto = ApiClientDto.builder()
                .clientId("testclientid")
                .name("testclient")
                .userId(userDto.get().getId())
                .build();

        apiClientDto = apiClientsService.create(apiClientDto);

        MockHttpServletRequestBuilder request = get("/admin/auth/generate-token/" + apiClientDto.getClientId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        request = appendAuthorizationToken(request, apiToken);

        ResultActions response = this.mvc.perform(request);

        response.andExpect(status().isOk());
    }
}


