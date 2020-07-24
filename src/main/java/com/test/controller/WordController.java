package com.test.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.domain.Essay;
import com.test.domain.Student;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.zhuozhengsoft.pageoffice.*;

/**
 * @author Administrator
 */
@RestController
public class WordController {

    @GetMapping(value = "/choose")
    public ModelAndView showChoose(Map<String, Object> map) {
        ModelAndView mv = new ModelAndView("teacher/Choose");
        List<Essay> list = new ArrayList<>();
        Student student = new Student("namelx", "lx", "123456");
        Essay essay = new Essay();
        essay.setName("test");
        essay.setStudent(student);
        Essay essay1 = new Essay();
        essay1.setName("testboom");
        essay1.setStudent(student);
        list.add(essay);
        list.add(essay1);
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
//		poCtrl.addCustomToolButton("盖章","AddSeal",2);//添加自定义盖章按钮
        poCtrl.setSaveFilePage("/save");//设置处理文件保存的请求方法
        String docName = (String) session.getAttribute("docName");
        String stuName = (String) session.getAttribute("stuName");
        //打开word
        poCtrl.webOpen("c:\\word\\" + stuName + "\\" + docName + "_" + index + ".doc", OpenModeType.docAdmin, "张三");
        map.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));
        ModelAndView mv = new ModelAndView("Word");
        return mv;
    }

    @RequestMapping("/save")
    public void saveFile(HttpServletRequest request, HttpServletResponse response) {
        FileSaver fs = new FileSaver(request, response);
        fs.saveToFile("c:\\word\\" + fs.getFileName());
        fs.close();
    }


    /**
     * 添加印章管理程序Servlet（可选）
     *
     * @return
     */
//    @Bean
//    public ServletRegistrationBean servletRegistrationBean2() {
//        com.zhuozhengsoft.pageoffice.poserver.AdminSeal adminSeal = new com.zhuozhengsoft.pageoffice.poserver.AdminSeal();
//        adminSeal.setAdminPassword(poPassWord);//设置印章管理员admin的登录密码
//        adminSeal.setSysPath(poSysPath);//设置印章数据库文件poseal.db存放的目录
//        ServletRegistrationBean srb = new ServletRegistrationBean(adminSeal);
//        srb.addUrlMappings("/adminseal.zz");
//        srb.addUrlMappings("/sealimage.zz");
//        srb.addUrlMappings("/loginseal.zz");
//        return srb;
//    }
}
