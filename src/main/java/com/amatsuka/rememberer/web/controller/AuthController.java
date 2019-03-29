package com.amatsuka.rememberer.web.controller;

import com.amatsuka.rememberer.domain.entity.ApiClient;
import com.amatsuka.rememberer.dto.ApiClientDto;
import com.amatsuka.rememberer.repository.ApiClientsRepository;
import com.amatsuka.rememberer.repository.UsersRepository;
import com.amatsuka.rememberer.dto.UserDto;
import com.amatsuka.rememberer.security.users.JwtUsersTokenProvider;
import com.amatsuka.rememberer.service.ApiClientsService;
import com.amatsuka.rememberer.service.UsersService;
import com.amatsuka.rememberer.service.exceptions.UserNotStoredException;
import com.amatsuka.rememberer.web.exception.BadRequestException;
import com.amatsuka.rememberer.web.exception.ResourceNotFoundException;
import com.amatsuka.rememberer.web.request.AuthenticationRequest;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@Api(value = "Авторизация/регистрация", description = "Регистрация и авторизация пользователей административного интерфейса")
@RestController
@RequestMapping("/admin/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUsersTokenProvider jwtTokenProvider;

    @Autowired
    private UsersRepository users;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ApiClientsService apiClientsService;

    @PostMapping("signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {
        try {
            String username = data.getUsername();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));

            String token = jwtTokenProvider.createToken(
                    username,
                    this.users.findByUsername(username).orElseThrow(
                            () -> new UsernameNotFoundException("Username " + username + "not found")
                    ).getRoles());

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);

            return ok(model);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @PostMapping("signup")
    public UserDto signup(@RequestBody AuthenticationRequest data) {
            String username = data.getUsername();
            String password = data.getPassword();
            //TODO костыль с обозначением типа шифрования пароля
            String encryptedPassword = "{bcrypt}" + passwordEncoder.encode(password);

            UserDto userDto = UserDto.builder().username(username).passwordHash(encryptedPassword).build();

            return this.usersService.create(userDto);
    }

    @PostMapping("profile")
    public UserDetails profile(@ApiIgnore Authentication authentication) {

        return (UserDetails)authentication.getPrincipal();
    }

    @GetMapping("generate-token/{clientId}")
    public String generateToken(@PathVariable("clientId") String clientId) {
        Optional<ApiClientDto> client = apiClientsService.findByClientId(clientId);

        return apiClientsService.generateToken(client.orElseThrow(ResourceNotFoundException::new));
    }
}