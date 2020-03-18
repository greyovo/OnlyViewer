package model;

import lombok.Data;
import org.junit.Test;
import java.io.File;

/**
 * ProjName: OnlyViewer
 * FileName: ImageModel
 * Author: Kevin
 * Describe: 单元图片类
 */

//lombok自动完成getter/setter/toString
@Data
public class ImageModel {

    private String imageFilePath; // *绝对路径
    private File imageFile;
    private String imageName;
    private long imageLength;
    private String imageType;

    public ImageModel(String path){
        this.imageFilePath = path; // 单个图片的绝对路径
        this.imageFile = new File(path);
        this.imageName = imageFile.getName();
        this.imageLength = imageFile.length();
    }

//    //单元测试
//    @Test
//    public void Test1(){
//        // listFiles只会找该层级的文件/文件夹
//        File file = new File("D:/Idea/Gitproj/OnlyViewer/src/home/java");
//        File[] files = file.listFiles();
//        assert files != null;
//        for(File f: files){
//            System.out.println(f.getName());
//        }
//        System.out.println("Test!");
//    }
}
