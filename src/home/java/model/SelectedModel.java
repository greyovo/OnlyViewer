package home.java.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 * @ProjName: OnlyViewer
 * @ClassName: SelectedModel
 * @Author: Kevin
 * @Time:2020/3/22 0:00
 * @Describe: 对被选中的图片进行操作
 * 1.初始化源 2.粘贴 3.剪切 4.重命名 5.删除
 **/

public class SelectedModel {
    private static Path sourcePath;
    private static Path targetPath;

    // 修改路径
    private static String otherPath(String newPath){
        StringBuilder sb = new StringBuilder(32);
        if (!newPath.endsWith("\\")){
            sb.append(newPath).append("\\");
        }else {
            sb.append(newPath);
        }
        sb.append(sourcePath.getFileName().toString()); // 获得文件名
//        System.out.println(sb);
        return sb.toString();
    }

    // 修改名字
    private static String otherName(String newName){
        StringBuilder sb = new StringBuilder(32);
        String path = sourcePath.toString().substring(0, sourcePath.toString().lastIndexOf("\\"));
        sb.append(path).append("\\").append(newName);
        return sb.toString();
    }

    // 初始化源 复制/剪切/重命名/删除选项调用
    public static boolean sourceImage(ImageModel im){
        sourcePath = im.getImageFile().toPath();
        return true;
    }
    // 重载
    public static boolean sourceImage(File f){
        sourcePath = f.toPath();
        return true;
    }
    // 重载
    public static boolean sourceImage(String path){
        sourcePath = new File(path).toPath();
        return true;
    }



    // 粘贴功能 如果遇到文件重复则覆盖
    public static boolean pasteImage(String path){
        targetPath = new File(otherPath(path)).toPath();
        try{
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            // TODO 选择框选择重复文件如何操作
            return false;
        }
        return true;
    }

    // 剪切图片
    public static boolean moveImage(String path){
        targetPath = new File(otherPath(path)).toPath();
        try{
            Files.move(sourcePath, targetPath);
        }catch (IOException e){
            // TODO 选择框选择重复文件如何操作
            return false;
        }
        return true;
    }

    // 重命名
    public static boolean renameImage(String newName){
        targetPath = new File(otherName(newName)).toPath();
        try{
            Files.move(sourcePath, targetPath);
        }catch (IOException e){
            // TODO 选择框选择重复文件如何操作
            return false;
        }
        return true;
    }

    // 删除图片
    public static boolean deleteImage(){
        try{
            Files.delete(sourcePath);
        }catch (IOException e){
            System.out.println("删除失败!");
            return false;
        }
        return true;
    }



    @Test
    public void Test() {
        /** 复制 686个 2.94G 1m28s
            剪切 686个 2.94G 2.4s
            删除 686个 2.94G 4.7s **/
        try{
            String path = "H:\\Ding\\test1";
            ArrayList<ImageModel> ilist = ImageListModel.initImgList(path);
            long timef = System.currentTimeMillis();
            for (ImageModel s : ilist) {
                sourceImage(s.getImageFilePath());
                moveImage("H:\\Ding\\test2");
            }
            long timel = System.currentTimeMillis();
            System.out.printf("耗时 %d ms\n", timel - timef);
            System.out.println("剪切成功");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
