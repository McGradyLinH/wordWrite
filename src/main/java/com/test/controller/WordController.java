package com.test.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.domain.Essay;
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

    @GetMapping(value = "/choose")
    public ModelAndView showChoose(Map<String, Object> map) {
        ModelAndView mv = new ModelAndView("teacher/Choose");
        List<Essay> list = studentService.queryEssayList(1);
        map.put("essays", list);
        return mv;
    }

    @GetMapping(value = "/index")
    public ModelAndView showIndex(Map<String, Object> map, String essayName, HttpSession session, String stuName) {
        ModelAndView mv = new ModelAndView("teacher/Index");
        session.setAttribute("docName", essayName);
        session.setAttribute("stuName", stuName);
        return mv;
    }

    /**
     * 打开word文档
     *
     * @param request
     * @param map
     * @param index   文章的编号
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

    @RequestMapping("/save")
    public void saveFile(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        FileSaver fs = new FileSaver(request, response);
        String stuName = (String) session.getAttribute("stuName");
        fs.saveToFile("c:\\word\\" + stuName +"\\" + fs.getFileName());
        fs.close();
    }
}
