package com.test.controller;

import com.zhuozhengsoft.pageoffice.DocumentVersion;
import com.zhuozhengsoft.pageoffice.FileSaver;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
public class StudentController {

    @GetMapping("/toWrite")
    public ModelAndView toWrite(HttpServletRequest request, Map<String, Object> map) {
        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
        poCtrl.setServerPage("/poserver.zz");//设置服务页面
        poCtrl.addCustomToolButton("保存", "Save", 1);//添加自定义保存按钮
        poCtrl.setSaveFilePage("/writeSave");//设置处理文件保存的请求方法
        //打开word
        ModelAndView mv = new ModelAndView("Word");
        //创建word
        poCtrl.webCreateNew("张三", DocumentVersion.Word2003);
        map.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));
        return mv;
}

    @RequestMapping("/writeSave")
    public void saveFile(HttpServletRequest request, HttpServletResponse response) {
        FileSaver fs = new FileSaver(request, response);
        String fileName = "";
        File file;
        boolean isCreate = false;
        for (int i = 1; i <= 4; i++) {
            fileName = "c:\\word\\write" + i + ".doc";
            file = new File(fileName);
            if (!file.exists()) {
                try {
                    isCreate = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                fs.saveToFile(fileName);
            }
            if (isCreate){
                fs.saveToFile(fileName);
            }
        }
        fs.close();
    }

}
