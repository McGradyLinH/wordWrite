package com.test.controller;

import com.test.domain.Essay;
import com.test.domain.EssayDto;
import com.test.domain.PlatformUser;
import com.test.domain.Title;
import com.test.service.StudentService;
import com.test.util.DocumentHandler;
import com.zhuozhengsoft.pageoffice.DocumentVersion;
import com.zhuozhengsoft.pageoffice.FileSaver;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    /**
     * 去到学生上传文章页面
     * @return
     */
    @GetMapping("/stuUpload")
    public ModelAndView imagerotate(){
        return new ModelAndView("student/ImageRotate");
    }

    /**
     * 上传文章并解析
     * @param essayFile
     * @param session
     * @return
     */
    @PostMapping("/uploadEssay")
    public String uploadEssay(MultipartFile essayFile,HttpSession session){



        return null;
    }

    /**
     * 去到登录页面
     * @return
     */
    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("Login");
    }

    /**
     * 根据角色跳转
     * @param phone 电话
     * @param password 密码
     * @return
     */
    @PostMapping("/login")
    public ModelAndView login(String phone, String password, HttpSession session, Map<String, Object> map) {
        PlatformUser student = studentService.queryByPhone(phone);
        ModelAndView modelAndView = new ModelAndView("redirect:/stuIndex");
        if (Objects.isNull(student) || !student.getPassword().equals(password)) {
            map.put("msg", "用户名或密码错误");
            modelAndView.setViewName("Login");
        }
        session.setAttribute("loginUser", student);
        switch (student.getRole()) {
            case 1:
                break;
            case 2:
            case 3:
                modelAndView.setViewName("redirect:/choose");
                break;
            default:
                modelAndView.setViewName("Login");
        }
        return modelAndView;
    }

    /**
     * 学生首页
     * @return
     */
    @GetMapping("/stuIndex")
    public ModelAndView stuIndex(HttpSession session, Map<String, Object> map) {
        //登录学生
        PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
        Integer studentId = student.getId();
        EssayDto essayDto = new EssayDto();
        essayDto.setStuId(studentId);
        List<Essay> list = studentService.queryEssayList(essayDto);
        map.put("essays", list);
        return new ModelAndView("student/Index");
    }

    /**
     * 去到学生查看文章页面
     * @param essayName 文件名称
     * @param stuName   学生姓名
     * @return
     */
    @GetMapping(value = "/stucheck")
    public ModelAndView showIndex(String essayName, HttpSession session, String stuName) {
        ModelAndView mv = new ModelAndView("student/Stucheck");
        session.setAttribute("docName", essayName);
        session.setAttribute("stuName", stuName);
        return mv;
    }

    /**
     * 学生写作页面
     * @return
     */
    @GetMapping("/stuWrite")
    public ModelAndView stuWrite() {
        return new ModelAndView("student/Write");
    }

    /**
     * 学生提交写的作文
     * @param content 文章的内容
     * @param title 文章的标题
     * @return
     */
    @PostMapping("/saveWrite")
    public String saveWrite(String content, HttpSession session, Title title) {
        try {
            StringBuilder str = new StringBuilder();
            //换行三个为一个整体
            //换行1
            str.append(" </w:t></w:r></w:p><w:p><w:pPr></w:pPr>");
            //换行2
            str.append("<w:r><w:rPr>");
            //设置样式  字体 大小 颜色
            str.append("<w:rFonts w:ascii=\"Calibri\" w:fareast=\"Calibri\" w:h-ansi=\"宋体\"/>");
            str.append("<w:color w:val=\"000000\"/><w:sz w:val=\"22\"/>");
            //换行3
            str.append(" </w:rPr><w:t>");
            content = content.replaceAll("(\r\n|\n)", str.toString());

            PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
            Map<String, Object> map = new HashMap<>();
            map.put("content", content);
            map.put("title", title.getTitleName());
            String name = UUID.randomUUID().toString().replace("-", "");
            String desSource = "c:/word/" + student.getName();
            File desFile = new File(desSource);
            if (!desFile.exists()) {
                desFile.mkdirs();
            }
            desSource += "/write.doc";
            desFile = new File(desSource);
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
            Essay essay = new Essay();
            essay.setName(name);
            essay.setStatus(1);
            essay.setStudent(student);
            essay.setTitle(title);
            //保存文件到数据库
            studentService.insertEssay(essay);
            //减掉学生的批改数
            studentService.decrementSurplus(student);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    /**
     * 检查剩余文章数
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

    //下面两个是保留方法
//    @GetMapping("/toWrite")
//    public ModelAndView toWrite(HttpServletRequest request, Map<String, Object> map) {
//        HttpSession session = request.getSession();
//        String name = UUID.randomUUID().toString().replace("-", "");
//        //文件名称
//        session.setAttribute("name", name);
//        PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
//        Essay essay = new Essay();
//        essay.setName(name);
//        essay.setStatus(1);
//        essay.setStudent(student);
//        //保存文件到数据库
//        studentService.insertEssay(essay);
//        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
//        poCtrl.setServerPage("/poserver.zz");//设置服务页面
//        poCtrl.addCustomToolButton("保存", "Save", 1);//添加自定义保存按钮
//        poCtrl.setSaveFilePage("/writeSave");//设置处理文件保存的请求方法
//        //打开word
//        ModelAndView mv = new ModelAndView("Word");
//        //创建word
//        poCtrl.webCreateNew(student.getName(), DocumentVersion.Word2003);
//        map.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));
//        map.put("name", student.getName());
//        return mv;
//    }
//
//    @RequestMapping("/writeSave")
//    public void saveFile(HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession();
//        PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
//        FileSaver fs = new FileSaver(request, response);
//        String fileName = "";
//        File file;
//        boolean isCreate = false;
//        //为每一个学生创建一个文件夹
//        file = new File("c:\\word\\" + student.getName() + "");
//        if (!file.exists()) {
//            file.mkdir();
//        }
//        String name = (String) session.getAttribute("name");
//
//        for (int i = 1; i <= 4; i++) {
//            fileName = "c:\\word\\" + student.getName() + "\\" + name + "_" + i + ".doc";
//            file = new File(fileName);
//            if (!file.exists()) {
//                try {
//                    isCreate = file.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                fs.saveToFile(fileName);
//            }
//            if (isCreate) {
//                fs.saveToFile(fileName);
//            }
//        }
//        fs.close();
//    }

}
