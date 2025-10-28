package com.Developer.DreamShop.service.User;

import com.Developer.DreamShop.dto.UserDto;
import com.Developer.DreamShop.exceptions.AlreadyExistException;
import com.Developer.DreamShop.exceptions.ResourceNotFoundException;
import com.Developer.DreamShop.model.User;
import com.Developer.DreamShop.repository.UserRepository;
import com.Developer.DreamShop.request.CreateUserRequest;
import com.Developer.DreamShop.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class userService implements IUserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public User findUserById(Long userId) {
        return  userRepository
                .findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not Found "));

    }

    @Override
    public User createUser(CreateUserRequest request) {
       return Optional.of(request)
               .filter(user-> !userRepository.existsByEmail(request.getEmail()))
               .map(req ->{
                   User user = new User();
                   user.setEmail(req.getEmail());
                   user.setPassword(req.getPassword());
                   user.setFirstName(req.getFirstName());
                   user.setLastName(req.getLastName());
                   return userRepository.save(user);
               }).orElseThrow(()->new AlreadyExistException( "Oops !! : "+ request.getEmail() +"  already Exist"));

    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return  userRepository.findById(userId).map(existingUser->{
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(()-> new ResourceNotFoundException("User not Found !!"));

    }

    @Override
    public void deleteUser(Long userId) {
        // can do this
//        User user = findUserById(userId);
//        userRepository.delete(user);
        // or
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete,()-> {
            throw new ResourceNotFoundException("User  not found !!");
        });
    }

    @Override
    public UserDto convertToDto(User user){
        return modelMapper.map(user,UserDto.class);
    }




}
