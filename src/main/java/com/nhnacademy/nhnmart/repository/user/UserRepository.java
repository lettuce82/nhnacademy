package com.nhnacademy.nhnmart.repository.user;

import com.nhnacademy.nhnmart.domain.User;

public interface UserRepository {
    boolean match(String id, String password);
    User getUser(String id);
}
