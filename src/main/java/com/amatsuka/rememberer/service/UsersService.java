package com.amatsuka.rememberer.service;

import com.amatsuka.rememberer.repository.QPredicatesBuilder;
import com.amatsuka.rememberer.domain.entity.QUser;
import com.amatsuka.rememberer.domain.entity.User;
import com.amatsuka.rememberer.repository.UsersRepository;
import com.amatsuka.rememberer.dto.UserDto;
import com.amatsuka.rememberer.mapper.UserMapper;
import com.amatsuka.rememberer.security.users.JwtUsersTokenProvider;
import com.amatsuka.rememberer.service.exceptions.UserNotDeletedException;
import com.amatsuka.rememberer.service.exceptions.UserNotStoredException;
import com.amatsuka.rememberer.service.exceptions.UserNotUpdatedException;
import com.amatsuka.rememberer.web.request.StoreUserRequest;
import com.amatsuka.rememberer.web.request.UpdateUserRequest;
import com.amatsuka.rememberer.web.request.UserFilterRequest;
import com.github.javafaker.Faker;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    
    private final UserMapper userMapper;

    private final JwtUsersTokenProvider jwtUsersTokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final Faker faker;

    @Autowired
    public UsersService(UsersRepository usersRepository, UserMapper userMapper, JwtUsersTokenProvider jwtUsersTokenProvider, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
        this.jwtUsersTokenProvider = jwtUsersTokenProvider;
        this.passwordEncoder = passwordEncoder;

        this.faker = new Faker();
    }

    public List<UserDto> findAll() {
        
        return  Lists.newArrayList(this.usersRepository.findAll()).stream()
                .map(userMapper::userToUserResource)
                .collect(Collectors.toList());
    }

    private Predicate buildDslQuery(UserFilterRequest filterRequest) {

        return QPredicatesBuilder.builder()
        .addIfNonNull(filterRequest.getName(), QUser.user.name::like)
                .addIfNonNull(filterRequest.getUsername(), QUser.user.username::like)
                .addIfNonNull(filterRequest.getCreatedAtFrom(), QUser.user.createdAt::gt)
                .addIfNonNull(filterRequest.getCreatedAtTo(), QUser.user.createdAt::lt)
                .build();
    }

    private Pageable buildPagable(UserFilterRequest filterRequest) {
        Sort sort = Sort.by("id").ascending();

        if (filterRequest.getSortBy() != null && filterRequest.getDirection() != null) {
            sort = Sort.by(filterRequest.getSortBy());

            sort = filterRequest.getDirection().equals("asc") ? sort.ascending() : sort.descending();
        }


        if (filterRequest.getPage() != null && filterRequest.getPageSize() != null) {
            return PageRequest.of(filterRequest.getPage() + 1, filterRequest.getPageSize(), sort);
        }

        return PageRequest.of(0, Integer.MAX_VALUE, sort);
    }


    public List<UserDto> findAll(UserFilterRequest filterRequest) {
        
        return Lists.newArrayList(this.usersRepository.findAll(buildDslQuery(filterRequest), buildPagable(filterRequest))).stream()
                .map(userMapper::userToUserResource)
                .collect(Collectors.toList());
    }

    public Optional<UserDto> findOne(Long id) {
        Optional<User> user = this.usersRepository.findById(id);
        
        return user.map(userMapper::userToUserResource);
    }

    public UserDto create(UserDto userDto) {

        if (userDto.getPasswordHash() == null) {
            //@TODO Костыль пока нету восстановления пароля
            String password = "secret";
            userDto.setPasswordHash(passwordEncoder.encode(password));
        }

        User user = userMapper.userResourceToUser(userDto);

        User result;

        try {
            result = this.usersRepository.save(user);

        } catch (DataIntegrityViolationException e) {
            throw new UserNotStoredException(e);
        }

        return userMapper.userToUserResource(result);
    }

    public UserDto create(StoreUserRequest storeUserRequest) {
       return create(userMapper.storeUserRequestToUserResource(storeUserRequest));
    }

    //TODO решить что делать с дублированием. В save проверяется новая ли это запись, возможно надо чекать наличие id
    public UserDto update(Long id, UserDto userDto) {
        User user = userMapper.userResourceToUser(userDto);

        User result;

        try {
            result = this.usersRepository.save(user);

        } catch (DataIntegrityViolationException e) {
            throw new UserNotStoredException(e);
        }

        return userMapper.userToUserResource(result);
    }

    public UserDto update(Long id, UpdateUserRequest updateUserRequest) {

        UserDto updateUserDto = userMapper.updateUserRequestToUserResource(updateUserRequest);

        Optional<User> user = this.usersRepository.findById(id);

        UserDto userDto = userMapper.userToUserResource(user.orElseThrow(UserNotUpdatedException::new));

        //TODO как-то костыльно переносятся данные из запроса в dto

        if (updateUserDto.getUsername() != null) {
            userDto.setUsername(updateUserDto.getUsername());
        }

        if (updateUserDto.getName() != null) {
            userDto.setName(updateUserDto.getName());
        }

        return this.update(id, userDto);
    }

    public void deleteById(long id) {
        try {
            this.usersRepository.deleteById(id);

        } catch (EmptyResultDataAccessException e) {
            throw new UserNotDeletedException();
        }

    }


    public String generateToken(UserDto userDto) {
        return jwtUsersTokenProvider.createToken(
                userDto.getUsername(),
                this.usersRepository.findByUsername(userDto.getUsername()).orElseThrow(
                        () -> new UsernameNotFoundException("Username " + userDto.getUsername() + "not found")
                ).getRoles());
    }
}
