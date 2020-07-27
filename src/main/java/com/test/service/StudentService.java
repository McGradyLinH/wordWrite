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

    /**
     * 减少学生作文数
     * @param student
     * @return
     */
    int decrementSurplus(PlatformUser student);

    /**
     * 增加学生作文数
     * @param student
     * @return
     */
    int incrementSurplus(PlatformUser student);

    /**
     * 修改文章的状态
     * @param essay
     * @return
     */
    int updateEssay(Essay essay);
}
