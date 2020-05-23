package home.java.model;

import lombok.Data;

import java.io.File;

/**
 * 单元图片类
 *
 * @author Kevin
 * @since 2020/3/16
 **/

@Data
public class ImageModel {

    private String imageFilePath;   // *绝对路径
    private String imageParentPath; // 图片所在文件夹路径
    private File imageFile;
    private String imageName;
    private String imageNameNoExt;  // 不含拓展名的名字
    private String imageType;
    private long fileLength;
    private long imageLastModified; // 图片修改时间

    public ImageModel(File file) {
        this.imageFile = file;
        this.imageFilePath = file.getAbsolutePath();
        this.imageParentPath = file.getParent();
        this.imageName = file.getName();
        this.imageNameNoExt = imageName.substring(0, imageName.lastIndexOf("."));
        this.imageType = imageName.substring(imageName.indexOf(".") + 1).toLowerCase();
        this.fileLength = file.length();
        this.imageLastModified = file.lastModified();
    }

    public ImageModel(String path) {
        this.imageFilePath = path;
        this.imageFile = new File(path);
        this.imageParentPath = imageFile.getParent();
        this.imageName = imageFile.getName();
        this.imageNameNoExt = imageName.substring(0, imageName.lastIndexOf("."));
        this.imageType = imageName.substring(imageName.indexOf(".") + 1).toLowerCase();
        this.fileLength = imageFile.length(); // 返回的单位是byte
        this.imageLastModified = imageFile.lastModified();
    }

    public String getFormatSize() {
        return GenUtilModel.getFormatSize(this.fileLength);
    }

    public String getFormatTime() {
        return GenUtilModel.getFormatTime(this.imageLastModified);
    }
}
