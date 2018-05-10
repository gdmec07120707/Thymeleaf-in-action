package com.fong.initializr.repository;

import com.fong.initializr.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserRepository {

    User saveOrUpdateUser(User user);

    void deleteUser(Long id);

    User getUserById(Long id);

    List<User> listUser();


}
