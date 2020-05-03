package home.java.model;

import lombok.Getter;
import lombok.NonNull;
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

    // 分割.jpg后缀 复制时重复名字处理
    private static String copyName(String newPath) {
        StringBuilder sb = new StringBuilder(32);
        String sourceName = sourcePath.getFileName().toString();
        String nameBefore = sourceName.substring(0, sourceName.indexOf(".")); // 只有一个名字 没有.
        String nameAfter = sourceName.substring(sourceName.indexOf(".")); // 带有. .jpg
        sb.append(checkPath(newPath)).append(nameBefore).append("_copy").append(nameAfter);
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

    public static boolean setSourcePath(String path) {
        sourcePath = new File(path).toPath();
        return true;
    }


    // 粘贴功能 -> 选择复制 or 剪切
    // 目前如果遇到文件重复 -> 1.若是源文件夹与目的文件夹相同则重命名
    //                             -> 2.若是不同文件，则直接REPLACE
    public static boolean pasteImage(String path) {
        if (option == 0){
            if (getBeforePath().equals(path)) {
                // 情况1
                boolean flag = false;
                String[] flist = new File(path).list();
                String sourceFileName = sourcePath.getFileName().toString();
                for (String s : flist) {
                    if (sourceFileName.equals(s) & !flag) {
                        targetPath = new File(copyName(path)).toPath();
                        flag = true;
                    }
                }
                if (!flag) {
                    targetPath = new File(otherPath(path)).toPath();
                }
                try {
                    Files.copy(sourcePath, targetPath);
                } catch (IOException e) {
                    // 复制失败
                    return false;
                }
            } else {
                // 情况2
                targetPath = new File(otherPath(path)).toPath();
                try {
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    // 复制失败
                    return false;
                }
            }
        } else if (option == 1){
            targetPath = new File(otherPath(path)).toPath();
            try {
                Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                // 剪切失败
                return false;
            }
        }
        // 复制/剪切完了以后就置 -1->按粘贴键没反应
        option = -1;
        return true;
    }

//    // 剪切图片 目前如果遇到文件重复则直接覆盖
//    public static boolean moveImage(String path) {
//
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
        // TODO 回收机制
        try {
            Files.delete(sourcePath);
        } catch (IOException e) {
            System.out.println("删除失败!");
            return false;
        }
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
