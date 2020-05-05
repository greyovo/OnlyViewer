package display.java.model;

import lombok.Getter;
import org.junit.Test;

import java.io.*;
import java.net.URLEncoder;

/**
 * @ProjName: OnlyViewer
 * @ClassName: Util
 * @Author: Kevin
 * @Time: 2020/4/7 21:16
 * @Describe: OCR
 **/

public class Ocr extends GenAIP{
    /**
     * 三十日更新一次
     * Latest: 2020/4/27
     */
    private static final String API_KEY = "mOmi2LXLqhls75BVZLGY4nr5";
    private static final String SECRET_KEY = "ShPdHkVbpHcgiexcZfA6SV9XkkGxCDsI";

    // 中英文模式 mode
    public static final int ENG = 0;
    public static final int CHI = 1;

    // 每天可识别50000次
    // mode默认情况为ENG，模式指的是使用该模式下的标点符号，非识别中英文参数
    // mode可选参数 Ocr.ENG / Ocr.CHI
    public static String doOcr(String path, int mode) {
        String re = OCR(path, mode);
        if (re == null) {
            System.out.println("识别失败！");
        }
        return re;
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
            byte[] imgData = GenAIP.readFileByBytes(filePath);
            String imgStr = GenAIP.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            // Body部分放置请求参数
            // 可自动检测图像朝向
            String param = "image=" + imgParam + "&detect_direction=true";

            // 此获取Token方法每三十日需要更新一次
            String accessToken = GenAIP.getAuth(API_KEY, SECRET_KEY);

            if (mode == 0)
                return GenAIP.post(url, accessToken, param);
            if (mode == 1)
                return GenAIP.post(url, accessToken, param).replace(";", "；").
                        replace(",", "，").replace("(", "（").
                        replace(")", "）").replace(":", "：");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void Test() {
        // 测试样例
        String path1 = "src/display/java/model/1.jpg";

        // 改变参数
        String choose = path1;
        File file = new File(choose);
        if (!file.exists()) {
            System.out.println("图片不存在!");
        }
        String s = doOcr(choose, Ocr.ENG);
        System.out.println(s);
    }
}
