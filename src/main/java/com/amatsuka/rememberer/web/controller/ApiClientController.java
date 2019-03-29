package com.amatsuka.rememberer.web.controller;

import com.amatsuka.rememberer.dto.ApiClientDto;
import com.amatsuka.rememberer.service.ApiClientsService;
import com.amatsuka.rememberer.web.exception.ResourceNotFoundException;
import com.amatsuka.rememberer.web.exception.ValidationException;
import com.amatsuka.rememberer.web.request.StoreApiClientRequest;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
@Api(value = "ApiClients", description = "Работа с данными клиентов для подключения к API сервиса")
@RestController
@RequestMapping("/api/admin/clients")
class ApiClientController {

    private ApiClientsService service;

    @Autowired
    public ApiClientController(ApiClientsService service) {
        this.service = service;
    }

    @GetMapping
    public List<ApiClientDto> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public ApiClientDto findOne(@PathVariable("id") Long id) {
        Optional<ApiClientDto> apiClientResourceOptional = service.findOne(id);

        return apiClientResourceOptional.orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiClientDto create(@RequestBody @Valid StoreApiClientRequest storeApiClientRequest, @ApiIgnore Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException();
        }

        return service.create(storeApiClientRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiClientDto update(@PathVariable("id") Long id, @RequestBody @Valid StoreApiClientRequest storeApiClientRequest, @ApiIgnore Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException();
        }

        return service.update(id, storeApiClientRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete( @PathVariable("id") Long id ){
        service.deleteById(id);
    }
}
