package model;

import lombok.Data;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @ProjName: OnlyViewer
 * @ClassName: ImageListModel
 * @Author: Kevin
 * @Time:2020/3/18 11:11
 * @Describe: 文件夹内的图片列表
 * 1.判断是否为图片 2.计算图片数 3.创建图片列表
 **/

@Data
public class ImageListModel {

    // Image支持的五种格式
    // 有多种方法检查 1.后缀 2. ImageIO 3.Iterator
    // 1.简单地通过后缀判断不靠谱但是快
    // 2.利用ImageIO检查图片格式，支持jpg/jpeg/png/gif/bmp
    // 缺点：ImageIO在解码时涉及磁盘I/O，使得读取速度非常慢
    // 3.利用ImageInputStream将图片变成输入流
    // 利用ImageIO的getImageReaders返回一个迭代器
    // 提高了程序效率

    private static String type;

    private static int width;
    private static int height;

    // 判断文件是否为图片 支持jpg/jepg/png/gif/bmp,暂不支持psd
    private static boolean isSupportedImg(File file) throws IOException {
        try{
            ImageInputStream iis = ImageIO.createImageInputStream(file);
            Iterator iterator = ImageIO.getImageReaders(iis);
            if (!iterator.hasNext())
                return false;
            else{
                ImageReader ir = (ImageReader) iterator.next();
//                ir.setInput(iis, true);
//                type = ir.getFormatName();
//                width = ir.getWidth(0);
//                height = ir.getHeight(0);
                iis.close();
                return true;
            }
        }catch (IllegalArgumentException e){
            // 修复Bug: 遇到系统文件应该进行异常捕获
//            System.out.println("无效文件");
            return false;
        }
    }

    // 创建列表前需要判断当前文件夹里有没有照片,同时返回有多少张图片
    public static int calcImgNum(String path)  {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null){
            return 0;
        }
        int num = 0;
        for (File f : files){
            try {
                if (f.isFile() && isSupportedImg(f)){
                    num++;
                }
            }catch (IOException e){
                System.out.println("ImageIO Input Error!");
            }
        }
        return num;
    }


    // init前提:文件夹里有image
    public static ArrayList<ImageModel> initImgList(String path){
        File file = new File(path);
        File[] files = file.listFiles();
        ArrayList<ImageModel> imageList = new ArrayList<>(); // 初始化
        for (File f : files){
            try {
                if (f.isFile() && isSupportedImg(f)){
                    ImageModel img = new ImageModel(f.getAbsolutePath());
//                    img.setImageType(type);
                    imageList.add(img);
                }
            }catch (IOException e){
                System.out.println("ImageIO Input Error!");
            }
        }
        System.out.println("init ImageList successfully!");
        return imageList;
    }

    // 获取这个文件夹里的 *图片*大小 不包括其他文件夹的大小
    public static String getListImgSize(ArrayList<ImageModel> im){
        long totalSize = 0;
        for (ImageModel i:im){
            totalSize += i.getFileLength();
        }
        return GenUtilModel.getStandardSize(totalSize);
    }

    // TODO 刷新文件夹功能


    @Test
    public void Test1(){

        String filePath = "F:\\Ding\\Ding_photo\\HK Shenzhen\\HK";

        int imageNum = calcImgNum(filePath);
        if (imageNum==0){
            System.out.println("There is no image!");
        }
        else if (imageNum>0){
            ArrayList<ImageModel> list = initImgList(filePath);
            for (ImageModel i : list){
                System.out.println("imgName:"+i.getImageName()+
                        "\t\timgLastModified:"+i.getImageLastModified()+
                        "\t\timgSize:"+i.getImageSize());
            }
            System.out.println("totalImgSize:" + getListImgSize(list));
        }
        System.out.println("测试成功!");
    }
}