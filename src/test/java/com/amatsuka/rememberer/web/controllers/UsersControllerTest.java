package com.amatsuka.rememberer.web.controllers;

import com.github.javafaker.Faker;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
}


