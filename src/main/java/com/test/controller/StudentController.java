package com.test.controller;

import com.test.domain.Student;
import com.zhuozhengsoft.pageoffice.DocumentVersion;
import com.zhuozhengsoft.pageoffice.FileSaver;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
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
import java.util.Map;
import java.util.UUID;

@RestController
public class StudentController {
    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("Login");
    }

    @PostMapping("/login")
    public ModelAndView login(String phone, String password, HttpSession session) {
        Student student = new Student("name" + phone, phone, password);
        session.setAttribute("loginUser", student);
        return new ModelAndView("Index");
    }

    @GetMapping("/toWrite")
    public ModelAndView toWrite(HttpServletRequest request, Map<String, Object> map) {
        HttpSession session = request.getSession();
        String name = UUID.randomUUID().toString().replace("-", "");
        session.setAttribute("name", name);
        Student student = (Student) session.getAttribute("loginUser");
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
        Student student = (Student) session.getAttribute("loginUser");
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
