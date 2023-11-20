package com.mhamdi.brander.apis.controllers;

import com.mhamdi.brander.apis.dto.request.NewUserRequest;
import com.mhamdi.brander.apis.dto.response.AppResponse;
import com.mhamdi.brander.apis.dto.response.ListResponse;
import com.mhamdi.brander.exceptions.apis.ApplicationException;
import com.mhamdi.brander.exceptions.apis.Errors;
import com.mhamdi.brander.services.intrfaces.UserService;
import com.mhamdi.db.models.User;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
class UsersController {
    private final UserService userService;

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/users")
    AppResponse all() {
        var items = userService.allUsers();
        var data = ListResponse.builder().items(items).build();
        return AppResponse.builder().data(data).status(1).message("success").build();
    }
    // end::get-aggregate-root[]

    @PostMapping("/users")
    AppResponse newUser(@RequestBody NewUserRequest request) {
        try {
            User item = userService.createUser(request);
            return AppResponse.builder().data(item).status(1).message("success").build();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw new ApplicationException(Errors.CREATE_ERROR, ex.getCause());
        }
    }

    // Single item

    @GetMapping("/users/{id}")
    AppResponse one(@PathVariable Long id) {
        try {
            var item = userService.getUserInfo(id);
            return AppResponse.builder().data(item).status(1).message("success").build();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw new ApplicationException(Errors.CREATE_ERROR, ex.getCause());
        }
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {

        return userService.getRepository().findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    return userService.getRepository().save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return userService.getRepository().save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    AppResponse deleteUser(@PathVariable Long id) {
        userService.getRepository().deleteById(id);
        return AppResponse.builder().status(1).message("success").build();
    }
}