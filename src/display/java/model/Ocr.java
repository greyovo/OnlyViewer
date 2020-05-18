package display.java.model;

import home.java.model.SelectedModel;
import org.junit.Test;

import java.io.*;
import java.net.URLEncoder;

/**
 * @author  Kevin
 * @since   2020/4
 * OCR
 **/

public class Ocr extends GenAIP {
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
    public static String doOcr(String imagePath, int mode) {
        boolean flag = false;
        String beforeName = imagePath.substring(0, imagePath.lastIndexOf("."));
        String afterName = imagePath.substring(imagePath.lastIndexOf("."));
        String newImagePath = beforeName + "_only" + afterName;
        System.out.println(newImagePath);
        File image = new File(newImagePath);
        String result = null;
        if (image.exists()) {
            flag = true;
            result = OCR(newImagePath, mode);
            if (result == null) {
                result = "图中无可识别文字";
            }
        } else {
            SelectedModel.setSourcePath(imagePath);
            if (SelectedModel.compressImage(800) != 0)
                result = OCR(newImagePath, mode);
            else {
                result = OCR(imagePath, mode);
                flag = true;
            }
            if (result == null) {
                result = "图中无可识别文字";
            }
        }
        if (!flag) {
            SelectedModel.setSourcePath(newImagePath);
            if (SelectedModel.deleteImage() != 0)
                System.out.println("删除压缩图片");
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

            // 此获取Token方法每三十日需要更新一次
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

    @Test
    public void Test() {
        System.out.println("This is a Test");
    }
}
