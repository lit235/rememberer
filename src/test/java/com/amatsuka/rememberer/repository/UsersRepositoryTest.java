package com.amatsuka.rememberer.repository;

import com.amatsuka.rememberer.domain.entity.ApiClient;
import com.amatsuka.rememberer.domain.entity.Record;
import com.amatsuka.rememberer.domain.entity.User;
import com.github.javafaker.Faker;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;

import static org.assertj.core.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class UsersRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ApiClientsRepository apiClientsRepository;

    private Faker faker;

    public UsersRepositoryTest() {
        this.faker = new Faker();
    }

    @Test
    public void should_store_user() {

        User user = User.builder()
                .name("test_name")
                .username("test_username")
                .passwordHash("test_passwordhash")
                .build();

        ApiClient apiClient = ApiClient.builder()
                .clientId("test_clientid")
                .name("test_name")
                .build();

        user.setApiClients(new HashSet<>(Sets.newHashSet(apiClient)));

        User result = this.usersRepository.save(user);

        apiClient.setUser(user);

        ArrayList<User> users = Lists.newArrayList(usersRepository.findAll());
        ArrayList<ApiClient> apiClients = Lists.newArrayList(apiClientsRepository.findAll());
    }
}
