package home.java.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.JFXListView;
import home.java.components.CustomDialog;
import home.java.components.DialogType;
import home.java.components.ImageBox;
import home.java.model.ImageListModel;
import home.java.model.ImageModel;
import home.java.model.SelectedModel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PopupMenuController implements Initializable {

    @FXML
    private JFXListView<?> popupList;

    private ImageModel im;
    private ImageBox imageBox;
    private HomeController hc;

    @Getter
    private JFXSnackbar snackbar;

    public PopupMenuController() {
        //将本类的实例添加到全局映射中
        ControllerUtil.controllers.put(this.getClass().getSimpleName(), this);
//        System.out.println("put PopupMenuCon in Map...");

        //获取HomeController实例
        hc = (HomeController) ControllerUtil.controllers.get(HomeController.class.getSimpleName());
//        System.out.println("hc in PopMenuCon: " + hc);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //信息条初始化
        snackbar = new JFXSnackbar(hc.getRootPane());
    }

    public PopupMenuController(ImageBox imageBox) {
        this();
        this.imageBox = imageBox;
        this.im = imageBox.getIm();
    }

    @FXML
    private void action() {
        switch (popupList.getSelectionModel().getSelectedIndex()) {
            case 0:
                System.out.println("点击复制 复制图片源:" + im.getImageFilePath());
                SelectedModel.setSourcePath(im.getImageFilePath());
                SelectedModel.setOption(0);
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent("已复制到剪贴板"));
                imageBox.getPopUpMenu().hide();
                break;
            case 1:
                System.out.println("点击剪切 剪切图片源:" + im.getImageFilePath());
                SelectedModel.setSourcePath(im.getImageFilePath());
                SelectedModel.setOption(1);
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent("已剪切到剪贴板"));
                imageBox.getPopUpMenu().hide();
                break;
            case 2:
                System.out.println("点击重命名 重命名图片源:" + im.getImageFilePath());
                // TODO 重命名输入名字
                new CustomDialog(hc, DialogType.RENAME, im,
                        "重命名图片",
                        "在做了在做了...Orz").show();
                imageBox.getPopUpMenu().hide();
                break;
            case 3:
                System.out.println("点击压缩图片 压缩图片源:" + im.getImageFilePath());
                imageBox.getPopUpMenu().hide();
                SelectedModel.compressImage(im.getImageFilePath(), 800);
                try {
                    // 手动实现刷新
                    hc.placeImages(ImageListModel.initImgList(im.getImageParentPath()), im.getImageParentPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent("已压缩图片并创建副本"));
                break;
            case 4:
                System.out.println("点击删除 删除图片源:" + im.getImageFilePath());
//                hc.callDeleteDialog(im);
                new CustomDialog(hc, DialogType.DELETE, im,
                        "确认删除",
                        "要删除文件：" + im.getImageName() + " 吗？\n\n你可以在回收站处找回。").show();
                imageBox.getPopUpMenu().hide();
                break;
            case 5:
                Image image = new Image(im.getImageFile().toURI().toString());
                StringBuilder info = new StringBuilder();
                info.append("尺寸：").append(image.getWidth()).append(" × ").append(image.getHeight()).append("\n");
                info.append("类型：").append(im.getImageType().toUpperCase()).append("\n");
                info.append("大小：").append(im.getFormatSize()).append("\n");
                info.append("日期：").append(im.getFormatTime()).append("\n");
                info.append("\n位置：").append(im.getImageFilePath()).append("\n");
                new CustomDialog(hc, DialogType.INFO, im,
                        im.getImageName(), info.toString()).show();
                imageBox.getPopUpMenu().hide();
                break;
            default:
        }
    }
}
