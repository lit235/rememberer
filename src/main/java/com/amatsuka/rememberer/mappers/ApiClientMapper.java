package com.amatsuka.rememberer.mappers;

import com.amatsuka.rememberer.domain.entities.ApiClient;
import com.amatsuka.rememberer.domain.entities.User;
import com.amatsuka.rememberer.resources.ApiClientResource;
import com.amatsuka.rememberer.resources.UserResource;
import com.amatsuka.rememberer.web.requests.StoreApiClientRequest;
import com.amatsuka.rememberer.web.requests.StoreUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ApiClientMapper {

    ApiClientMapper INSTANCE = Mappers.getMapper( ApiClientMapper.class );

    ApiClientResource apiClientToApiClientResource(ApiClient apiClient);

    ApiClient apiClientResourceToApiClient(ApiClientResource apiClientResource);

    ApiClientResource storeApiClientRequestToApiClientResource(StoreApiClientRequest storeApiClientRequest);

}