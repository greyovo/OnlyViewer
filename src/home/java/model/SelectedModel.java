package home.java.model;

import com.jfoenix.controls.JFXSnackbar;
import com.sun.jna.platform.FileUtils;
import home.java.components.CustomDialog;
import home.java.components.DialogType;
import home.java.controllers.ControllerUtil;
import home.java.controllers.HomeController;
import lombok.Getter;
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * @ProjName: OnlyViewer
 * @ClassName: SelectedModel
 * @Author: Kevin
 * @Time:2020/3/22 0:00
 * @Describe: 对被选中的图片进行操作
 * 1.初始化源 2.粘贴 3.剪切 4.重命名 5.删除 6.压缩
 **/

public class SelectedModel {
    /**
     * 复制：如果遇到文件重复 -> 1.若是源文件夹与目的文件夹相同则重命名
     *                       -> 2.若是在不同文件夹，可选择覆盖或跳过
     * 剪切：如果遇到文件重复 -> 直接覆盖
     * 重命名：如果遇到文件重复 -> 直接覆盖
     * 支持多选操作的有：
     * 1.复制 2.剪切 3.删除 4.压缩 5.重命名
     */
    @Getter
    private static Path sourcePath;
    @Getter
    private static Set<Path> sourcePathSet = new HashSet<>();
    private static Path targetPath;

    @Setter
    @Getter // 选择复制/剪切 0->复制 1->剪切
    private static int copyOrMove = -1;

    @Getter
    @Setter // 选择单选/多选 0->单选 1->多选
    private static int singleOrMultiple = -1;

    @Getter @Setter
    private static int waitingPasteNum = 0;
    @Getter @Setter
    private static int havePastedNum = 0;
    private static int coverImage = 0;

    private static HomeController hc = (HomeController)ControllerUtil.controllers.get("HomeController");

    /**
     * 1.初始化源 复制/剪切/重命名/删除/压缩选项调用
     */
    public static boolean setSourcePath(@NonNull ImageModel im) {
        sourcePath = im.getImageFile().toPath();
        singleOrMultiple = 0;
        return true;
    }

    public static boolean setSourcePath(@NonNull File f) {
        sourcePath = f.toPath();
        singleOrMultiple = 0;
        return true;
    }

    /**
     * 单选时 传入一张图片地址 同时singleOrMultiple=0
     */
    public static boolean setSourcePath(String imagePath) {
        sourcePath = new File(imagePath).toPath();
        singleOrMultiple = 0;
        return true;
    }

    /**
     * 多选时直接传入一个列表即可 同时singleOrMultiple=1
     */
    public static boolean setSourcePath(Set<ImageModel> imList) {
        sourcePathSet.clear();    // 每次点击都需要清空List, 不创建对象以节约空间与时间
        for (ImageModel im : imList) {
            setSourcePath(im);
            sourcePathSet.add(sourcePath);
        }
        singleOrMultiple = 1;
        return true;
    }

    /**
     * 2.粘贴选项 1.若是源文件夹与目的文件夹相同则重命名 2.若是在不同文件夹，可选择覆盖或跳过
     *
     * @param path 新的文件夹路径
     */
    public static boolean pasteImage(String path) {
        havePastedNum = 0;
        System.out.println("调用paste时copyOrMove: " + copyOrMove);
        if (singleOrMultiple == 0) {
            try {
                microPaste(path);
            } catch (IOException e) {
                System.err.println("粘贴失败");
                return false;
            }
        } else if (singleOrMultiple == 1) {
            try {
                coverImage = 0;
                for (Path p : sourcePathSet) {
                    sourcePath = p;
                    microPaste(path);
                }
                System.out.println("coverImage: " + coverImage);
                if (coverImage !=0){
                    hc.getSnackbar().enqueue(new JFXSnackbar.SnackbarEvent("覆盖了 " + coverImage + " 张图片"));
                }
            } catch (IOException e) {
                System.err.println("粘贴失败");
                return false;
            }
        }
        return true;
    }

    public static boolean replaceImage() {
        System.out.println("调用覆盖时copyOrMove: " + copyOrMove);
        switch (copyOrMove) {
            case 0:
                try {
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                break;
//            case 1:
//                try {
//                    Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return false;
//                }
//                break;
        }
        return true;
    }

    // 粘贴的微操作
    private static void microPaste(String path) throws IOException{
        if (copyOrMove == 0){
            //复制粘贴
            if (getBeforePath().equals(path)) {
                // 情况1
                boolean flag = false;  // 哨兵flag
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
                Files.copy(sourcePath, targetPath);
                havePastedNum++;
            } else {
                // 情况2
                targetPath = new File(otherPath(path)).toPath();
                if (!imageRepeat(path)) {
                    // 没有重复的图片
                    Files.copy(sourcePath, targetPath);
                    havePastedNum++;
                } else {
                    show();
                }
            }
        } else if (copyOrMove == 1) {
            //剪切粘贴
            targetPath = new File(otherPath(path)).toPath();
            if (imageRepeat(path))
                coverImage++;
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            havePastedNum++;
            if (havePastedNum == waitingPasteNum)
                copyOrMove = -1;  // 剪切完了以后就置 -1->按粘贴键没反应
        }
    }

    /** 粘贴前需要判断是否有重复图片 */
    private static boolean imageRepeat(String path) {
        String targetImageName = targetPath.getFileName().toString();
        try {
            if (SearchImageModel.accurateSearch(targetImageName, Objects.requireNonNull(ImageListModel.initImgList(path))) != null) {
                // 找到有重复的图片
                System.out.println("有重复图片");
                if (SearchImageModel.accurateSearch(targetImageName, Objects.requireNonNull(ImageListModel.initImgList(path))) != null) {
                    // 有重复图片
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private synchronized static void show() {
        ImageModel im = new ImageModel(targetPath.toFile());
        new CustomDialog(hc, DialogType.REPLACE, im,
                "替换或跳过文件",
                "\n目标已包含一个名为\"" + im.getImageName() + "\"的文件\n").show();
    }

    /**
     * 3.重命名选项 重复命名直接覆盖
     *
     * @param newName 新的文件名
     */
    public static boolean renameImage(String newName) {
        if (singleOrMultiple == 0) {
            try {
                microRename(newName);
            } catch (IOException e) {
                System.err.println("重命名失败");
                return false;
            }
        } else if (singleOrMultiple == 1) {
            Path[] imArray = new Path[sourcePathSet.size()];
            sourcePathSet.toArray(imArray);
            for (int i = 0; i < imArray.length; i++) {
                sourcePath = imArray[i];
                try {
                    String beforeName = newName.substring(0, newName.lastIndexOf("."));
                    String afterName = newName.substring(newName.lastIndexOf("."));
                    microRename(beforeName + String.format("_%04d", i + 1) + afterName);
                } catch (IOException e) {
                    System.err.println("重命名失败");
                    return false;
                }
            }
        }
        singleOrMultiple = -1;
        return true;
    }

    private static void microRename(String newName) throws IOException {
        targetPath = new File(otherName(newName)).toPath();
        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 4.删除图片选项
     * @return 返回删除成功的图片个数
     */
    public static int deleteImage() {
        int success=0;
        // 删除图片文件进入回收站，不直接删除
        if (singleOrMultiple == 0) {
            try {
                microDelete();
                success++;
            } catch (IOException e) {
                System.err.println("删除失败");
                return 0;
            }
        } else if (singleOrMultiple == 1) {
            for (Path p : sourcePathSet) {
                sourcePath = p;
                try {
                    microDelete();
                    success++;
                } catch (IOException e) {
                    System.err.println("删除失败");
                    return 0;
                }
            }
        }
        singleOrMultiple = -1;
        return success;
    }

    // 删除的微操作
    private static void microDelete() throws IOException {
        FileUtils fileUtils = FileUtils.getInstance();
        if (fileUtils.hasTrash()) {
            fileUtils.moveToTrash(new File[]{(sourcePath.toFile())});
        }
    }


    /**
     * 5.压缩图片选项
     *
     * @param desSize 目标大小
     */
    // 压缩图片 desSize 目标字节数 最终压缩结果向1MB靠近
    public static boolean compressImage(int desSize) {
        if (singleOrMultiple == 0) {
            try {
                if (!microCompress(desSize))
                    return false;
            } catch (IOException e) {
                System.err.println("压缩失败");
                return false;
            }
        } else if (singleOrMultiple == 1) {
            for (Path p : sourcePathSet) {
                sourcePath = p;
                try {
                    microCompress(desSize);
                } catch (IOException e) {
                    System.err.println("压缩失败");
                    return false;
                }
            }
        }
        singleOrMultiple = -1;
        return true;
    }

    // 压缩图片微操作
    private static boolean microCompress(int desSize) throws IOException {
        byte[] imageBytes = GenUtilModel.getByteByFile(sourcePath.toFile());
        if (imageBytes == null || imageBytes.length < desSize * 1024) {
            // 不需要压缩了
            return false;
        }
        double accuracy = 0;
        System.out.println("进行压缩");
        if (imageBytes.length > desSize * 1024) {
            accuracy = getAccuracy(imageBytes.length / 1024.0);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(imageBytes.length);
            Thumbnails.of(sourcePath.toFile())
                    .scale(accuracy)  // 分辨率
                    .outputQuality(accuracy)  // 图片质量
                    .toOutputStream(bos);
//                        .toFile(newFile);  // 速度略慢
            imageBytes = bos.toByteArray();
        }
        System.out.println("压缩完毕");
        String newImagePath = suffixName(getBeforePath(), "_only");
        File newFile = new File(newImagePath);
        return GenUtilModel.getFileByByte(imageBytes, newFile);
    }

    private static double getAccuracy(double imageSize) {
        double accuracy = 0;
        if (imageSize < 1024 * 2) {
            accuracy = 0.71;
        } else if (imageSize < 1024 * 4) {
            accuracy = 0.66;
        } else if (imageSize < 1024 * 8) {
            accuracy = 0.61;
        } else {
            accuracy = 0.59;
        }
        return accuracy;
    }

    /**
     * 文件名处理私有方法
     */
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

    @Test
    public void Test() {
        /** 复制 686个 2.94G 1m30s
         剪切 686个 2.94G 4 - 7s
         删除 686个 2.94G 3 - 4s **/
        try {
            String path = "D:\\TestImg2\\test";
            ArrayList<ImageModel> ilist = ImageListModel.initImgList(path);
            long timef = System.currentTimeMillis();
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
}
