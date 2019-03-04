package com.amatsuka.rememberer.mappers;

import com.amatsuka.rememberer.domain.entities.User;
import com.amatsuka.rememberer.resources.UserResource;
import com.amatsuka.rememberer.web.requests.StoreUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    UserResource userToUserResource(User record);

    User userResourceToUser(UserResource userResource);

    UserResource storeUserRequestToUserResource(StoreUserRequest storeUserRequest);

}