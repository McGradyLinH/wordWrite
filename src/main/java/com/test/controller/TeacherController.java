package com.test.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.test.domain.Comment;
import com.test.domain.Essay;
import com.test.domain.EssayDto;
import com.test.domain.PlatformUser;
import com.test.domain.UserDto;
import com.test.service.EmailService;
import com.test.service.PlatformUserService;
import com.test.service.StudentService;

/**
 * @author Administrator
 */
@RestController
public class TeacherController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PlatformUserService userService;

    @GetMapping("/choose")
    public ModelAndView choose(Map<String, Object> map, HttpSession session) {
        return new ModelAndView("teacher/Choose");
    }

    @GetMapping("/teacheressays")
    public Map<String, Object> stuessays(HttpSession session, int pageSize, int pageIndex) {
        //登录老师
        PlatformUser teacher = (PlatformUser) session.getAttribute("loginUser");
        Integer role = teacher.getRole();
        EssayDto essayDto = new EssayDto();
        essayDto.setStatus(role - 1);
        if (role == 2) {
            essayDto.setEnTeacher(teacher);
        } else {
            essayDto.setCNTeacher(teacher);
        }
        List<Essay> list = studentService.queryEssayList(essayDto);
        //根据essaycode去重
        list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(Essay::getName))), ArrayList::new));
        return getStringObjectMap(pageSize, pageIndex, list);
    }

    static Map<String, Object> getStringObjectMap(int pageSize, int pageIndex, List<Essay> list) {
        List<Essay> result = list.stream().skip((pageIndex - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("rows",result);
        resultMap.put("total",result.size());
        return resultMap;
    }

    @GetMapping("/choosedone")
    public ModelAndView choosedone(Map<String, Object> map, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("teacher/ChooseDone");
        //登录老师
        PlatformUser teacher = (PlatformUser) session.getAttribute("loginUser");
        Integer role = teacher.getRole();
        Map<String, Object> paramMap = new HashMap<>(2);
        if (role == 2) {
            paramMap.put("enTeacherid", teacher.getId());
        } else {
            paramMap.put("CNTeacherid", teacher.getId());
        }
        List<Essay> list1 = studentService.queryDoneEssay(paramMap);
        //根据essaycode去重
        list1 = list1.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(Essay::getName))), ArrayList::new));
        map.put("essays", list1);
        return modelAndView;
    }

    /**
     * 去到批改文章的页面
     *
     * @param essayName 文件名称
     * @param stuName   学生姓名
     * @return
     */
    @GetMapping(value = "/index")
    public ModelAndView showIndex(String essayName, HttpSession session, String stuName) {
        ModelAndView mv = new ModelAndView("redirect:/correctindex");
        session.setAttribute("docName", essayName);
        session.setAttribute("stuName", stuName);
        return mv;
    }

    /**
     * 批改某一篇文章
     *
     * @param map
     * @param index
     * @param session
     * @return
     */
    @GetMapping("/correct/{index}")
    public ModelAndView correct(Map<String, Object> map, @PathVariable("index") Integer index, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("teacher/CorrectEssay");
        String docName = (String) session.getAttribute("docName");
        EssayDto essayDto = new EssayDto();
        essayDto.setEssayName(docName);
        essayDto.setEssayNumber(index);
        Essay essay = studentService.queryEssayList(essayDto).get(0);
        map.put("essay", essay);
        String titlePath = essay.getTitlePath();
        map.put("xiaozuowen", "false");
        if (null != titlePath && !"".equals(titlePath)) {
            map.put("xiaozuowen", "true");
            session.setAttribute("titleSrc", titlePath);
        }
        //登录老师
        PlatformUser teacher = (PlatformUser) session.getAttribute("loginUser");
        map.put("teacherName", teacher.getName());
        map.put("index", index);
        return modelAndView;
    }

    /**
     * 读取本地图片
     */
    @RequestMapping(value = "/showImg")
    public void showImg(HttpServletResponse response, HttpSession session) {
        String pathName = session.getAttribute("titleSrc").toString();
        File imgFile = new File(pathName);
        FileInputStream fin = null;
        OutputStream output = null;
        try {
            output = response.getOutputStream();
            fin = new FileInputStream(imgFile);
            byte[] arr = new byte[1024 * 10];
            int n;
            while ((n = fin.read(arr)) != -1) {
                output.write(arr, 0, n);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 修改文章的状态
     *
     * @param essay 要修改的文章
     * @return
     */
    @PutMapping("/essay")
    public String updateEssay(HttpSession session, Essay essay) {
        //文件名称
        String docName = (String) session.getAttribute("docName");
        EssayDto essayDto = new EssayDto();
        essayDto.setEssayName(docName);
        essayDto.setVersions(3);
        //该文章对应的学生,拿到学生的邮箱，给学生发送提醒邮件
        PlatformUser student = studentService.queryEssayList(essayDto).get(0).getStudent();
        //登录老师
        PlatformUser teacher = (PlatformUser) session.getAttribute("loginUser");
        essay.setName(docName);
        Integer role = teacher.getRole();
        String content;
        if (role == 2) {
            content = "该文章已由外教批改完成，现移交给中教修改！";
            UserDto userDto = new UserDto();
            userDto.setRole(3);
            List<PlatformUser> users = userService.queryUsersByDto(userDto);
            essay.setCNTeacher(users.get(0));
            essay.setStatus(2);
            emailService.sendSimpleEmail(users.get(0).getEmail(), "作文提交", "有新作文提交！");
        } else {
            essay.setStatus(3);
            content = "该文章已由中教批改完成，现在你可以登录我们的网站进行查看！";
        }
        emailService.sendSimpleEmail(student.getEmail(), "批改进度", content);
        int i = studentService.updateEssay(essay);
        if (i > 0) {
            return "success";
        }
        return "fail";
    }

    @PostMapping("/correctessay")
    public String correctEssay(Integer index, String content, HttpSession session,
                               String addComments, String delComments) {
        List<Comment> addCommentList = JSON.parseArray(addComments, Comment.class);
        List<Comment> delCommentList = JSON.parseArray(delComments, Comment.class);
        String essayCode = (String) session.getAttribute("docName");
        //登录老师
        PlatformUser teacher = (PlatformUser) session.getAttribute("loginUser");
        addCommentList.forEach(comment -> {
            comment.setEssayCode(essayCode);
            comment.setTeacher(teacher);
        });
        Essay essay = new Essay();
        essay.setEssayContent(content);
        essay.setEssayNumber(index);
        essay.setName(essayCode);
        int i = studentService.correctEssay(essay, addCommentList, delCommentList);
        if (i > 0) {
            return "success";
        }
        return "fail";
    }
}







