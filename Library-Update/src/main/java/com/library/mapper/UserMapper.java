package com.library.mapper;


import com.library.domain.User;
import com.library.dto.RegisterDTO;
import com.library.dto.UserDTO;
import com.library.dto.request.RegisterRequest;
import com.library.dto.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring")
public interface UserMapper {

    User registerRequestToUser(RegisterRequest registerRequest);

    RegisterDTO userToRegisterDTO(User user);

    UserDTO userToUserDTO(User user);

    @Mapping(target = "roles",ignore = true)
    User userRequestToUser(UserRequest request);
}