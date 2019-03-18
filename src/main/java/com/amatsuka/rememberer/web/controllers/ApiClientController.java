package com.amatsuka.rememberer.web.controllers;

import com.amatsuka.rememberer.resources.ApiClientResource;
import com.amatsuka.rememberer.sevices.ApiClientsService;
import com.amatsuka.rememberer.sevices.exceptions.UserNotDeletedException;
import com.amatsuka.rememberer.sevices.exceptions.UserNotStoredException;
import com.amatsuka.rememberer.sevices.exceptions.UserNotUpdatedException;
import com.amatsuka.rememberer.web.exceptions.BadRequestException;
import com.amatsuka.rememberer.web.exceptions.ResourceNotFoundException;
import com.amatsuka.rememberer.web.exceptions.ValidationException;
import com.amatsuka.rememberer.web.requests.StoreApiClientRequest;
import com.amatsuka.rememberer.web.requests.UserFilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/clients")
class ApiClientController {

    private ApiClientsService service;

    @Autowired
    public ApiClientController(ApiClientsService service) {
        this.service = service;
    }

    @GetMapping
    public List<ApiClientResource> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public ApiClientResource findOne(@PathVariable("id") Long id) {
        Optional<ApiClientResource> apiClientResourceOptional = service.findOne(id);

        return apiClientResourceOptional.orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiClientResource create(@RequestBody @Valid StoreApiClientRequest storeApiClientRequest, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException();
        }

        try {
            return service.create(storeApiClientRequest);
        } catch (UserNotStoredException e) {
            throw new BadRequestException();
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiClientResource update(@PathVariable("id") Long id, @RequestBody @Valid StoreApiClientRequest storeApiClientRequest, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException();
        }

        try {
            return service.update(id, storeApiClientRequest);
        } catch (UserNotUpdatedException e) {
            throw new BadRequestException();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete( @PathVariable("id") Long id ){

        try {
            service.deleteById(id);

        } catch (UserNotDeletedException e) {
            throw new BadRequestException();
        }

    }
}
