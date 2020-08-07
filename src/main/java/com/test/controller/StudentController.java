package com.test.controller;

import com.test.domain.*;
import com.test.service.PlatformUserService;
import com.test.service.StudentService;
import com.test.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private PlatformUserService userService;

    /**
     * 去到登录页面
     *
     * @return
     */
    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("Login");
    }

    /**
     * 根据角色跳转
     *
     * @param phone    电话
     * @param password 密码
     * @return
     */
    @PostMapping("/login")
    public ModelAndView login(String phone, String password, HttpSession session,
                              Map<String, Object> map, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("redirect:/stuIndex");
        if (!CodeUtil.checkVerifyCode(request)) {
            map.put("msg", "验证码错误");
            modelAndView.setViewName("Login");
        } else {
            PlatformUser student = studentService.queryByPhone(phone);
            if (Objects.isNull(student) || !student.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
                map.put("msg", "用户名或密码错误");
                modelAndView.setViewName("Login");
                return modelAndView;
            }
            session.setAttribute("loginUser", student);
            switch (student.getRole()) {
                case 1:
                    break;
                case 2:
                case 3:
                    modelAndView.setViewName("redirect:/choose");
                    break;
                case 0:
                    modelAndView.setViewName("redirect:/users");
                    break;
                default:
                    modelAndView.setViewName("Login");
            }
        }
        return modelAndView;
    }

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
        List<Essay> list = studentService.queryEssayList(essayDto);
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
    public ModelAndView showIndex(String essayName, HttpSession session, String stuName,Integer versions) {
        ModelAndView mv = new ModelAndView("student/Stucheck");
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
    public ModelAndView saveWrite(String content, HttpSession session, String titleName,
                                  MultipartFile titleImage, Integer teacherId) {
        try {
            PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
            //减掉学生的批改数
//            int i1 = studentService.decrementSurplus(student);
//            if (i1 == 0) {
//                return new ModelAndView("redirect:/login");
//            }
//            StringBuilder str = new StringBuilder();
            //换行三个为一个整体
            //换行1
//            str.append(" </w:t></w:r></w:p><w:p><w:pPr></w:pPr>");
            //换行2
//            str.append("<w:r><w:rPr>");
            //设置样式  字体 大小 颜色
//            str.append("<w:rFonts w:ascii=\"Calibri\" w:fareast=\"Calibri\" w:h-ansi=\"宋体\"/>");
//            str.append("<w:color w:val=\"000000\"/><w:sz w:val=\"22\"/>");
            //换行3
//            str.append("</w:rPr><w:t>&#160;&#160;&#160;&#160;");
//            str.append("<w:br/>");
            content = content.replaceAll("(\r\n|\n)", "<w:br/>");
            content = content.replaceAll(" ","&#160;");
            content = content.replaceAll("\t","&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;");
            System.err.println(content);
            //传入word文档的值
            Map<String, Object> map = new HashMap<>(3);
            map.put("content", content);
            map.put("img", "");
            map.put("title", "");
            String ftl = "write.ftl";
            Essay essay = new Essay();
            essay.setTitleName("小作文，无描述");
            if (!"".equals(titleName)) {
                map.put("title", titleName);
                essay.setTitleName(titleName);
            }
            if (!titleImage.isEmpty()) {
                ftl = "upload.ftl";
                //上传图片的base64
                String base64 = Base64.getEncoder().encodeToString(titleImage.getBytes());
                map.put("image", base64);
            }
            String name = UUID.randomUUID().toString().replace("-", "");
            String desSource = "c:/word/" + student.getName();
            File desFile = new File(desSource);
            if (!desFile.exists()) {
                desFile.mkdirs();
            }
            desSource += "/write.doc";
            desFile = new File(desSource);
            //创建word文档
            DocumentHandler.createDoc(map, desSource, ftl);
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
            essay.setName(name);
            essay.setStudent(student);
            PlatformUser enTeacher = new PlatformUser(teacherId);
            essay.setEnTeacher(enTeacher);
            //保存文件到数据库
            studentService.insertEssay(essay);
            //更改登录用户session中的文章数
            student.setSurplus(student.getSurplus() - 1);
            session.setAttribute("loginUser", student);
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
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
        essay.setStatus(2);
        essay.setName(docName);
        int i = studentService.updateEssay(essay);
        if (i == 0) {
            return "fail";
        }
        return "success";
    }

}
