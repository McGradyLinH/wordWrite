package com.test.dao;

import com.test.domain.Essay;
import com.test.domain.EssayDto;
import com.test.domain.PlatformUser;

import java.util.List;
import java.util.Map;

/**
 * @author lx
 * @version 1.0
 * @date 2020/7/24 22:35
 */
public interface StudentDao {
	/**
	 * 登录时，根据电话号码查询账号
	 * @param phone
	 * @return
	 */
    PlatformUser queryByPhone(String phone);

    /**
     * 根据多条件查询未完成批改的文章
     * @param essayDto
     * @return
     */
    List<Essay> queryEssayList(EssayDto essayDto);
    
    /**
     * 查询已经批改完成的作文
     * @param paramMap
     * @return
     */
    List<Essay> queryDoneEssay(Map<String, Object> paramMap);

    /**
     * 添加文章
     * @param essays
     * @return
     */
    int insertEssay(List<Essay> essays);

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
