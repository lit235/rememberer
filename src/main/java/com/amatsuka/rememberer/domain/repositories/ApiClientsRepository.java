package com.amatsuka.rememberer.domain.repositories;

import com.amatsuka.rememberer.domain.entities.ApiClient;
import com.amatsuka.rememberer.domain.entities.QApiClient;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiClientsRepository extends QueryDslRepository<ApiClient, QApiClient, Long> {
}
