package onlyviewer.display.java.model;

import java.io.*;
import java.net.URLEncoder;

/**
 * OCR文本识别
 *
 * @author Kevin
 * @since 2020/4/26
 **/

public class Ocr extends GenAIP {
    // 需要您自行前往百度AI -> 控制台 -> 文字识别 -> 添加应用获取API_KEY与SECRET_KEY
    private static final String API_KEY = "";
    private static final String SECRET_KEY = "";

    // 中英文模式 mode
    public static final int ENG = 0;
    public static final int CHI = 1;

    // 每天可识别50000次
    // mode默认情况为ENG，模式指的是使用该模式下的标点符号，非识别中英文参数
    // mode可选参数 Ocr.ENG / Ocr.CHI
    public static String doOcr(String imagePath, int mode) {
        String beforeName = imagePath.substring(0, imagePath.lastIndexOf("."));
        String afterName = imagePath.substring(imagePath.lastIndexOf("."));
        String newImagePath = beforeName + "_only" + afterName;
        File image = new File(newImagePath);
        String result;
        if (API_KEY.length() == 0 && SECRET_KEY.length() == 0) {
            result ="抱歉给您带来不便（＞人＜；）\n\n" +
                    "请您自行前往百度AI -> 控制台 -> 文字识别 -> 添加应用获取API_KEY与SECRET_KEY并更新代码\n\n" +
                    "https://ai.baidu.com/";
                    ;
        } else {
            if (image.exists()) {
                result = OCR(newImagePath, mode);
            } else {
                result = OCR(imagePath, mode);
            }
            if (result == null) {
                result = "图中无可识别文字";
            }
            if (result.equals("Expired")) {
                result = "请联系作者更新Access Token!";
            }
        }
        return result;
    }


    /**
     * 步骤：
     * 请求图片需经过base64编码及urlencode后传入
     * 图片的base64编码指将一副图片数据编码成一串字符串
     * 使用该字符串代替图像地址
     */
    private static String OCR(String filePath, int mode) {
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
        try {
            byte[] imgData = readFileByBytes(filePath);
            String imgStr = encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            // Body部分放置请求参数
            // 可自动检测图像朝向
            String param = "image=" + imgParam + "&detect_direction=true";

            String accessToken = getAuth(API_KEY, SECRET_KEY);

            if (mode == 0)
                return post(url, accessToken, param);
            if (mode == 1)
                return post(url, accessToken, param).replace(";", "；").
                        replace(",", "，").replace("(", "（").
                        replace(")", "）").replace(":", "：");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
