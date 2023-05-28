package com.vodafone.rest.webservices.restfulwebservices.jpa;


import com.vodafone.rest.webservices.restfulwebservices.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfosDaoService extends JpaRepository<User, Integer> {
}
