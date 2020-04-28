package display.java.model;

import com.baidu.aip.imageclassify.AipImageClassify;
import lombok.Getter;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @ProjName: OnlyViewer
 * @ClassName: AIP
 * @Author: Kevin
 * @Time:2020/4/27 0:10
 * @Describe: baidu
 **/

public class AIP {
    /**
     * 三十日更新一次
     * Latest: 2020/4/27
     */
    private static final String API_KEY = "mOmi2LXLqhls75BVZLGY4nr5";
    private static final String SECRET_KEY = "ShPdHkVbpHcgiexcZfA6SV9XkkGxCDsI";

    // 中英文模式 mode
    @Getter
    private static final int ENG = 0;
    @Getter
    private static final int CHI = 1;

    /**
     * 步骤：
     * 请求图片需经过base64编码及urlencode后传入
     * 图片的base64编码指将一副图片数据编码成一串字符串
     * 使用该字符串代替图像地址
     */
    static String OCR(String filePath, int mode) {
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
        try {
            byte[] imgData = GenAIP.readFileByBytes(filePath);
            String imgStr = GenAIP.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

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
}
