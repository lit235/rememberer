package com.amatsuka.rememberer.mappers;

import com.amatsuka.rememberer.domain.entities.ApiClient;
import com.amatsuka.rememberer.dto.ApiClientDto;
import com.amatsuka.rememberer.web.requests.StoreApiClientRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ApiClientMapper {

    ApiClientDto apiClientToApiClientResource(ApiClient apiClient);

    ApiClient apiClientResourceToApiClient(ApiClientDto apiClientDto);

    ApiClientDto storeApiClientRequestToApiClientResource(StoreApiClientRequest storeApiClientRequest);

}