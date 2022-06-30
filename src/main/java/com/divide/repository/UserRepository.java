package com.divide.repository;

import com.divide.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface UserRepository {
    List<User> getUsers() throws ExecutionException, InterruptedException;

    String signup(User user) throws ExecutionException, InterruptedException;
    Optional<User> findByEmail(String email) throws ExecutionException, InterruptedException;
}
