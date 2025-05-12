package com.e_commerceapp.clothshops.service.user;

import com.e_commerceapp.clothshops.dto.UserDTO;
import com.e_commerceapp.clothshops.exceptionhandler.AlreadyExistException;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.model.User;
import com.e_commerceapp.clothshops.repository.UserRepository;
import com.e_commerceapp.clothshops.requests.CreateUserReq;
import com.e_commerceapp.clothshops.requests.UserUpdateReq;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no user with id: " + userId
                )
        );
    }

    public User createUser(CreateUserReq createUserReq) {

        return Optional.of(createUserReq)
                .filter(request -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
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

    }

    public User updateUser(UserUpdateReq userUpdateReq, Long userId) {
        return userRepository.findById(userId).map(user -> {
            user.setFirstName(userUpdateReq.getFirstName());
            user.setLastName(userUpdateReq.getLastName());
            return userRepository.save(user);
        }).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no user with Id: " + userId
                )
        );
    }


    public void deleteUser(Long userId) {
//        userRepository.delete(getUserById(userId));

        userRepository.findById(userId).ifPresentOrElse(userRepository::delete,
                () -> {
                    throw new GlobalNotFoundException(
                            "there is no user with id: " + userId
                    );
                });

    }
}
