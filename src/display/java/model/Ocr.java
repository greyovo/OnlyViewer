package display.java.model;

import org.junit.Test;

import java.io.*;

/**
 * @ProjName: OnlyViewer
 * @ClassName: Util
 * @Author: Kevin
 * @Time: 2020/4/7 21:16
 * @Describe: OCR
 **/

public class Ocr {
    // 每天可识别50000次
    // mode默认情况为ENG，模式指的是使用该模式下的标点符号，非识别中英文参数
    // mode可选参数 AIP.ENG / AIP.CHI
    public static String doOcr(String path, int mode) {
        String re = AIP.OCR(path, mode);
        if (re == null) {
            System.out.println("识别失败！");
        }
        return re;
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
        String s = doOcr(choose, AIP.getENG());
        System.out.println(s);
    }
}
