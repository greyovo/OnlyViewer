package home.java.components;

import home.java.model.ImageModel;
import home.java.model.SelectedModel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.util.Optional;

/**
 * @ProjName: OnlyViewer
 * @ClassName: MouseImageMenu
 * @Author: Kevin
 * @Time: 2020/4/13 15:06
 * @Describe:
 **/

public class MouseImageMenu extends ContextMenu {

    private static MouseImageMenu instance;

    /**
     * 针对对图片右键点击出现的菜单选择
     *
     * @param im ImageModel
     */
    public MouseImageMenu(ImageModel im) {
        MenuItem copy = new MenuItem("  复制(C)  ");
        MenuItem move = new MenuItem("  剪切(T)  ");
        MenuItem reName = new MenuItem("  重命名(M)  ");
        MenuItem delete = new MenuItem("  删除(D)  ");

        copy.setOnAction(event -> {
            System.out.println("点击复制 复制图片源:" + im.getImageFilePath());
            SelectedModel.sourceImage(im.getImageFilePath());
        });
        move.setOnAction(event -> {
            System.out.println("点击剪切 剪切图片源:" + im.getImageFilePath());
            SelectedModel.sourceImage(im.getImageFilePath());
        });
        reName.setOnAction(event -> {
            System.out.println("点击重命名 重命名图片源:" + im.getImageFilePath());
            // TODO 输入名字

        });
        delete.setOnAction(event -> {
            System.out.println("点击删除 删除图片源:" + im.getImageFilePath());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("删除文件");
            alert.setHeaderText("确定要把此文件删除吗？");
            alert.setContentText("删除文件:" + im.getImageName());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                SelectedModel.sourceImage(im.getImageFilePath());
                SelectedModel.deleteImage();
                System.out.println("删除成功!");
            } else if (result.get() == ButtonType.CANCEL) {
                System.out.println("您取消删除了喔~");
                alert.close();
            }
        });
        getItems().addAll(copy, move, reName, delete);
    }

    public static ContextMenu getInstance(ImageModel im) {
        instance = new MouseImageMenu(im);
        return instance;
    }
}
