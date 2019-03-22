package com.amatsuka.rememberer.sevices;

import com.amatsuka.rememberer.domain.entities.QUser;
import com.amatsuka.rememberer.domain.entities.User;
import com.amatsuka.rememberer.domain.repositories.UsersRepository;
import com.amatsuka.rememberer.dto.UserDto;
import com.amatsuka.rememberer.mappers.UserMapper;
import com.amatsuka.rememberer.security.users.JwtUsersTokenProvider;
import com.amatsuka.rememberer.sevices.exceptions.UserNotDeletedException;
import com.amatsuka.rememberer.sevices.exceptions.UserNotStoredException;
import com.amatsuka.rememberer.web.requests.StoreUserRequest;
import com.amatsuka.rememberer.web.requests.UserFilterRequest;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
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

    @Autowired
    public UsersService(UsersRepository usersRepository, UserMapper userMapper, JwtUsersTokenProvider jwtUsersTokenProvider, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
        this.jwtUsersTokenProvider = jwtUsersTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDto> findAll() {
        
        return  Lists.newArrayList(this.usersRepository.findAll()).stream()
                .map(userMapper::userToUserResource)
                .collect(Collectors.toList());
    }

    private Predicate buildDslQuery(UserFilterRequest filterRequest) {

        BooleanExpression query = QUser.user.id.isNotNull();

        if (filterRequest.getName() != null) {
            query = query.and(QUser.user.name.like(filterRequest.getName()));
        }

        if (filterRequest.getUsername() != null) {
            query = query.and(QUser.user.username.like(filterRequest.getUsername()));
        }

        if (filterRequest.getCreatedAtFrom() != null) {
            query = query.and(QUser.user.createdAt.gt(filterRequest.getCreatedAtFrom()));
        }

        if (filterRequest.getCreatedAtTo() != null) {
            query = query.and(QUser.user.createdAt.lt(filterRequest.getCreatedAtFrom()));
        }

        return query;
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
        user.setId(id);

        User result;

        try {
            result = this.usersRepository.save(user);

        } catch (DataIntegrityViolationException e) {
            throw new UserNotStoredException(e);
        }

        return userMapper.userToUserResource(result);
    }

    public UserDto update(Long id, StoreUserRequest storeUserRequest) {
        return this.update(id, userMapper.storeUserRequestToUserResource(storeUserRequest));
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
