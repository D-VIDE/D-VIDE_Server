package com.divide.repository;

import com.divide.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface UserRepository {

    /**
     * 모든 User들을 조회하는 테스트용 메서드
     * @return 모든 User들
     */
    List<User> getUsers();

    /**
     * 전달받은 User로 회원가입을 하는 메서드
     * @param user
     * @return
     */
    void signup(User user);

    /**
     * email로 회원을 조회하는 메서드
     * @param email
     * @return user
     */
    Optional<User> findByEmail(String email);
}
