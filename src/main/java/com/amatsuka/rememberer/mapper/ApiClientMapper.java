package com.amatsuka.rememberer.mapper;

import com.amatsuka.rememberer.domain.entity.ApiClient;
import com.amatsuka.rememberer.dto.ApiClientDto;
import com.amatsuka.rememberer.web.request.StoreApiClientRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApiClientMapper {

    ApiClientDto apiClientToApiClientResource(ApiClient apiClient);

    ApiClient apiClientResourceToApiClient(ApiClientDto apiClientDto);

    ApiClientDto storeApiClientRequestToApiClientResource(StoreApiClientRequest storeApiClientRequest);

}