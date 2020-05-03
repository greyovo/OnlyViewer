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
    private String imageParentPath; // 图片所在文件夹路径
    private File imageFile;
    private String imageName;
    private String imageType;
    private long fileLength;
    private long imageLastModified; // 图片修改时间

    // 暂且先不考虑获取图片宽高，其耗时较多
//    private int imageWidth;
//    private int imageHeight;

    public ImageModel(File file){
        this.imageFile = file;
        this.imageFilePath = file.getAbsolutePath();
        this.imageParentPath = file.getParent();
        this.imageName = file.getName();
        this.imageType = imageName.substring(imageName.indexOf(".")+1).toLowerCase();
        this.fileLength = file.length();
        this.imageLastModified = file.lastModified();
    }

    public ImageModel(String path){
        this.imageFilePath = path;
        this.imageFile = new File(path);
        this.imageParentPath = imageFile.getParent();
        this.imageName = imageFile.getName();
        this.imageType = imageName.substring(imageName.indexOf(".")+1).toLowerCase();
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
