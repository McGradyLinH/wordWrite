package com.test.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.domain.Essay;
import com.test.domain.EssayDto;
import com.test.domain.PlatformUser;
import com.test.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.zhuozhengsoft.pageoffice.*;

/**
 * @author Administrator
 */
@RestController
public class WordController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/choose")
    public ModelAndView choose(Map<String, Object> map, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("teacher/Choose");
        //登录老师
        PlatformUser teacher = (PlatformUser) session.getAttribute("loginUser");
        Integer role = teacher.getRole();
        EssayDto essayDto = new EssayDto();
        essayDto.setStatus(role);
        List<Essay> list1 = studentService.queryEssayList(essayDto);
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
        ModelAndView mv = new ModelAndView("teacher/Index");
        session.setAttribute("docName", essayName);
        session.setAttribute("stuName", stuName);
        return mv;
    }

    /**
     * 打开word文档
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
        //登录老师
        PlatformUser teacher = (PlatformUser) session.getAttribute("loginUser");
        essay.setName(docName);
        Integer role = teacher.getRole();
        if (role == 2) {
            essay.setEnTeacher(teacher);
        } else
            essay.setCNTeacher(teacher);
        System.out.println(teacher);
        int i = studentService.updateEssay(essay);
        if (i > 0) {
            return "success";
        }
        return "fail";
    }
}
