package com.e_commerceapp.clothshops.service.user;

import com.e_commerceapp.clothshops.data.dto.UserDTO;
import com.e_commerceapp.clothshops.exceptionhandler.AlreadyExistException;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.mapper.UserMapper;
import com.e_commerceapp.clothshops.data.model.Users;
import com.e_commerceapp.clothshops.data.repository.UserRepository;
import com.e_commerceapp.clothshops.rest.requests.CreateUserReq;
import com.e_commerceapp.clothshops.rest.requests.UserUpdateReq;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO getUserById(Long userId) {
         return userRepository.findById(userId).map(userMapper::createUserDtoFromUserEntity).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no user with id: " + userId
                )
        );
    }

    public UserDTO createUser(CreateUserReq createUserReq) {

        Users createdUser =  Optional.of(createUserReq)
                .filter(request -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    Users user = new Users();
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    user.setEmail(req.getEmail());
                    user.setPassword(req.getPassword());
                    return userRepository.save(user);
                })
                .orElseThrow(
                        () -> new AlreadyExistException(
                                "there is a user with email: " + createUserReq.getEmail()
                        )
                );
        return userMapper.createUserDtoFromUserEntity(createdUser);
    }

    public UserDTO updateUser(UserUpdateReq userUpdateReq, Long userId) {
        Users updatedUser = userRepository.findById(userId).map(user -> {
            user.setFirstName(userUpdateReq.getFirstName());
            user.setLastName(userUpdateReq.getLastName());
            return userRepository.save(user);
        }).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no user with Id: " + userId
                )
        );
        return userMapper.createUserDtoFromUserEntity(updatedUser);
    }


    public void deleteUser(Long userId) {

        userRepository.findById(userId).ifPresentOrElse(userRepository::delete,
                () -> {
                    throw new GlobalNotFoundException(
                            "there is no user with id: " + userId
                    );
                });

    }
}
