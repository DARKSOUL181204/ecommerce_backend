package com.Developer.DreamShop.controller;


import com.Developer.DreamShop.dto.UserDto;
import com.Developer.DreamShop.exceptions.AlreadyExistException;
import com.Developer.DreamShop.exceptions.ResourceNotFoundException;
import com.Developer.DreamShop.model.User;
import com.Developer.DreamShop.request.CreateUserRequest;
import com.Developer.DreamShop.request.UpdateUserRequest;
import com.Developer.DreamShop.response.ApiResponse;
import com.Developer.DreamShop.service.User.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/Users")
public class UserController {
    private final IUserService userService;


    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> findUserById(@PathVariable Long userId){
        try {
            User user = userService.findUserById(userId);
            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("User Successfully Found " ,userDto ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Error User Not Found " + e.getMessage() , null));

        }
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request){
        try {
            User user = userService.createUser(request);
            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("User Successfully Created " ,userDto ));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse("Already Crated " + e.getMessage() , null));
        }
    }


    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request,@PathVariable Long userId){
        try {
            User updatedUser = userService.updateUser(request,userId);
            UserDto userDto = userService.convertToDto(updatedUser);
            return ResponseEntity.ok(new ApiResponse("User Successfully Updated " ,userDto ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Error User not Found " + e.getMessage() , null));
        }
    }


    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@RequestParam Long userId){
        try {
           userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Deleted User Successfully" ,null ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Error User not Found " + e.getMessage() , null));
        }
    }


}
