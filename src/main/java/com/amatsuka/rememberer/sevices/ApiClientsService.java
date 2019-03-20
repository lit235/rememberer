package com.amatsuka.rememberer.sevices;

import com.amatsuka.rememberer.domain.entities.ApiClient;
import com.amatsuka.rememberer.domain.repositories.ApiClientsRepository;
import com.amatsuka.rememberer.mappers.ApiClientMapper;
import com.amatsuka.rememberer.resources.ApiClientResource;
import com.amatsuka.rememberer.security.users.JwtUsersTokenProvider;
import com.amatsuka.rememberer.sevices.exceptions.ApiClientNotDeletedException;
import com.amatsuka.rememberer.sevices.exceptions.ApiClientNotStoredException;
import com.amatsuka.rememberer.web.requests.StoreApiClientRequest;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApiClientsService {

    private final JwtUsersTokenProvider jwtTokenProvider;


    private final ApiClientsRepository apiClientsRepository;

    @Autowired
    public ApiClientsService(JwtUsersTokenProvider jwtTokenProvider, ApiClientsRepository apiClientsRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.apiClientsRepository = apiClientsRepository;
    }

    public List<ApiClientResource> findAll() {

        ApiClientMapper mapper = ApiClientMapper.INSTANCE;

        return  Lists.newArrayList(this.apiClientsRepository.findAll()).stream()
                .map(mapper::apiClientToApiClientResource)
                .collect(Collectors.toList());
    }


    public Optional<ApiClientResource> findOne(Long id) {
        Optional<ApiClient> apiClient = this.apiClientsRepository.findById(id);

        ApiClientMapper mapper = ApiClientMapper.INSTANCE;

        return apiClient.map(mapper::apiClientToApiClientResource);
    }

    public ApiClientResource create(ApiClientResource ApiClientResource) {
        ApiClient apiClient = ApiClientMapper.INSTANCE.apiClientResourceToApiClient(ApiClientResource);


        ApiClient result;

        try {
            result = this.apiClientsRepository.save(apiClient);

        } catch (DataIntegrityViolationException e) {
            throw new ApiClientNotStoredException(e);
        }

        return ApiClientMapper.INSTANCE.apiClientToApiClientResource(result);
    }

    public ApiClientResource create(StoreApiClientRequest storeApiClientRequest) {
       return create(ApiClientMapper.INSTANCE.storeApiClientRequestToApiClientResource(storeApiClientRequest));
    }

    //TODO решить что делать с дублированием. В save проверяется новая ли это запись, возможно надо чекать наличие id
    public ApiClientResource update(Long id, ApiClientResource ApiClientResource) {
        ApiClient apiClient = ApiClientMapper.INSTANCE.apiClientResourceToApiClient(ApiClientResource);
        apiClient.setId(id);

        ApiClient result;

        try {
            result = this.apiClientsRepository.save(apiClient);

        } catch (DataIntegrityViolationException e) {
            throw new ApiClientNotStoredException(e);
        }

        return ApiClientMapper.INSTANCE.apiClientToApiClientResource(result);
    }

    public ApiClientResource update(Long id, StoreApiClientRequest storeApiClientRequest) {
        return this.update(id, ApiClientMapper.INSTANCE.storeApiClientRequestToApiClientResource(storeApiClientRequest));
    }

    public void deleteById(long id) {
        try {
            this.apiClientsRepository.deleteById(id);

        } catch (EmptyResultDataAccessException e) {
            throw new ApiClientNotDeletedException();
        }

    }

    public String generateToken(ApiClientResource apiClientResource) {
        return jwtTokenProvider.createToken(
                apiClientResource.getClientId(),
                this.apiClientsRepository.findByClientId(apiClientResource.getClientId()).orElseThrow(
                        () -> new UsernameNotFoundException("Username " + apiClientResource.getClientId() + "not found")
                ).getRoles());
    }


}
