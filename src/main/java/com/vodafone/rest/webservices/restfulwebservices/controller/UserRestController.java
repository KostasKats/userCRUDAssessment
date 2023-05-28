package com.vodafone.rest.webservices.restfulwebservices.controller;


import com.vodafone.rest.webservices.restfulwebservices.dto.UserDto;
import com.vodafone.rest.webservices.restfulwebservices.exception.UserNotFoundException;
import com.vodafone.rest.webservices.restfulwebservices.jpa.UserRepository;
import com.vodafone.rest.webservices.restfulwebservices.user.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@ComponentScan(basePackages ="com.vodafone.rest.webservices.restfulwebservices")
@Tag(name = "User CRUD API", description = "CRUD API operations for managing users")
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping(path = "users/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("id" + id));

        return ResponseEntity.ok(modelMapper.map(user,UserDto.class));
    }

    @GetMapping(path = "/users")
    public List<UserDto> findUsers() {

        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping(path = "/createUser")
    public ResponseEntity<UserDto> addUser(@RequestBody @Valid UserDto userDto) {
        userRepository.save(modelMapper.map(userDto,User.class));
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping(path = "/createUsers")
    public ResponseEntity<List<UserDto>> addUsers(@RequestBody @Valid List<UserDto> usersDto) {
        List<User> users = usersDto.stream()
                .map(user -> modelMapper.map(user, User.class))
                .collect(Collectors.toList());

        userRepository.saveAll(users);

        return new ResponseEntity<>(usersDto, HttpStatus.CREATED);

    }

    @PutMapping(path = "/updateUser")
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserDto updatedUser) {
        userRepository.save(modelMapper.map(updatedUser,User.class));

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping(path = "/updateUsers")
    public ResponseEntity<UserDto> updateUsers(@RequestBody @Valid List<UserDto> userDtos) {
        List<User> users = userDtos.stream()
                .map(user -> modelMapper.map(user, User.class))
                .collect(Collectors.toList());

            userRepository.saveAll(users);
            return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/deleteUser")
    public ResponseEntity<UserDto> deleteUser(@RequestBody Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty())
            return ResponseEntity.notFound().build();

        User user = optionalUser.get();
        userRepository.delete(user);

        return new ResponseEntity<>(modelMapper.map(user,UserDto.class), HttpStatus.OK);
    }

    @DeleteMapping(path = "/deleteUsers")
    public ResponseEntity<List<Integer>> deleteUsers(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = ids.stream()
                .filter(id -> userRepository.findById(id).isEmpty())
                .collect(Collectors.toList());

        ids.removeAll(notFoundIds);
        ids.forEach(id -> {
            Optional<User> optionalUser = userRepository.findById(id);
            optionalUser.ifPresent(userRepository::delete);
        });

        if (notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundIds);
        }
    }


}

