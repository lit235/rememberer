package com.amatsuka.rememberer.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class BaseTest {
    public <T> T retrieveResourceFromResponse(String jsonFromResponse, Class<T> clazz)
            throws IOException {

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(jsonFromResponse, clazz);
    }

    public List<HashMap<String, String>> retrieveResourceCollectionFromResponse(String jsonFromResponse)
            throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        return  mapper.readValue(
                jsonFromResponse, new TypeReference<List<HashMap<String, String>>>() { });
    }

    public MockHttpServletRequestBuilder appendAuthorizationToken(MockHttpServletRequestBuilder request, String apiToken) {
        return request.header("Authorization", "Bearer " + apiToken);
    }
}
