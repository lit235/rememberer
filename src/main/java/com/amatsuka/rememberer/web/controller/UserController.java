package com.amatsuka.rememberer.web.controller;

import com.amatsuka.rememberer.dto.UserDto;
import com.amatsuka.rememberer.service.UsersService;
import com.amatsuka.rememberer.web.exception.ResourceNotFoundException;
import com.amatsuka.rememberer.web.exception.ValidationException;
import com.amatsuka.rememberer.web.request.StoreUserRequest;
import com.amatsuka.rememberer.web.request.UpdateUserRequest;
import com.amatsuka.rememberer.web.request.UserFilterRequest;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Api(value = "Users", description = "Управление пользователями административного интерфейса")
@RestController
@RequestMapping("/admin/users")
class UserController {

    private UsersService service;

    @Autowired
    public UserController(UsersService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserDto> findAll(UserFilterRequest filterRequest) {
        return service.findAll(filterRequest);
    }

    @GetMapping("{id}")
    public UserDto findOne(@PathVariable("id") Long id) {

        Optional<UserDto> userResourceOptional = service.findOne(id);

        return userResourceOptional.orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid StoreUserRequest storeUserRequest, @ApiIgnore Errors errors) {

        if (errors.hasErrors()) {
            throw new ValidationException();
        }

        return service.create(storeUserRequest);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@PathVariable("id") Long id, @RequestBody @Valid UpdateUserRequest updateUserRequest, @ApiIgnore Errors errors) {

        if (errors.hasErrors()) {
            throw new ValidationException();
        }

        return service.update(id, updateUserRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete( @PathVariable("id") Long id ) {

        service.deleteById(id);
    }
}
