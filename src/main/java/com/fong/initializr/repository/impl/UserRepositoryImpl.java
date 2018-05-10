package com.fong.initializr.repository.impl;

import com.fong.initializr.domain.User;
import com.fong.initializr.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepositoryImpl implements UserRepository{

    private final ConcurrentMap<Long,User> userMap = new ConcurrentHashMap<>();

    private static AtomicLong counter = new AtomicLong();

    @Override
    public User saveOrUpdateUser(User user) {
        Long id = user.getId();
        if(id <= 0){
            id = counter.incrementAndGet();
            user.setId(id);
        }
        this.userMap.put(id,user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        this.userMap.remove(id);
    }

    @Override
    public User getUserById(Long id) {
        User user = this.userMap.get(id);
        return user;
    }

    @Override
    public List<User> listUser() {
        return new ArrayList<User>(this.userMap.values());
    }
}
