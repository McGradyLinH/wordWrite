package com.test.controller;

import org.springframework.util.DigestUtils;
public class Test {

    public static void main(String[] args) {
        System.out.println(DigestUtils.md5DigestAsHex("123".getBytes()));
    }

//    public static void main(String[] args) {
//        String result = "{\"code\":\"0\",\"data\":{\"block\":[{\"type\":\"text\",\"line\":[{\"confidence\":1,\"word\":[{\"content\":\"The\"},{\"content\":\"terrible\"},{\"content\":\"earthquake\"},{\"content\":\"and\"},{\"content\":\"the\"},{\"content\":\"following\"},{\"content\":\"tsunami\"}]},{\"confidence\":1,\"word\":[{\"content\":\"have\"},{\"content\":\"caused\"},{\"content\":\"so\"},{\"content\":\"many\"},{\"content\":\"people's\"},{\"content\":\"injuries\"},{\"content\":\"and\"},{\"content\":\"deaths.\"}]},{\"confidence\":1,\"word\":[{\"content\":\"Besides,\"},{\"content\":\"many\"},{\"content\":\"citizens\"},{\"content\":\"become\"},{\"content\":\"homeless.\"},{\"content\":\"We\"},{\"content\":\"chinese\"},{\"content\":\"feel\"}]},{\"confidence\":1,\"word\":[{\"content\":\"greatly\"},{\"content\":\"sorry\"},{\"content\":\"for\"},{\"content\":\"the\"},{\"content\":\"pains\"},{\"content\":\"you\"},{\"content\":\"are\"},{\"content\":\"suffering.\"},{\"content\":\"And\"},{\"content\":\"I\"}]},{\"confidence\":1,\"word\":[{\"content\":\"do\"},{\"content\":\"hope\"},{\"content\":\"you\"},{\"content\":\"can\"},{\"content\":\"overcome\"},{\"content\":\"those\"},{\"content\":\"great\"},{\"content\":\"difficulties.\"}]},{\"confidence\":1,\"word\":[{\"content\":\"Please\"},{\"content\":\"remember\"},{\"content\":\"Japan\"},{\"content\":\"isn't\"},{\"content\":\"alone.People\"},{\"content\":\"all\"},{\"content\":\"over\"},{\"content\":\"the\"}]},{\"confidence\":1,\"word\":[{\"content\":\"world\"},{\"content\":\"are\"},{\"content\":\"lending\"},{\"content\":\"hands\"},{\"content\":\"to\"},{\"content\":\"the\"},{\"content\":\"areas\"},{\"content\":\"struck\"},{\"content\":\"by\"},{\"content\":\"tsunam\"}]},{\"confidence\":1,\"word\":[{\"content\":\"and\"},{\"content\":\"earthquake.Now\"},{\"content\":\"that\"},{\"content\":\"the\"},{\"content\":\"disaster\"},{\"content\":\"has\"},{\"content\":\"happened\"}]},{\"confidence\":1,\"word\":[{\"content\":\"we\"},{\"content\":\"should\"},{\"content\":\"face\"},{\"content\":\"it\"},{\"content\":\"bravely\"},{\"content\":\"and\"},{\"content\":\"rebuild\"},{\"content\":\"the\"},{\"content\":\"homes.\"},{\"content\":\"thus\"}]},{\"confidence\":1,\"word\":[{\"content\":\"creating\"},{\"content\":\"a\"},{\"content\":\"stronger\"},{\"content\":\"country.\"},{\"content\":\"Tomorrow\"},{\"content\":\"is\"},{\"content\":\"another\"},{\"content\":\"day.\"}]},{\"confidence\":1,\"word\":[{\"content\":\"All\"},{\"content\":\"the\"},{\"content\":\"things\"},{\"content\":\"will\"},{\"content\":\"be\"},{\"content\":\"better.\"}]}]}]},\"desc\":\"success\",\"sid\":\"wcr0018b0ae@gz6620127b2e6a463000\"}\n";
//        Map<String, Object> map = (Map<String, Object>) JSON.parse(result);
//        if (map.get("code").toString().equals("0")) {
//            StringBuilder content = new StringBuilder();
//            String data = map.get("data").toString();
//            Map<String, Object> map1 = (Map<String, Object>) JSON.parse(data);
//            String block = map1.get("block").toString();
//            List<Map> blocks = JSON.parseArray(block, Map.class);
//            Map map2 = blocks.get(0);
//            String line = map2.get("line").toString();
//            List<Map> lines = JSON.parseArray(line, Map.class);
//            for (Map line1 : lines) {
//                String word = line1.get("word").toString();
//                List<Map> words = JSON.parseArray(word, Map.class);
//                for (Map word1 : words) {
//                    content.append(word1.get("content") + " ");
//                }
//                content.append("\r\n");
//            }
//            System.out.println(content);
//
//        }
//
//    }

    /**
     * 去到学生上传文章页面
     *
     * @return
     */
//    @GetMapping("/stuUpload")
//    public ModelAndView imagerotate(Map<String, Object> map) {
//        return new ModelAndView("student/ImageRotate");
//    }
//
//    /**
//     * 上传文章并解析
//     *
//     * @param imageString 图片的base64码
//     * @param session
//     * @return
//     */
//    @PostMapping("/uploadEssay")
//    public String uploadEssay(HttpSession session, String imageString, Title title) {
//        PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
//        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
//        String filePath = "c:/word/uploadImage/" + student.getName() + "/";
//        //保存图片
//        Base64ToFile.base64ToFile(imageString, fileName, filePath);
//        //解析文字
//        try {
//            String jsonString = WebOcr.readFile(filePath + fileName);
//            String content = AnalyticText.analyticText(jsonString);
////            saveWrite(content, session, title);
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
//        return "success";
//    }

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

    //    @PostMapping("/saveWrite")
//    public ModelAndView saveWrite(String content, HttpSession session, String titleName,
//                                  MultipartFile titleImage, Integer teacherId) {
//        try {
//            PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
//            //减掉学生的批改数
//            int i1 = studentService.decrementSurplus(student);
//            if (i1 == 0) {
//                return new ModelAndView("redirect:/login");
//            }
//            content = content.replaceAll("(\r\n|\n)", "<w:br/>");
//            content = content.replaceAll(" ","&#160;");
//            content = content.replaceAll("\t","&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;");
//            System.err.println(content);
//            //传入word文档的值
//            Map<String, Object> map = new HashMap<>(3);
//            map.put("content", content);
//            map.put("img", "");
//            map.put("title", "");
//            String ftl = "write.ftl";
//            Essay essay = new Essay();
//            essay.setTitleName("小作文，无描述");
//            if (!"".equals(titleName)) {
//                map.put("title", titleName);
//                essay.setTitleName(titleName);
//            }
//            if (!titleImage.isEmpty()) {
//                ftl = "upload.ftl";
//                //上传图片的base64
//                String base64 = Base64.getEncoder().encodeToString(titleImage.getBytes());
//                map.put("image", base64);
//            }
//            String name = UUID.randomUUID().toString().replace("-", "");
//            String desSource = "c:/word/" + student.getName();
//            File desFile = new File(desSource);
//            if (!desFile.exists()) {
//                desFile.mkdirs();
//            }
//            desSource += "/write.doc";
//            desFile = new File(desSource);
//            //创建word文档
//            DocumentHandler.createDoc(map, desSource, ftl);
//            File file;
//            for (int i = 1; i <= 4; i++) {
//                //要保存的路径
//                file = new File("c:\\word\\" + student.getName() + "\\" + name + "_" + i + ".doc");
//                try {
//                    Files.copy(desFile.toPath(), file.toPath());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            essay.setName(name);
//            essay.setStudent(student);
//            PlatformUser enTeacher = new PlatformUser(teacherId);
//            essay.setEnTeacher(enTeacher);
//            //保存文件到数据库
//            studentService.insertEssay(essay);
//            //更改登录用户session中的文章数
//            student.setSurplus(student.getSurplus() - 1);
//            session.setAttribute("loginUser", student);
//        } catch (RuntimeException | IOException e) {
//            e.printStackTrace();
//        }
//        return new ModelAndView("redirect:/stuIndex");
//    }


    /**
     * 打开word文档
     *
     * @param index 文章的编号
     * @return
     */
//    @GetMapping(value = "/word")
//    public ModelAndView showWord(HttpServletRequest request, Map<String, Object> map, Integer index, HttpSession session) {
//        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
//        poCtrl.setServerPage("/poserver.zz");//设置服务页面
//        poCtrl.addCustomToolButton("保存", "Save", 1);//添加自定义保存按钮
//        poCtrl.setSaveFilePage("/save");//设置处理文件保存的请求方法
//        String docName = (String) session.getAttribute("docName");
//        String stuName = (String) session.getAttribute("stuName");
//        //打开word
//        PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
//        poCtrl.webOpen("c:\\word\\" + stuName + "\\" + docName + "_" + index + ".doc", OpenModeType.docAdmin, student.getName());
//        map.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));
//        ModelAndView mv = new ModelAndView("Word");
//        return mv;
//    }
//
//    /**
//     * word文档保存方法
//     */
//    @RequestMapping("/save")
//    public void saveFile(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//        FileSaver fs = new FileSaver(request, response);
//        String stuName = (String) session.getAttribute("stuName");
//        fs.saveToFile("c:\\word\\" + stuName + "\\" + fs.getFileName());
//        fs.close();
//    }
}
