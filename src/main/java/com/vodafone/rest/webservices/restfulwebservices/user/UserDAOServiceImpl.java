package com.vodafone.rest.webservices.restfulwebservices.user;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDAOServiceImpl implements UserDAOService{
    private static List<User> users = new ArrayList<>();

    private static int id = 0;

/*    static {
        users.add(new User(++id,"kkatsaros","kkatsaros@outlook.de","Kostas","Katsaros"));
        users.add(new User(++id,"omytil","omytil@outlook.de","Orestis","Mytilinaios"));

    }*/

    @Override
    public List<User> findUsers() {
        return users;
    }

    @Override
    public User findUserById(Integer id) {
        return users.stream()
                .filter(user-> user.getId().equals(id))
                .findFirst()
                .orElse(new User());
    }

    @Override
    public void updateUsers(List<User> users) {
    }


    @Override
    public void createUsers(List<User> newUsers) {
        users.addAll(newUsers);
        System.out.println("Output users: " + users);
    }

    @Override
    public void createUser(User newUser) {
        newUser.setId(++id);
        users.add(newUser);
    }

    @Override
    public void deleteUsers(List<Integer> ids) {
        users.removeIf(user->ids.contains(user.getId()));
    }
}
