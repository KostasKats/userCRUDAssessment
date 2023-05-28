package com.vodafone.rest.webservices.restfulwebservices.controller;


import com.vodafone.rest.webservices.restfulwebservices.exception.UserNotFoundException;
import com.vodafone.rest.webservices.restfulwebservices.jpa.UserInfosDaoService;
import com.vodafone.rest.webservices.restfulwebservices.user.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@Tag(name = "User CRUD API", description = "CRUD API operations for managing users")
public class UserRestController {

    @Autowired
    private UserInfosDaoService userInfosDaoService;


    @GetMapping(path = "users/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Integer id) {
        Optional<User> user = userInfosDaoService.findById(id);

        if(user.isEmpty())
            throw new UserNotFoundException("id: " + id);

        return ResponseEntity.ok(user.get());
    }

    @GetMapping(path = "/users")
    public List<User> findUsers() {
        return userInfosDaoService.findAll();
    }

    @PostMapping(path = "/createUser")
    public ResponseEntity<User> addUser(@RequestBody @Valid User user) throws Exception {
        userInfosDaoService.save(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping(path = "/createUsers")
    public ResponseEntity<User> addUsers(@RequestBody @Valid List<User> users) {
        userInfosDaoService.saveAll(users);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/updateUser")
    public ResponseEntity<User> updateUser(@RequestBody @Valid User updatedUser) {
        userInfosDaoService.save(updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping(path = "/updateUsers")
    public ResponseEntity<User> updateUsers(@RequestBody @Valid List<User> users) {
            userInfosDaoService.saveAll(users);
            return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/deleteUser")
    public ResponseEntity<User> deleteUser(@RequestBody Integer id) {
        Optional<User> optionalUser = userInfosDaoService.findById(id);

        if (optionalUser.isEmpty())
            return ResponseEntity.notFound().build();

        User user = optionalUser.get();
        userInfosDaoService.delete(user);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/deleteUsers")
    public ResponseEntity<List<Integer>> deleteUsers(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = ids.stream()
                .filter(id -> userInfosDaoService.findById(id).isEmpty())
                .collect(Collectors.toList());

        ids.removeAll(notFoundIds);
        ids.forEach(id -> {
            Optional<User> optionalUser = userInfosDaoService.findById(id);
            optionalUser.ifPresent(userInfosDaoService::delete);
        });

        if (notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundIds);
        }
    }


}

