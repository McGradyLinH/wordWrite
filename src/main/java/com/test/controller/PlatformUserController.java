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
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    //一页的默认大小
    private static final Integer NUMS = 10;

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
        if (Objects.isNull(pages)) {
            pages = 1;
        }
        PageHelper.startPage(pages, NUMS);
        List<PlatformUser> users = userService.queryUsers();
        PageInfo<PlatformUser> pageInfo = new PageInfo<>(users);

        map.put("users", users);
        map.put("pages", pageInfo);
        return "background/PlatformUser";
    }

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public String addUser(PlatformUser user, Map<String, Object> map,
                          HttpServletRequest request, MultipartFile avatar) {
        if (!CodeUtil.checkVerifyCode(request)) {
            map.put("msg", "验证码错误！");
            return "background/add";
        }
        PlatformUser user1 = userService.checkUser(user);
        if (!Objects.isNull(user1)) {
            map.put("msg", "该手机号已经注册！");
            return "background/add";
        }
        String property = System.getProperty("user.dir") + "/static/images/";
        File file = new File(property + "test.jpg");
        try {
            System.out.println(file.createNewFile());
            avatar.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String password = user.getPassword();
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        //int i = userService.registerUser(user);
        int i = 0;
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
