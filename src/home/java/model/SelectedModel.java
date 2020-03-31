package model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.io.*;

/**
 * @ProjName: OnlyViewer
 * @ClassName: SelectedModel
 * @Author: Kevin
 * @Time:2020/3/22 0:00
 * @Describe: 对被选中的图片进行操作
 * 1.复制 2.粘贴 3.删除 4.剪切 5.重命名
 **/

public class SelectedModel {
    // 文件流操作
    private static InputStream in;
    private static OutputStream out;
    private static BufferedInputStream bin;
    private static BufferedOutputStream bout;

    @Getter @Setter
    // 图片名字处理问题
    private static String ImageName;
    private static String ImageNewName;

    // 复制功能创建缓冲对象
    public static boolean copyImage(ImageModel im){
        System.out.println(im.getImageFilePath());
        File f = im.getImageFile();
        ImageName = im.getImageName();
        if (!f.exists()){
            System.out.println("Image is not exist!");
            return false;
        }
        try{
            in = new FileInputStream(f);
            bin = new BufferedInputStream(in);
        }catch (FileNotFoundException e){
            System.out.println("Can't find the image!");
        }
        System.out.println("Copy in buffer successfully!");
        return true;
    }

    // 执行粘贴操作时 传入目的路径参数
    public static boolean pasteImage(String path) {
        File f = new File(path);
        try{
            out = new FileOutputStream(f);
            bout = new BufferedOutputStream(out);
            // size代表读取的字节数
            int size = 0;
            while ((size = bin.read())!= -1){
                bout.write(size);
            }
        }catch (FileNotFoundException e){
            System.out.println("Can't find the paste path!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 当执行close时缓冲区剩下的字节全部写入
                bin.close();
                bout.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return true;
    }

    // 删除图片
    public static boolean deleteImage(ImageModel im){
        File f = im.getImageFile();
        if (!f.exists()){
            return false;
        }
        if (f.isFile()){
            if(f.delete())
                System.out.println("Delete image successfully!");
            else
                System.out.println("Delete image unsuccessfully!");
        }
        return true;
    }

    // TODO 剪切图片=复制粘贴后执行删除


    // TODO 重命名图片
    public static boolean renameImage(ImageModel im, String reName){
        File f = im.getImageFile();
//        f.renameTo(new File(reName));

        return true;
    }


    @Test
    public void test(){
        ImageModel im = new ImageModel("D:\\TestImg\\IMG_2503.JPG");
        copyImage(im);
        System.out.println("右键粘贴后");
        if(pasteImage("D:/TestImg2/CopyTest.png"))
            System.out.println("复制完成!");
        else
            System.out.println("复制失败!");
        if(deleteImage(new ImageModel("D:\\TestImg2\\校园卡.jpg")))
            System.out.println("删除成功!");
        else
            System.out.println("目标图片不存在!");
    }
}
