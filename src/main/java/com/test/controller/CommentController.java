package com.test.controller;

import com.test.domain.Comment;
import com.test.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentsService commentsService;

    @PostMapping("/comment")
    public String insert(Comment comment, HttpSession session) {
        String docName = (String) session.getAttribute("docName");
        comment.setEssayCode(docName);
        int i = commentsService.insertComment(comment);
        String result = "fail";
        if (i > 0) {
            result = "success";
        }
        return result;
    }

    @DeleteMapping("/comment")
    public String delete(Comment comment, HttpSession session) {
        String docName = (String) session.getAttribute("docName");
        comment.setEssayCode(docName);
        int i = commentsService.deleteComment(comment);
        String result = "fail";
        if (i > 0) {
            result = "success";
        }
        return result;
    }

    @GetMapping("/comments")
    public List<Comment> comments(HttpSession session,Integer index){
        String docName = (String) session.getAttribute("docName");
        return commentsService.queryComments(docName, index);
    }

}
