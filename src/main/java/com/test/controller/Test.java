package com.test.controller;

import com.alibaba.fastjson.JSON;
import com.test.domain.PlatformUser;
import com.test.domain.Title;
import com.test.util.AnalyticText;
import com.test.util.Base64ToFile;
import com.test.util.WebOcr;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Test {

    public static void main(String[] args) {
        String result = "{\"code\":\"0\",\"data\":{\"block\":[{\"type\":\"text\",\"line\":[{\"confidence\":1,\"word\":[{\"content\":\"The\"},{\"content\":\"terrible\"},{\"content\":\"earthquake\"},{\"content\":\"and\"},{\"content\":\"the\"},{\"content\":\"following\"},{\"content\":\"tsunami\"}]},{\"confidence\":1,\"word\":[{\"content\":\"have\"},{\"content\":\"caused\"},{\"content\":\"so\"},{\"content\":\"many\"},{\"content\":\"people's\"},{\"content\":\"injuries\"},{\"content\":\"and\"},{\"content\":\"deaths.\"}]},{\"confidence\":1,\"word\":[{\"content\":\"Besides,\"},{\"content\":\"many\"},{\"content\":\"citizens\"},{\"content\":\"become\"},{\"content\":\"homeless.\"},{\"content\":\"We\"},{\"content\":\"chinese\"},{\"content\":\"feel\"}]},{\"confidence\":1,\"word\":[{\"content\":\"greatly\"},{\"content\":\"sorry\"},{\"content\":\"for\"},{\"content\":\"the\"},{\"content\":\"pains\"},{\"content\":\"you\"},{\"content\":\"are\"},{\"content\":\"suffering.\"},{\"content\":\"And\"},{\"content\":\"I\"}]},{\"confidence\":1,\"word\":[{\"content\":\"do\"},{\"content\":\"hope\"},{\"content\":\"you\"},{\"content\":\"can\"},{\"content\":\"overcome\"},{\"content\":\"those\"},{\"content\":\"great\"},{\"content\":\"difficulties.\"}]},{\"confidence\":1,\"word\":[{\"content\":\"Please\"},{\"content\":\"remember\"},{\"content\":\"Japan\"},{\"content\":\"isn't\"},{\"content\":\"alone.People\"},{\"content\":\"all\"},{\"content\":\"over\"},{\"content\":\"the\"}]},{\"confidence\":1,\"word\":[{\"content\":\"world\"},{\"content\":\"are\"},{\"content\":\"lending\"},{\"content\":\"hands\"},{\"content\":\"to\"},{\"content\":\"the\"},{\"content\":\"areas\"},{\"content\":\"struck\"},{\"content\":\"by\"},{\"content\":\"tsunam\"}]},{\"confidence\":1,\"word\":[{\"content\":\"and\"},{\"content\":\"earthquake.Now\"},{\"content\":\"that\"},{\"content\":\"the\"},{\"content\":\"disaster\"},{\"content\":\"has\"},{\"content\":\"happened\"}]},{\"confidence\":1,\"word\":[{\"content\":\"we\"},{\"content\":\"should\"},{\"content\":\"face\"},{\"content\":\"it\"},{\"content\":\"bravely\"},{\"content\":\"and\"},{\"content\":\"rebuild\"},{\"content\":\"the\"},{\"content\":\"homes.\"},{\"content\":\"thus\"}]},{\"confidence\":1,\"word\":[{\"content\":\"creating\"},{\"content\":\"a\"},{\"content\":\"stronger\"},{\"content\":\"country.\"},{\"content\":\"Tomorrow\"},{\"content\":\"is\"},{\"content\":\"another\"},{\"content\":\"day.\"}]},{\"confidence\":1,\"word\":[{\"content\":\"All\"},{\"content\":\"the\"},{\"content\":\"things\"},{\"content\":\"will\"},{\"content\":\"be\"},{\"content\":\"better.\"}]}]}]},\"desc\":\"success\",\"sid\":\"wcr0018b0ae@gz6620127b2e6a463000\"}\n";
        Map<String, Object> map = (Map<String, Object>) JSON.parse(result);
        if (map.get("code").toString().equals("0")) {
            StringBuilder content = new StringBuilder();
            String data = map.get("data").toString();
            Map<String, Object> map1 = (Map<String, Object>) JSON.parse(data);
            String block = map1.get("block").toString();
            List<Map> blocks = JSON.parseArray(block, Map.class);
            Map map2 = blocks.get(0);
            String line = map2.get("line").toString();
            List<Map> lines = JSON.parseArray(line, Map.class);
            for (Map line1 : lines) {
                String word = line1.get("word").toString();
                List<Map> words = JSON.parseArray(word, Map.class);
                for (Map word1 : words) {
                    content.append(word1.get("content") + " ");
                }
                content.append("\r\n");
            }
            System.out.println(content);

        }

    }

    /**
     * 去到学生上传文章页面
     *
     * @return
     */
    @GetMapping("/stuUpload")
    public ModelAndView imagerotate(Map<String, Object> map) {
        return new ModelAndView("student/ImageRotate");
    }

    /**
     * 上传文章并解析
     *
     * @param imageString 图片的base64码
     * @param session
     * @return
     */
    @PostMapping("/uploadEssay")
    public String uploadEssay(HttpSession session, String imageString, Title title) {
        PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
        String filePath = "c:/word/uploadImage/" + student.getName() + "/";
        //保存图片
        Base64ToFile.base64ToFile(imageString, fileName, filePath);
        //解析文字
        try {
            String jsonString = WebOcr.readFile(filePath + fileName);
            String content = AnalyticText.analyticText(jsonString);
//            saveWrite(content, session, title);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return "success";
    }
}
