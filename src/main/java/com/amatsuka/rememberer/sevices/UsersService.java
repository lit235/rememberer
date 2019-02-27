package com.amatsuka.rememberer.sevices;

import com.amatsuka.rememberer.resources.UserResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    List<UserResource> get() {
        return null;
    }

    UserResource save(UserResource userResource) {
        return userResource;
    }

    UserResource update(UserResource userResource) {
        return userResource;
    }

    boolean delete(UserResource userResource) {
        return true;
    }

    boolean delete(long id) {
        return  true;
    }


}
