package com.test.service;

import com.test.domain.Essay;
import com.test.domain.PlatformUser;

import java.util.List;

/**
 * @author lx
 * @version 1.0
 * @date 2020/7/24 22:52
 */
public interface StudentService {
    PlatformUser queryByPhone(String phone);

    List<Essay> queryEssayList(Integer status);

    Essay insertEssay(Essay essay);
}
