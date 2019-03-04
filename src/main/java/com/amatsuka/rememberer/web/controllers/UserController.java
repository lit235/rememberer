package com.amatsuka.rememberer.web.controllers;

import com.amatsuka.rememberer.resources.UserResource;
import com.amatsuka.rememberer.sevices.UsersService;
import com.amatsuka.rememberer.sevices.exceptions.RecordNotStoredException;
import com.amatsuka.rememberer.sevices.exceptions.UserNotStoredException;
import com.amatsuka.rememberer.sevices.exceptions.UserNotUpdatedException;
import com.amatsuka.rememberer.web.exceptions.BadRequestException;
import com.amatsuka.rememberer.web.exceptions.ResourceNotFoundException;
import com.amatsuka.rememberer.web.exceptions.ValidationException;
import com.amatsuka.rememberer.web.requests.StoreUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
class UserController {

    private UsersService service;

    @Autowired
    public UserController(UsersService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserResource> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public UserResource findOne(@PathVariable("id") Long id) {
        Optional<UserResource> userResourceOptional = service.findOne(id);

        return userResourceOptional.orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResource create(@RequestBody @Valid StoreUserRequest storeUserRequest, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException();
        }

        try {
            return service.create(storeUserRequest);
        } catch (UserNotStoredException e) {
            throw new BadRequestException();
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResource update(@PathVariable("id") Long id, @RequestBody @Valid StoreUserRequest storeUserRequest, Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException();
        }

        try {
            return service.update(storeUserRequest);
        } catch (UserNotUpdatedException e) {
            throw new BadRequestException();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete( @PathVariable("id") Long id ){
        if(!service.deleteById(id)) {
            throw new BadRequestException();
        }
    }
}
