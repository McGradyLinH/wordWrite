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

import javax.servlet.http.HttpServletRequest;
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
import com.zhuozhengsoft.pageoffice.FileSaver;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;

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
        ModelAndView modelAndView = new ModelAndView("teacher/Choose");
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
        List<Essay> list1 = studentService.queryEssayList(essayDto);
        //根据essaycode去重
        list1 = list1.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(Essay::getName))), ArrayList::new));
        map.put("essays", list1);
        return modelAndView;
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
        String titleName = essay.getTitleName();
        map.put("xiaozuowen", "false");
        if (titleName.split("/").length == 5) {
            map.put("xiaozuowen", "true");
            session.setAttribute("titleSrc", titleName);
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
     * 打开word文档
     *
     * @param index 文章的编号
     * @return
     */
    @GetMapping(value = "/word")
    public ModelAndView showWord(HttpServletRequest request, Map<String, Object> map, Integer index, HttpSession session) {
        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
        poCtrl.setServerPage("/poserver.zz");//设置服务页面
        poCtrl.addCustomToolButton("保存", "Save", 1);//添加自定义保存按钮
        poCtrl.setSaveFilePage("/save");//设置处理文件保存的请求方法
        String docName = (String) session.getAttribute("docName");
        String stuName = (String) session.getAttribute("stuName");
        //打开word
        PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
        poCtrl.webOpen("c:\\word\\" + stuName + "\\" + docName + "_" + index + ".doc", OpenModeType.docAdmin, student.getName());
        map.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));
        ModelAndView mv = new ModelAndView("Word");
        return mv;
    }

    /**
     * word文档保存方法
     */
    @RequestMapping("/save")
    public void saveFile(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        FileSaver fs = new FileSaver(request, response);
        String stuName = (String) session.getAttribute("stuName");
        fs.saveToFile("c:\\word\\" + stuName + "\\" + fs.getFileName());
        fs.close();
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
        addCommentList.forEach(comment -> comment.setEssayCode(essayCode));
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







