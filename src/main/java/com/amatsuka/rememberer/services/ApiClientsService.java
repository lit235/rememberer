package com.amatsuka.rememberer.services;

import com.amatsuka.rememberer.domain.entities.ApiClient;
import com.amatsuka.rememberer.domain.repositories.ApiClientsRepository;
import com.amatsuka.rememberer.dto.ApiClientDto;
import com.amatsuka.rememberer.mappers.ApiClientMapper;
import com.amatsuka.rememberer.security.users.JwtUsersTokenProvider;
import com.amatsuka.rememberer.services.exceptions.ApiClientNotDeletedException;
import com.amatsuka.rememberer.services.exceptions.ApiClientNotStoredException;
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

    private final ApiClientMapper apiClientMapper;
    
    @Autowired
    public ApiClientsService(JwtUsersTokenProvider jwtTokenProvider, ApiClientsRepository apiClientsRepository, ApiClientMapper apiClientMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.apiClientsRepository = apiClientsRepository;
        this.apiClientMapper = apiClientMapper;
    }

    public List<ApiClientDto> findAll() {


        return  Lists.newArrayList(this.apiClientsRepository.findAll()).stream()
                .map(apiClientMapper::apiClientToApiClientResource)
                .collect(Collectors.toList());
    }


    public Optional<ApiClientDto> findOne(Long id) {
        Optional<ApiClient> apiClient = this.apiClientsRepository.findById(id);
        
        return apiClient.map(apiClientMapper::apiClientToApiClientResource);
    }

    public ApiClientDto create(ApiClientDto ApiClientDto) {
        ApiClient apiClient = apiClientMapper.apiClientResourceToApiClient(ApiClientDto);


        ApiClient result;

        try {
            result = this.apiClientsRepository.save(apiClient);

        } catch (DataIntegrityViolationException e) {
            throw new ApiClientNotStoredException(e);
        }

        return apiClientMapper.apiClientToApiClientResource(result);
    }

    public ApiClientDto create(StoreApiClientRequest storeApiClientRequest) {
       return create(apiClientMapper.storeApiClientRequestToApiClientResource(storeApiClientRequest));
    }

    //TODO решить что делать с дублированием. В save проверяется новая ли это запись, возможно надо чекать наличие id
    public ApiClientDto update(Long id, ApiClientDto ApiClientDto) {
        ApiClient apiClient = apiClientMapper.apiClientResourceToApiClient(ApiClientDto);
        apiClient.setId(id);

        ApiClient result;

        try {
            result = this.apiClientsRepository.save(apiClient);

        } catch (DataIntegrityViolationException e) {
            throw new ApiClientNotStoredException(e);
        }

        return apiClientMapper.apiClientToApiClientResource(result);
    }

    public ApiClientDto update(Long id, StoreApiClientRequest storeApiClientRequest) {
        return this.update(id, apiClientMapper.storeApiClientRequestToApiClientResource(storeApiClientRequest));
    }

    public void deleteById(long id) {
        try {
            this.apiClientsRepository.deleteById(id);

        } catch (EmptyResultDataAccessException e) {
            throw new ApiClientNotDeletedException();
        }

    }

    public String generateToken(ApiClientDto apiClientDto) {
        return jwtTokenProvider.createToken(
                apiClientDto.getClientId(),
                this.apiClientsRepository.findByClientId(apiClientDto.getClientId()).orElseThrow(
                        () -> new UsernameNotFoundException("Username " + apiClientDto.getClientId() + "not found")
                ).getRoles());
    }


}
