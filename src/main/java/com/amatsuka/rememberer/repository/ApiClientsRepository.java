package com.amatsuka.rememberer.repository;

import com.amatsuka.rememberer.domain.entity.ApiClient;
import com.amatsuka.rememberer.domain.entity.QApiClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiClientsRepository extends QueryDslRepository<ApiClient, QApiClient, Long> {
    Optional<ApiClient> findByClientId(String clientId);
}
