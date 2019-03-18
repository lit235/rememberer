package com.amatsuka.rememberer.domain.repositories;

import com.amatsuka.rememberer.domain.entities.ApiClient;
import com.amatsuka.rememberer.domain.entities.QApiClient;
import com.amatsuka.rememberer.domain.entities.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiClientsRepository extends QueryDslRepository<ApiClient, QApiClient, Long> {
    Optional<ApiClient> findByClientId(String clientId);
}
