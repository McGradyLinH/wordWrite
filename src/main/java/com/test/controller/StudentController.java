package com.test.controller;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.test.domain.Essay;
import com.test.domain.EssayDto;
import com.test.domain.PlatformUser;
import com.test.domain.UserDto;
import com.test.service.EmailService;
import com.test.service.PlatformUserService;
import com.test.service.StudentService;
import com.test.util.CodeUtil;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private PlatformUserService userService;

    @Autowired
    private EmailService emailService;

    /**
     * 学生首页
     *
     * @return
     */
    @GetMapping("/stuIndex")
    public ModelAndView stuIndex(HttpSession session, Map<String, Object> map) {
        //登录学生
        PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
        Integer studentId = student.getId();
        EssayDto essayDto = new EssayDto();
        essayDto.setStuId(studentId);
        essayDto.setVersions(3);
        List<Essay> list = studentService.queryEssayList(essayDto);
        //根据essayCode去重
        list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(Essay::getName))), ArrayList::new));
        map.put("essays", list);
        return new ModelAndView("student/Index");
    }

    /**
     * 去到学生查看文章页面
     *
     * @param essayName 文件名称
     * @param stuName   学生姓名
     * @return
     */
    @GetMapping(value = "/stucheck")
    public ModelAndView stucheck(String essayName, HttpSession session, String stuName, Integer versions) {
        ModelAndView mv = new ModelAndView("redirect:/check");
        session.setAttribute("docName", essayName);
        session.setAttribute("stuName", stuName);
        session.setAttribute("versions", versions);
        return mv;
    }

    /**
     * 学生写作页面
     *
     * @return
     */
    @GetMapping("/stuWrite")
    public ModelAndView stuWrite(Map<String, Object> map) {
        UserDto userDto = new UserDto();
        userDto.setRole(2);
        map.put("teachers", userService.queryUsersByDto(userDto));
        return new ModelAndView("student/Write");
    }

    /**
     * 学生提交写的作文
     *
     * @param content   文章的内容
     * @param titleName 文章的标题
     * @return
     */
    @PostMapping("/saveWrite")
    @Transactional(rollbackFor = Exception.class)
    public ModelAndView saveWrite(String content, HttpSession session, String titleName,
                                  MultipartFile titleImage, Integer teacherId) {
        PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
        //减掉学生的批改数
        int i1 = studentService.decrementSurplus(student);
        if (i1 == 0) {
            return new ModelAndView("redirect:/stuIndex");
        }
        Essay essay = new Essay();
        essay.setEssayType(2);
        if (!"".equals(titleName)) {
            essay.setTitleName(titleName);
        }
        if (!titleImage.isEmpty()) {
            essay.setEssayType(1);
            String filename = UUID.randomUUID().toString().replaceAll("-", "");
            String filePath = "c:/word/titleimage/" + student.getName() + "/";
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String pathName = filePath + filename + "." + titleImage.getOriginalFilename().split("\\.")[1];
            file = new File(pathName);
            try {
                titleImage.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            essay.setTitlePath(pathName);
        }
        String essayCode = UUID.randomUUID().toString().replaceAll("-", "");
        essay.setName(essayCode);
        content = content.replaceAll("(\r\n|\n)", "<br/>");
        content = content.replaceAll(" ", "&nbsp;");
        content = content.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        essay.setEssayContent(content);
        PlatformUser enTeacher = userService.queryUserById(teacherId);
        essay.setEnTeacher(enTeacher);
        essay.setStudent(student);
        studentService.insertEssay(essay);
        emailService.sendSimpleEmail(student.getEmail(), "批改进度", "已提交给外教，我们会随时为你提供进度信息！");
        emailService.sendSimpleEmail(enTeacher.getEmail(), "作文提交", "有新作文提交！");
        return new ModelAndView("redirect:/stuIndex");
    }


    /**
     * 检查剩余文章数
     *
     * @return
     */
    @GetMapping("/checkSurplus")
    public String checkSurplus(HttpSession session) {
        PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
        if (student.getSurplus() >= 1) {
            return "success";
        }
        return "false";
    }

    /**
     * 学生修改文章状态
     *
     * @return
     */
    @PutMapping("/stuessay")
    public String stuessay(HttpSession session, Essay essay) {
        String docName = session.getAttribute("docName").toString();
        essay.setStatus(essay.getVersions());
        essay.setName(docName);
        int i = studentService.updateEssay(essay);
        if (i == 0) {
            return "fail";
        }
        return "success";
    }

    /**
     * 查看某一篇文章
     *
     * @param map
     * @param index
     * @param session
     * @return
     */
    @GetMapping("/check/{index}")
    public ModelAndView correct(Map<String, Object> map, @PathVariable("index") Integer index, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("student/CheckEssay");
        String docName = (String) session.getAttribute("docName");
        EssayDto essayDto = new EssayDto();
        essayDto.setEssayName(docName);
        essayDto.setEssayNumber(index);
        essayDto.setVersions(3);
        Essay essay = studentService.queryEssayList(essayDto).get(0);
        map.put("essay", essay);
        String titlePath = essay.getTitlePath();
        map.put("xiaozuowen", "false");
        if (null != titlePath && !"".equals(titlePath)) {
            map.put("xiaozuowen", "true");
            session.setAttribute("titleSrc", titlePath);
        }
        map.put("index", index);
        return modelAndView;
    }
}
