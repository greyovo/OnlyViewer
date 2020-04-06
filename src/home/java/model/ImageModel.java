package home.java.model;

import lombok.Data;

import java.io.File;

/**
 * @ProjName: OnlyViewer
 * @ClassName: ImageModel
 * @Author: Kevin
 * @Describe: 单元图片类
 */

@Data
public class ImageModel {

    private String imageFilePath; // *绝对路径
    private File imageFile;
    private String imageName;
    private long fileLength;
//    private String imageSize;
//    private String imageType; // 图片类型 依赖于ImageListModel
    private long imageLastModified; // 图片修改时间

    // 暂且先不考虑获取图片宽高，其耗时较多
//    private int imageWidth;
//    private int imageHeight;

    public ImageModel(File file){
        this.imageFile = file;
        this.imageFilePath = file.getAbsolutePath();
        this.imageName = file.getName();
        this.fileLength = file.length();
    }

    public ImageModel(String path){
        this.imageFilePath = path;
        this.imageFile = new File(path);
        this.imageName = imageFile.getName();
        this.fileLength = imageFile.length(); // 返回的单位是byte
        this.imageLastModified = imageFile.lastModified();
    }

    private void init(){

    }

    public String getFormatSize(){
        return home.java.model.GenUtilModel.getFormatSize(this.fileLength);
    }

    public String getFormatTime(){
        return home.java.model.GenUtilModel.getFormatTime(this.imageLastModified);
    }
}
