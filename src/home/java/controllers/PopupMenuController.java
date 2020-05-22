package home.java.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.JFXListView;
import home.java.components.CustomDialog;
import home.java.components.DialogType;
import home.java.components.ImageBox;
import home.java.model.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import splice.SplicePreviewWindow;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

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

//    @SuppressWarnings("unused")
//    public void setTargetImage(ImageBox imageBox){
//        this.imageBox = imageBox;
//        this.im = imageBox.getIm();
//    }

    @SuppressWarnings("unused")
    @FXML
    private void action() {
        ArrayList<ImageModel> sourceList = SelectionModel.getImageModelList();
        switch (popupList.getSelectionModel().getSelectedIndex()) {
            case 0:
                if (sourceList.isEmpty()) {
                    SelectedModel.setSourcePath(im.getImageFilePath());
                    System.out.println("复制图片源:" + im.getImageFilePath());
                } else {
                    SelectedModel.setSourcePath(sourceList);
                    System.out.println("复制图片源集合" + sourceList);
                }
                SelectedModel.setCopyOrMove(0);

                hc.getPasteButton().setDisable(false);
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent("已复制到剪贴板"));
                imageBox.getPopUpMenu().hide();
                break;
            case 1:
                if (sourceList.isEmpty()) {
                    SelectedModel.setSourcePath(im.getImageFilePath());
                    System.out.println("剪切图片源:" + im.getImageFilePath());
                } else {
                    SelectedModel.setSourcePath(sourceList);
                    System.out.println("剪切图片源集合" + sourceList);
                }
                SelectedModel.setCopyOrMove(1);

                hc.getPasteButton().setDisable(false);
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent("已剪切到剪贴板"));
                imageBox.getPopUpMenu().hide();
                break;
            case 2:
                if (sourceList.isEmpty()) {
                    SelectedModel.setSourcePath(im.getImageFilePath());
                    System.out.println("重命名图片源:" + im.getImageFilePath());
                } else {
                    SelectedModel.setSourcePath(sourceList);
                    System.out.println("重命名图片源集合" + sourceList);
                }
                new CustomDialog(hc, DialogType.RENAME, im,
                        "重命名图片").show();
                imageBox.getPopUpMenu().hide();
                break;
            case 3:
                System.out.println("点击压缩图片 压缩图片源:" + im.getImageFilePath());
                imageBox.getPopUpMenu().hide();

                if (sourceList.isEmpty()) {
                    SelectedModel.setSourcePath(im.getImageFilePath());
                    System.out.println("压缩图片源:" + im.getImageFilePath());
                } else {
                    SelectedModel.setSourcePath(sourceList);
                    System.out.println("压缩图片源集合" + sourceList);
                }
                int success = SelectedModel.compressImage(800);

                hc.refreshImagesList(hc.getSortComboBox().getValue());
                if (success != 0) snackbar.enqueue(new JFXSnackbar.SnackbarEvent("已压缩" + success + "张图片并创建副本"));
                else snackbar.enqueue(new JFXSnackbar.SnackbarEvent(" 没有图片进行压缩 \n压缩条件:大于800KB"));
                break;
            case 4:
                System.out.println("拼接功能");
                if (sourceList.isEmpty() || sourceList.size() == 1) {
                    //未选择或只选了一张图片
                    snackbar.enqueue(new JFXSnackbar.SnackbarEvent("请选择两张以上图片进行拼接"));
                } else {
                    SplicePreviewWindow previewWindow = new SplicePreviewWindow();
                    previewWindow.initImageList(sourceList);
                    //打开窗口
                    try {
                        previewWindow.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                imageBox.getPopUpMenu().hide();
                break;
            case 5:
                if (sourceList.isEmpty()) {
                    SelectedModel.setSourcePath(im.getImageFilePath());
                    new CustomDialog(hc, DialogType.DELETE, im,
                            "确认删除",
                            "要删除文件：" + im.getImageName() + " 吗？\n\n你可以在回收站处找回。").show();
                } else {
                    SelectedModel.setSourcePath(sourceList);
                    new CustomDialog(hc, DialogType.DELETE, im,
                            "确认删除",
                            "要删除这" + sourceList.size() + "个文件吗？\n\n你可以在回收站处找回。").show();
                }
                imageBox.getPopUpMenu().hide();
                break;
            case 6:
                Image image = new Image(im.getImageFile().toURI().toString());
                StringBuilder info = new StringBuilder();

                if (sourceList.isEmpty() || sourceList.size() == 1) {
                    info.append("尺寸：").append(image.getWidth()).append(" × ").append(image.getHeight()).append("\n");
                    info.append("类型：").append(im.getImageType().toUpperCase()).append("\n");
                    info.append("大小：").append(im.getFormatSize()).append("\n");
                    info.append("日期：").append(im.getFormatTime()).append("\n");
                    info.append("\n位置：").append(im.getImageFilePath());
                    new CustomDialog(hc, DialogType.INFO, im,
                            im.getImageName(), info.toString()).show();
                } else {
                    info.append("数量：").append(sourceList.size()).append(" 个\n");
                    long totalSize = 0;
                    for (ImageModel im : sourceList) {
                        totalSize += im.getFileLength();
                    }
                    info.append("大小：").append(GenUtilModel.getFormatSize(totalSize)).append("\n");
                    info.append("位置：").append(im.getImageParentPath()).append("\n");
                    CustomDialog dialog = new CustomDialog(hc, DialogType.INFO, null, "多个文件", info.toString());
                    dialog.getBodyTextArea().setPrefHeight(150);
                    dialog.show();
                }

                imageBox.getPopUpMenu().hide();
                break;
            default:
        }
    }
}
