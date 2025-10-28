package com.Developer.DreamShop.service.User;


import com.Developer.DreamShop.dto.UserDto;
import com.Developer.DreamShop.model.User;
import com.Developer.DreamShop.request.CreateUserRequest;
import com.Developer.DreamShop.request.UpdateUserRequest;

import java.util.function.LongFunction;

public interface IUserService {
    User findUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request,Long userId);
    void deleteUser(Long userId);


    UserDto convertToDto(User user);
}
