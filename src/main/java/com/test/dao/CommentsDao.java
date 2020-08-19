package com.test.dao;

import com.test.domain.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lx
 * @version 1.0
 * @date 2020/8/11 22:18
 */
public interface CommentsDao {
    List<Comment> queryComments(@Param("essayCode") String essayCode,@Param("index") Integer index);

    int insertComment(Comment comment);

    int deleteComment(Comment comment);

    int insertBatchComment(List<Comment> comments);

    int deleteBatchComment(Map<String,Object> map);
}
