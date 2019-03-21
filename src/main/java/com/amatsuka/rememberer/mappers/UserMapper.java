package com.amatsuka.rememberer.mappers;

import com.amatsuka.rememberer.domain.entities.User;
import com.amatsuka.rememberer.dto.UserDto;
import com.amatsuka.rememberer.web.requests.StoreUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserResource(User record);

    User userResourceToUser(UserDto userDto);

    UserDto storeUserRequestToUserResource(StoreUserRequest storeUserRequest);

}