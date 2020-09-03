package com.test.pay.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.domain.PlatformUser;
import com.test.pay.pay.entity.dto.NotifyDTO;
import com.test.pay.util.HttpsUtils;
import com.test.pay.util.PayjsConfig;
import com.test.service.OrderService;
import com.test.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/pay")
public class PayController {
    private static final String COUPON_CODE = "save14";
    @Autowired
    private OrderService orderService;

    @Autowired
    private StudentService studentService;

    /**
     * native
     *
     * @param essayNumber 购买几篇文章
     * @param coupon      优惠码
     */
    @RequestMapping("/native")
    @ResponseBody
    public ModelAndView Native(Integer essayNumber, String coupon) throws IOException {
        Map<String, String> payData = new HashMap<>();
        BigDecimal num = new BigDecimal("" + essayNumber * 100);
        BigDecimal price = new BigDecimal("69");
        BigDecimal totalPrice = num.multiply(price);
        if (!StringUtils.isEmpty(coupon) && COUPON_CODE.equalsIgnoreCase(coupon)) {
            BigDecimal discount = new BigDecimal("0.86");
            totalPrice = totalPrice.multiply(discount).setScale(BigDecimal.ROUND_HALF_UP, 2);
        }
        payData.put("mchid", PayjsConfig.mchid);
//        payData.put("total_fee", totalPrice.toString());
        payData.put("total_fee", "1");
        String out_trade_no = "" + System.currentTimeMillis();
        // 订单号
        payData.put("out_trade_no", out_trade_no);
        payData.put("body", "订单标题");
//        payData.put("type", "alipay");
        payData.put("attach", ""+essayNumber);
        payData.put("notify_url", "http://127.0.0.1:8080/api/pay/notify");
        // 进行sign签名
        payData.put("sign", sign(payData, PayjsConfig.key));
        // 请求payjs获取二维码
        String result = HttpsUtils.sendPost(PayjsConfig.nativeUrl, JSON.toJSONString(payData), null);
        // 接口返回二维码数据
        ModelAndView modelAndView = new ModelAndView("student/QRCode");
        JSONObject jsonObject = JSON.parseObject(result);
        //添加订单
        NotifyDTO notifyDTO = new NotifyDTO();
        notifyDTO.setOut_trade_no(out_trade_no);
        notifyDTO.setTotal_fee(totalPrice.toString());
        orderService.insertOrder(notifyDTO);
        System.err.println(jsonObject.toString());
        modelAndView.addObject("qrcode", jsonObject.get("qrcode"));
        return modelAndView;
    }

    /**
     * jsapi
     */
    @RequestMapping("/jsapi")
    @ResponseBody
    public Object Jsapi() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        Map<String, String> payData = new HashMap<>();
        payData.put("mchid", PayjsConfig.mchid);
        payData.put("total_fee", "100");
        payData.put("out_trade_no", "83432749"); // 订单号 随便输的，自己生成一下就好了
        payData.put("openid", "xxxxxxxxxxxxxx"); // 根据openid接口获取到的openid
        payData.put("body", "订单标题");
        payData.put("attach", "自定义数据");
        payData.put("notify_url", "https://你的域名/api/pay/notify");

        // 进行sign签名
        payData.put("sign", sign(payData, PayjsConfig.key));

        // 请求payjs
        String result = HttpsUtils.sendPost(PayjsConfig.jsapiUrl, JSON.toJSONString(payData), null);

        // 接口返回数据
        return JSON.parseObject(result);
    }

    /**
     * check
     */
    @RequestMapping("/check")
    @ResponseBody
    public Object Check() throws NoSuchAlgorithmException, KeyManagementException, IOException {

        Map<String, String> payData = new HashMap<>();
        payData.put("mchid", PayjsConfig.mchid);
        payData.put("payjs_order_id", "83432749"); // payjs订单号


        // 进行sign签名
        payData.put("sign", sign(payData, PayjsConfig.key));

        // 请求payjs
        String result = HttpsUtils.sendPost(PayjsConfig.checkUrl, JSON.toJSONString(payData), null);

        // 接口返回数据
        return JSON.parseObject(result);
    }

    /**
     * close 关闭订单
     */
    @RequestMapping("/close")
    @ResponseBody
    public Object Close() throws NoSuchAlgorithmException, KeyManagementException, IOException {

        Map<String, String> payData = new HashMap<>();
        payData.put("mchid", PayjsConfig.mchid);
        payData.put("payjs_order_id", "83432749"); // payjs订单号

        // 进行sign签名
        payData.put("sign", sign(payData, PayjsConfig.key));

        // 请求payjs
        String result = HttpsUtils.sendPost(PayjsConfig.closeUrl, JSON.toJSONString(payData), null);

        // 接口返回数据
        return JSON.parseObject(result);
    }

    /**
     * reverse 撤销订单
     */
    @RequestMapping("/reverse")
    @ResponseBody
    public Object Reverse() throws NoSuchAlgorithmException, KeyManagementException, IOException {

        Map<String, String> payData = new HashMap<>();
        payData.put("mchid", PayjsConfig.mchid);
        payData.put("payjs_order_id", "83432749"); // payjs订单号

        // 进行sign签名
        payData.put("sign", sign(payData, PayjsConfig.key));

        // 请求payjs
        String result = HttpsUtils.sendPost(PayjsConfig.reverseUrl, JSON.toJSONString(payData), null);

        // 接口返回数据
        return JSON.parseObject(result);
    }

    /**
     * refund 订单退款
     */
    @RequestMapping("/refund")
    @ResponseBody
    public Object Refund(String payjs_order_id) throws NoSuchAlgorithmException, KeyManagementException, IOException {

        Map<String, String> payData = new HashMap<>();
        payData.put("mchid", PayjsConfig.mchid);
        payData.put("payjs_order_id", payjs_order_id); // payjs订单号

        // 进行sign签名
        payData.put("sign", sign(payData, PayjsConfig.key));

        // 请求payjs
        String result = HttpsUtils.sendPost(PayjsConfig.refundUrl, JSON.toJSONString(payData), null);

        // 接口返回数据
        return JSON.parseObject(result);
    }

    /**
     * info 商户资料
     */
    @RequestMapping("/info")
    @ResponseBody
    public Object Info() throws NoSuchAlgorithmException, KeyManagementException, IOException {

        Map<String, String> payData = new HashMap<>();
        payData.put("mchid", PayjsConfig.mchid);

        // 进行sign签名
        payData.put("sign", sign(payData, PayjsConfig.key));

        // 请求payjs
        String result = HttpsUtils.sendPost(PayjsConfig.infoUrl, JSON.toJSONString(payData), null);

        // 接口返回数据
        return JSON.parseObject(result);
    }

    /**
     * complaint 投诉查询
     */
    @RequestMapping("/complaint")
    @ResponseBody
    public Object Complaint() throws NoSuchAlgorithmException, KeyManagementException, IOException {

        Map<String, String> payData = new HashMap<>();
        payData.put("mchid", PayjsConfig.mchid);

        // 进行sign签名
        payData.put("sign", sign(payData, PayjsConfig.key));

        // 请求payjs
        String result = HttpsUtils.sendPost(PayjsConfig.complaintUrl, JSON.toJSONString(payData), null);

        // 接口返回数据
        return JSON.parseObject(result);
    }

    /**
     * bank 银行查询
     */
    @RequestMapping("/bank")
    @ResponseBody
    public Object Bank() throws NoSuchAlgorithmException, KeyManagementException, IOException {

        Map<String, String> payData = new HashMap<>();
        payData.put("mchid", PayjsConfig.mchid);
        payData.put("bank", "xxxxxx"); // 银行简称

        // 进行sign签名
        payData.put("sign", sign(payData, PayjsConfig.key));

        // 请求payjs
        String result = HttpsUtils.sendPost(PayjsConfig.bankUrl, JSON.toJSONString(payData), null);

        // 接口返回数据
        return JSON.parseObject(result);
    }

    /**
     * 异步通知
     * @param notifyDTO
     * @return
     */
    @PostMapping("/notify")
    @ResponseBody
    public Object payjsNotify(NotifyDTO notifyDTO, HttpSession session) {
        System.out.println(11111);
        Map<String, String> notifyData = new HashMap<>();
        notifyData.put("return_code", notifyDTO.getReturn_code());
        notifyData.put("total_fee", notifyDTO.getTotal_fee());
        //订单号
        notifyData.put("out_trade_no", notifyDTO.getOut_trade_no());
        notifyData.put("payjs_order_id", notifyDTO.getPayjs_order_id());
        notifyData.put("transaction_id", notifyDTO.getTransaction_id());
        notifyData.put("time_end", notifyDTO.getTime_end());
        notifyData.put("openid", notifyDTO.getOpenid());
        notifyData.put("attach", notifyDTO.getAttach());
        notifyData.put("mchid", notifyDTO.getMchid());
        System.out.println(notifyDTO);
        String sign = sign(notifyData, PayjsConfig.key);
        if (sign.equals(notifyDTO.getSign())) {
            System.out.println(notifyDTO);
            // 验签通过，这里修改订单状态
            orderService.updateOrder(notifyDTO);
            //登录学生
            PlatformUser student = (PlatformUser) session.getAttribute("loginUser");
            student.setSurplus(Integer.valueOf(notifyDTO.getAttach()));
            studentService.incrementSurplus(student);
            return "success";
        }

        return "failure";
    }


    //签名算法
    public String sign(Map<String, String> params, String secret) {
        String sign = "";
        StringBuilder sb = new StringBuilder();
        //step1：先对请求参数排序
        Set<String> keyset = params.keySet();
        TreeSet<String> sortSet = new TreeSet<String>();
        sortSet.addAll(keyset);
        Iterator<String> it = sortSet.iterator();
        //step2：把参数的key value链接起来 secretkey放在最后面，得到要加密的字符串
        while (it.hasNext()) {
            String key = it.next();
            String value = params.get(key);
            sb.append(key).append("=").append(value).append("&");
        }
        sb.append("key=").append(secret);
        byte[] md5Digest;
        try {
            //得到Md5加密得到sign
            md5Digest = getMD5Digest(sb.toString());
            sign = byte2hex(md5Digest);
        } catch (IOException e) {
            System.out.println("生成签名错误" + e);
        }
        return sign;
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    private static byte[] getMD5Digest(String data) throws IOException {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data.getBytes("UTF-8"));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse);
        }
        return bytes;
    }

}
