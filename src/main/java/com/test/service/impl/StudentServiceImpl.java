package com.test.service.impl;

import com.test.dao.CommentsDao;
import com.test.dao.StudentDao;
import com.test.domain.Comment;
import com.test.domain.Essay;
import com.test.domain.EssayDto;
import com.test.domain.PlatformUser;
import com.test.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lx
 * @version 1.0
 * @date 2020/7/24 22:53
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Resource
    private StudentDao studentDao;

    @Resource
    private CommentsDao commentsDao;

    @Override
    public PlatformUser queryByPhone(String phone) {
        return studentDao.queryByPhone(phone);
    }

    @Override
    public List<Essay> queryEssayList(EssayDto essayDto) {
        return studentDao.queryEssayList(essayDto);
    }

    @Override
    @Transactional
    public Essay insertEssay(Essay essay) {
        List<Essay> list = new ArrayList<>(4);
        for (int i = 1; i <= 4; i++) {
            Essay temp = new Essay();
            temp.setEssayNumber(i);
            temp.setName(essay.getName());
            temp.setEssayContent(essay.getEssayContent());
            temp.setEnTeacher(essay.getEnTeacher());
            temp.setTitleName(essay.getTitleName());
            temp.setStudent(essay.getStudent());
            list.add(essay);
        }
        int i = studentDao.insertEssay(list);
        if (i > 0) {
            //成功增加四次
            return essay;
        }
        return null;
    }

    @Override
    public int decrementSurplus(PlatformUser student) {
        return studentDao.decrementSurplus(student);
    }

    @Override
    public int incrementSurplus(PlatformUser student) {
        return studentDao.incrementSurplus(student);
    }

    @Override
    public int updateEssay(Essay essay) {
        return studentDao.updateEssay(essay);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int correctEssay(Essay essay, List<Comment> addCommentList, List<Comment> delCommentList) {
        //添加评论
        if (!addCommentList.isEmpty()){
            commentsDao.insertBatchComment(addCommentList);
        }
        //删除评论
        if (!delCommentList.isEmpty()){
            Map<String,Object> paramMap = new HashMap<>(3);
            paramMap.put("essayCode",essay.getName());
            paramMap.put("essayNumber",essay.getEssayNumber());
            paramMap.put("list",delCommentList);
            commentsDao.deleteBatchComment(paramMap);
        }
        //更新文章内容
        studentDao.updateEssay(essay);
        return 1;
    }

	@Override
	public List<Essay> queryDoneEssay(Map<String, Object> paramMap) {
		return studentDao.queryDoneEssay(paramMap);
	}
}
