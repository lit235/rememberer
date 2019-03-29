package com.amatsuka.rememberer.mapper;

import com.amatsuka.rememberer.domain.entity.User;
import com.amatsuka.rememberer.dto.UserDto;
import com.amatsuka.rememberer.web.request.StoreUserRequest;
import com.amatsuka.rememberer.web.request.UpdateUserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserResource(User record);

    User userResourceToUser(UserDto userDto);

    UserDto storeUserRequestToUserResource(StoreUserRequest storeUserRequest);

    UserDto updateUserRequestToUserResource(UpdateUserRequest updateUserRequest);

}