package com.test.controller;

import com.test.domain.Essay;
import com.test.domain.PlatformUser;
import com.test.service.StudentService;
import com.test.util.DocumentHandler;
import com.zhuozhengsoft.pageoffice.DocumentVersion;
import com.zhuozhengsoft.pageoffice.FileSaver;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("Login");
    }

    @PostMapping("/login")
    public ModelAndView login(String phone, String password, HttpSession session, Map<String, Object> map) {
        PlatformUser student = studentService.queryByPhone(phone);
        ModelAndView modelAndView = new ModelAndView("student/Index");
        if (Objects.isNull(student) || !student.getPassword().equals(password)) {
            map.put("msg", "用户名或密码错误");
            modelAndView.setViewName("Login");
        }
        session.setAttribute("loginUser", student);
        switch (student.getRole()) {
            case 1:
                break;
            case 2:
                modelAndView.setViewName("teacher/Choose");
                break;
            case 3:
                modelAndView.setViewName("teacher/Choose");
                break;
            default:
                modelAndView.setViewName("Login");
        }
        return modelAndView;
    }

    @PostMapping("/saveWrite")
    public String saveWrite(String content, String titleName, HttpSession session) {
        PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
        Map<String, Object> map = new HashMap<>();
        map.put("content", content);
        map.put("title", titleName);
        String name = UUID.randomUUID().toString().replace("-", "");
        Essay essay = new Essay();
        essay.setName(name);
        essay.setStatus(1);
        essay.setStudent(student);
        //保存文件到数据库
        studentService.insertEssay(essay);
        String desSource = "c:/word/" + student.getName() + "/write.doc";
        File desFile = new File(desSource);
        DocumentHandler.createDoc(map, desSource);
        File file;
        for (int i = 1; i <= 4; i++) {
            //要保存的路径
            file = new File("c:\\word\\" + student.getName() + "\\" + name + "_" + i + ".doc");
            try {
                Files.copy(desFile.toPath(), file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "success";
    }

    @GetMapping("/toWrite")
    public ModelAndView toWrite(HttpServletRequest request, Map<String, Object> map) {
        HttpSession session = request.getSession();
        String name = UUID.randomUUID().toString().replace("-", "");
        //文件名称
        session.setAttribute("name", name);
        PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
        Essay essay = new Essay();
        essay.setName(name);
        essay.setStatus(1);
        essay.setStudent(student);
        //保存文件到数据库
        studentService.insertEssay(essay);
        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
        poCtrl.setServerPage("/poserver.zz");//设置服务页面
        poCtrl.addCustomToolButton("保存", "Save", 1);//添加自定义保存按钮
        poCtrl.setSaveFilePage("/writeSave");//设置处理文件保存的请求方法
        //打开word
        ModelAndView mv = new ModelAndView("Word");
        //创建word
        poCtrl.webCreateNew(student.getName(), DocumentVersion.Word2003);
        map.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));
        map.put("name", student.getName());
        return mv;
    }

    @RequestMapping("/writeSave")
    public void saveFile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
        FileSaver fs = new FileSaver(request, response);
        String fileName = "";
        File file;
        boolean isCreate = false;
        //为每一个学生创建一个文件夹
        file = new File("c:\\word\\" + student.getName() + "");
        if (!file.exists()) {
            file.mkdir();
        }
        String name = (String) session.getAttribute("name");

        for (int i = 1; i <= 4; i++) {
            fileName = "c:\\word\\" + student.getName() + "\\" + name + "_" + i + ".doc";
            file = new File(fileName);
            if (!file.exists()) {
                try {
                    isCreate = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                fs.saveToFile(fileName);
            }
            if (isCreate) {
                fs.saveToFile(fileName);
            }
        }
        fs.close();
    }

}
