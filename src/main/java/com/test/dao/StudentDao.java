package com.test.dao;

import com.test.domain.Essay;
import com.test.domain.PlatformUser;

import java.util.List;

/**
 * @author lx
 * @version 1.0
 * @date 2020/7/24 22:35
 */
public interface StudentDao {
    PlatformUser queryByPhone(String phone);

    List<Essay> queryEssayList(Integer status);

    int insertEssay(Essay essay);
}
