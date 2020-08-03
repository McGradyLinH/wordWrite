package com.test.dao;

import com.test.domain.PlatformUser;

import java.util.List;

public interface PlatformUserDao {
    List<PlatformUser> queryUsers();

    int registerUser(PlatformUser user);
}
