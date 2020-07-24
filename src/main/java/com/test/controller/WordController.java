package com.test.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.zhuozhengsoft.pageoffice.*;

/**
 * @author Administrator
 */
@RestController
public class WordController {
    @Value("${posyspath}")
    private String poSysPath;

    @GetMapping(value = "/index")
    public ModelAndView showIndex() {
        ModelAndView mv = new ModelAndView("Index");
        return mv;
    }

    /**
     * 打开word文档
     *
     * @param request
     * @param map
     * @param index   文章的编号
     * @param docName word文档的名称
     * @return
     */
    @GetMapping(value = "/word")
    public ModelAndView showWord(HttpServletRequest request, Map<String, Object> map, Integer index, String docName) {
        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
        poCtrl.setServerPage("/poserver.zz");//设置服务页面
        poCtrl.addCustomToolButton("保存", "Save", 1);//添加自定义保存按钮
//		poCtrl.addCustomToolButton("盖章","AddSeal",2);//添加自定义盖章按钮
        poCtrl.setSaveFilePage("/save");//设置处理文件保存的请求方法
        //打开word
        poCtrl.webOpen("c:\\word\\" + docName + "" + index + ".doc", OpenModeType.docAdmin, "张三");
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
     * 添加PageOffice的服务器端授权程序Servlet（必须）
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        com.zhuozhengsoft.pageoffice.poserver.Server poserver = new com.zhuozhengsoft.pageoffice.poserver.Server();
        poserver.setSysPath(poSysPath);//设置PageOffice注册成功后,license.lic文件存放的目录
        ServletRegistrationBean srb = new ServletRegistrationBean(poserver);
        srb.addUrlMappings("/poserver.zz");
        srb.addUrlMappings("/posetup.exe");
        srb.addUrlMappings("/pageoffice.js");
        srb.addUrlMappings("/jquery.min.js");
        srb.addUrlMappings("/pobstyle.css");
        srb.addUrlMappings("/sealsetup.exe");
        return srb;
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
