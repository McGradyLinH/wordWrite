package com.test.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class DocumentHandler {

    private static final Configuration configuration = new Configuration();

    static {
        configuration.setDefaultEncoding("utf-8");
    }

    public static void createDoc(Map<String, Object> dataMap, String fileName) {
        //需要导出模板的包路径
        configuration.setClassForTemplateLoading(DocumentHandler.class, "/word");
        Template t = null;
        try {
            t = configuration.getTemplate("write.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }
        File outFile = new File(fileName);
        Writer out = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outFile);
            OutputStreamWriter oWriter = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            out = new BufferedWriter(oWriter);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        try {
            assert t != null;
            t.process(dataMap, out);
            assert out != null;
            out.close();
            fos.close();
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }

    }
}