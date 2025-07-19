package com.e_commerceapp.clothshops.rest.controller;

import com.e_commerceapp.clothshops.data.dto.UserDTO;
import com.e_commerceapp.clothshops.rest.requests.CreateUserReq;
import com.e_commerceapp.clothshops.rest.requests.UserUpdateReq;
import com.e_commerceapp.clothshops.rest.response.ApiResponse;
import com.e_commerceapp.clothshops.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(new ApiResponse("Success", user));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createNewUser(@RequestBody CreateUserReq request) {
        UserDTO user = userService.createUser(request);
        return ResponseEntity.ok(new ApiResponse("create success", user));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateReq request, @PathVariable Long userId) {
        UserDTO user = userService.updateUser(request, userId);
        return ResponseEntity.ok(new ApiResponse("update success", user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse("delete success", null));
    }
}
