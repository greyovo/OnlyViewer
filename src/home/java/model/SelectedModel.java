package home.java.model;

import com.sun.jna.platform.FileUtils;
import lombok.NonNull;
import lombok.Setter;
import net.coobird.thumbnailator.Thumbnails;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


/**
 * @ProjName: OnlyViewer
 * @ClassName: SelectedModel
 * @Author: Kevin
 * @Time:2020/3/22 0:00
 * @Describe: 对被选中的图片进行操作
 * 1.初始化源 2.粘贴 3.剪切 4.重命名 5.删除
 **/

public class SelectedModel {
    /**
     * 复制：如果遇到文件重复 -> 1.若是源文件夹与目的文件夹相同则重命名
     * -> 2.若是不同文件，则直接REPLACE
     * 剪切：如果遇到文件重复 -> 直接覆盖
     * 重命名：如果遇到文件重复 -> 直接覆盖
     */
    private static Path sourcePath;
    private static Path targetPath;

    @Setter // 选择复制/剪切 0->复制 1->剪切
    private static int option = -1;

    // 检查路径后缀
    private static String checkPath(String path) {
        StringBuilder sb = new StringBuilder(32);
        if (!path.endsWith("\\")) {
            sb.append(path).append("\\");
        } else {
            sb.append(path);
        }
        return sb.toString();
    }

    // 获得图片绝对路径的前部分
    private static String getBeforePath() {
        String path = sourcePath.toString();
        return path.substring(0, path.lastIndexOf("\\"));
    }

    // 修改路径 复制/剪切
    private static String otherPath(String newPath) {
        StringBuilder sb = new StringBuilder(32);
        sb.append(checkPath(newPath)).append(sourcePath.getFileName().toString()); // 获得文件名
        return sb.toString();
    }

    // 修改名字 重命名
    private static String otherName(String newName) {
        StringBuilder sb = new StringBuilder(32);
        String path = sourcePath.toString().substring(0, sourcePath.toString().lastIndexOf("\\"));
        sb.append(path).append("\\").append(newName);
        return sb.toString();
    }

    // 分割.jpg后缀 处理名字前半部分冲突
    private static String suffixName(String newPath, String suffix) {
        StringBuilder sb = new StringBuilder(32);
        String sourceName = sourcePath.getFileName().toString();
        String nameBefore = sourceName.substring(0, sourceName.indexOf(".")); // 只有一个名字 没有.
        String nameAfter = sourceName.substring(sourceName.indexOf(".")); // 带有. .jpg
        sb.append(checkPath(newPath)).append(nameBefore).append(suffix).append(nameAfter);
        return sb.toString();
    }



    // 初始化源 复制/剪切/重命名/删除选项调用
    public static boolean setSourcePath(@NonNull ImageModel im) {
        sourcePath = im.getImageFile().toPath();
        return true;
    }

    public static boolean setSourcePath(@NonNull File f) {
        sourcePath = f.toPath();
        return true;
    }

    public static boolean setSourcePath(String imagePath) {
        sourcePath = new File(imagePath).toPath();
        return true;
    }


    // 粘贴功能 -> 选择复制 or 剪切
    // 目前如果遇到文件重复 -> 1.若是源文件夹与目的文件夹相同则重命名
    //                             -> 2.若是不同文件，则直接REPLACE
    // TODO  遇到重命名应询问是否覆盖
    public static boolean pasteImage(String path) {

        if (option == 0){
            //复制粘贴
            if (getBeforePath().equals(path)) {
                // 情况1
                boolean flag = false;
                String[] flist = new File(path).list();
                String sourceFileName = sourcePath.getFileName().toString();
                for (String s : flist) {
                    if (sourceFileName.equals(s) & !flag) {
                        targetPath = new File(suffixName(path, "_copy")).toPath();
                        flag = true;
                    }
                }
                if (!flag) {
                    targetPath = new File(otherPath(path)).toPath();
                }
                try {
                    Files.copy(sourcePath, targetPath);
                } catch (IOException e) {
                    return false; // 复制失败
                }
            } else {
                // 情况2
                targetPath = new File(otherPath(path)).toPath();
                try {
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    return false;// 复制失败
                }
            }
            return true;

        } else if (option == 1) {
            //剪切粘贴
            targetPath = new File(otherPath(path)).toPath();
            try {
                Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                return false;// 剪切失败
            }
            option = -1;  // 剪切完了以后就置 -1->按粘贴键没反应
            return true;
        }
        return false;
    }

//    // 剪切图片 目前如果遇到文件重复则直接覆盖
//    public static boolean moveImage(String path) {
//        targetPath = new File(otherPath(path)).toPath();
//        try {
//            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            // 剪切失败
//            return false;
//        }
//        // 复制/剪切完了以后就置 -1->按粘贴键没反应
//        option = -1;
//        return true;
//    }

    // 重命名 重复命名直接覆盖
    public static boolean renameImage(String newName) {
        targetPath = new File(otherName(newName)).toPath();
        try {
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    // 删除图片
    public static boolean deleteImage() {
        // 删除图片文件进入回收站，不直接删除
        FileUtils fileUtils = FileUtils.getInstance();
        try {
            if (fileUtils.hasTrash()) {
                fileUtils.moveToTrash(new File[] { (sourcePath.toFile()) });
            }
        } catch (IOException e) {
            System.out.println("删除失败!");
            return false;
        }
        return true;
    }

    private static double getAccuracy(double imageSize) {
        double accuracy = 0;
        if (imageSize < 1024*5) {
            accuracy = 0.8;
        } else if (imageSize < 1024*8) {
            accuracy = 0.65;
        } else if (imageSize < 1024*10) {
            accuracy = 0.6;
        } else {
            accuracy = 0.55;
        }
        return accuracy;
    }

    // 压缩图片 desSize 目标字节数 最终压缩结果向1MB靠近 返回值是新的文件夹
    public static boolean compressImage(String imagePath, int desSize) {
        sourcePath = new File(imagePath).toPath();
        byte[] imageBytes = GenUtilModel.getByteByFile(sourcePath.toFile());
        if (imageBytes == null || imageBytes.length < desSize * 1024){
            // 不需要压缩了
            return false;
        }
        double accuracy = 0;
        try {
            System.out.println("进行压缩");
            if (imageBytes.length > desSize * 1024) {
                accuracy = getAccuracy(imageBytes.length / 1024.0);
                ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(bis)
                        .scale(accuracy)
                        .outputQuality(accuracy)
                        .toOutputStream(bos);
                imageBytes = bos.toByteArray();
            }
            System.out.println("压缩完毕");
        } catch (IOException e){
            e.printStackTrace();
        }
        String newImagePath = suffixName(getBeforePath(), "_press");
        File newFile = new File(newImagePath);
        GenUtilModel.getFileByByte(imageBytes, newFile);
        return true;
    }

    @Test
    public void Test() {
        /** 复制 686个 2.94G 1m30s
         剪切 686个 2.94G 4 - 7s
         删除 686个 2.94G 3 - 4s **/
        try {
            String path = "H:\\Ding\\test1";
            ArrayList<ImageModel> ilist = ImageListModel.initImgList(path);
            long timef = System.currentTimeMillis();
            for (ImageModel s : ilist) {
                setSourcePath(s.getImageFilePath());
                deleteImage();
            }
            long timel = System.currentTimeMillis();
            System.out.printf("耗时 %d ms\n", timel - timef);
            System.out.println("操作成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        File file = new File("H:\\Ding\\test2");
//        String[] list = file.list();
//        System.out.println(list); // [Ljava.lang.String;@6bf2d08e
//        String path = "H:\\Ding\\test2\\P70125-214324.jpg";
//        System.out.println(path.substring(0, path.lastIndexOf("\\"))); //H:\Ding\test2
//        for (String s : list){ ;
//            System.out.println(s.substring(0, s.indexOf("."))); //P70125-214324
//            System.out.println(s.substring(s.indexOf("."))); //.jpg
//
//        }
    }
}
