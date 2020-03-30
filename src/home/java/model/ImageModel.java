package model;

import lombok.Data;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ProjName: OnlyViewer
 * @ClassName: ImageModel
 * @Author: Kevin
 * @Describe: 单元图片类
 */

//lombok自动完成getter/setter/toString
@Data
public class ImageModel {

    private String imageFilePath; // *绝对路径
    private File imageFile;
    private String imageName;
    private long fileLength;
    private String imageSize;
//    private String imageType; // 图片类型 依赖于ImageListModel
    private String imageLastModified; // 图片修改时间

    // 暂且先不考虑获取图片宽高，其耗时较多
    private int imageWidth;
    private int imageHeight;

    public ImageModel(File file){
        this.imageFile = file;
        this.imageFilePath = file.getAbsolutePath();
        this.imageName = file.getName();
        this.fileLength = file.length();
        this.imageSize = getStandardSize(fileLength);
        this.imageLastModified = getStandardTime(imageFile.lastModified());
    }

    public ImageModel(String path){
        this.imageFilePath = path;
        this.imageFile = new File(path);
        this.imageName = imageFile.getName();
        this.fileLength = imageFile.length(); // 返回的单位是byte
        this.imageSize = getStandardSize(fileLength);
        this.imageLastModified = getStandardTime(imageFile.lastModified());
    }

    public static String getStandardSize(long fileLength){
        return GenUtilModel.getStandardSize(fileLength);
    }

    public static String getStandardTime(long time){
        String imgStandardTime = null;
        Date data = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        imgStandardTime = sdf.format(data);
        return imgStandardTime;
    }
}
