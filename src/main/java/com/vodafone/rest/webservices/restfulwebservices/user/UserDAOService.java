package com.vodafone.rest.webservices.restfulwebservices.user;

import java.util.List;

public interface UserDAOService {
    public List<User> findUsers();

    public User findUserById(Integer id);

    public void updateUsers(List<User> users);

    public void createUsers(List<User> users);

    public void createUser(User user);

    public void deleteUsers(List<Integer> ids);


}
