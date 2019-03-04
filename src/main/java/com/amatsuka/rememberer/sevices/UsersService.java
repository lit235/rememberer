package com.amatsuka.rememberer.sevices;

import com.amatsuka.rememberer.domain.entities.User;
import com.amatsuka.rememberer.domain.repositories.UsersRepository;
import com.amatsuka.rememberer.mappers.UserMapper;
import com.amatsuka.rememberer.resources.UserResource;
import com.amatsuka.rememberer.sevices.exceptions.UserNotDeletedException;
import com.amatsuka.rememberer.sevices.exceptions.UserNotStoredException;
import com.amatsuka.rememberer.web.requests.StoreUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<UserResource> findAll() {

        UserMapper mapper = UserMapper.INSTANCE;

        return this.usersRepository.findAll().stream()
                .map(mapper::userToUserResource)
                .collect(Collectors.toList());
    }

    public Optional<UserResource> findOne(Long id) {
        Optional<User> user = this.usersRepository.findById(id);

        UserMapper mapper = UserMapper.INSTANCE;

        return user.map(mapper::userToUserResource);
    }

    public UserResource create(UserResource userResource) {
        User user = UserMapper.INSTANCE.userResourceToUser(userResource);
        User result;

        try {
            result = this.usersRepository.save(user);

        } catch (DataIntegrityViolationException e) {
            throw new UserNotStoredException(e);
        }

        return UserMapper.INSTANCE.userToUserResource(result);
    }

    public UserResource create(StoreUserRequest storeUserRequest) {
       return create(UserMapper.INSTANCE.storeUserRequestToUserResource(storeUserRequest));
    }

    //TODO решить что делать с дублированием. В save проверяется новая ли это запись, возможно надо чекать наличие id
    public UserResource update(UserResource userResource) {
        User user = UserMapper.INSTANCE.userResourceToUser(userResource);
        User result;

        try {
            result = this.usersRepository.save(user);

        } catch (DataIntegrityViolationException e) {
            throw new UserNotStoredException(e);
        }

        return UserMapper.INSTANCE.userToUserResource(result);
    }

    public UserResource update(StoreUserRequest storeUserRequest) {
        return this.update(UserMapper.INSTANCE.storeUserRequestToUserResource(storeUserRequest));
    }

    public void deleteById(long id) {
        try {
            this.usersRepository.deleteById(id);

        } catch (EmptyResultDataAccessException e) {
            throw new UserNotDeletedException();
        }

    }


}