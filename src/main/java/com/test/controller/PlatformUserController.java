package com.test.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.domain.PlatformUser;
import com.test.service.PlatformUserService;
import com.test.service.StudentService;
import com.test.util.CodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author LX
 */
@Controller
public class PlatformUserController {
    @Autowired
    private PlatformUserService userService;

    @Autowired
    private StudentService studentService;

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

    @GetMapping("/users")
    public String user(Map<String, Object> map, Integer pages, HttpSession session) {
        PlatformUser admin = (PlatformUser) session.getAttribute("loginUser");
        if (admin.getRole() != 0) {
            switch (admin.getRole()) {
                case 1:
                    return "redirect:/stuIndex";
                case 2:
                case 3:
                    return "redirect:/choose";
                default:
                    return "redirect:/login";
            }
        }
        return "background/PlatformUser";
    }

    @GetMapping("/getUsers")
    @ResponseBody
    public Map<String, Object> getUsers(int pageSize, int pageIndex) {
        Map<String, Object> resultMap = new HashMap<>(2);
        //开始分页
        PageHelper.startPage(pageIndex, pageSize);
        List<PlatformUser> users = userService.queryUsers();
        PageInfo<PlatformUser> pageInfo = new PageInfo<>(users);
        resultMap.put("rows", pageInfo.getList());
        resultMap.put("total", pageInfo.getTotal());
        return resultMap;
    }

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public String addUser(PlatformUser user, Map<String, Object> map,
                          HttpServletRequest request) {
        if (!CodeUtil.checkVerifyCode(request)) {
            map.put("msg", "验证码错误！");
            return "background/add";
        }
        PlatformUser user1 = userService.checkUser(user);
        if (!Objects.isNull(user1)) {
            map.put("msg", "该手机号已经注册！");
            return "background/add";
        }
        String password = user.getPassword();
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        int i = userService.registerUser(user);
        if (i == 0) {
            map.put("msg", "提交失败，请稍后重试！");
            return "background/add";
        }
        return "redirect:/login";
    }

    @GetMapping("/addUser")
    public String addUser() {
        return "background/add";
    }

}
