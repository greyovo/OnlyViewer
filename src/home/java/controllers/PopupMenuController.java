package home.java.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.JFXListView;
import home.java.components.CustomDialog;
import home.java.components.DialogType;
import home.java.components.ImageBox;
import home.java.model.ImageListModel;
import home.java.model.ImageModel;
import home.java.model.SelectedModel;

import home.java.model.SelectionModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import splice.SplicePreviewWindow;

import java.io.IOException;
import java.net.URL;
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

    @FXML
    private void action() {
        switch (popupList.getSelectionModel().getSelectedIndex()) {
            case 0:
                if (SelectionModel.getImageModelSet().isEmpty()) {
                    SelectedModel.setSourcePath(im.getImageFilePath());
                    System.out.println("复制图片源:" + im.getImageFilePath());
                } else {
                    SelectedModel.setSourcePath(SelectionModel.getImageModelSet());
                    System.out.println("复制图片源集合" + SelectionModel.getImageModelSet());
                }
                SelectedModel.setCopyOrMove(0);

                hc.getPasteButton().setDisable(false);
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent("已复制到剪贴板"));
                imageBox.getPopUpMenu().hide();
                break;
            case 1:
                if (SelectionModel.getImageModelSet().isEmpty()) {
                    SelectedModel.setSourcePath(im.getImageFilePath());
                    System.out.println("剪切图片源:" + im.getImageFilePath());
                } else {
                    SelectedModel.setSourcePath(SelectionModel.getImageModelSet());
                    System.out.println("剪切图片源集合" + SelectionModel.getImageModelSet());
                }
                SelectedModel.setCopyOrMove(1);

                hc.getPasteButton().setDisable(false);
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent("已剪切到剪贴板"));
                imageBox.getPopUpMenu().hide();
                break;
            case 2:
                if (SelectionModel.getImageModelSet().isEmpty()) {
                    SelectedModel.setSourcePath(im.getImageFilePath());
                    System.out.println("重命名图片源:" + im.getImageFilePath());
                } else {
                    SelectedModel.setSourcePath(SelectionModel.getImageModelSet());
                    System.out.println("重命名图片源集合" + SelectionModel.getImageModelSet());
                }
                new CustomDialog(hc, DialogType.RENAME, im,
                        "重命名图片").show();
                imageBox.getPopUpMenu().hide();
                break;
            case 3:
                System.out.println("点击压缩图片 压缩图片源:" + im.getImageFilePath());
                imageBox.getPopUpMenu().hide();

                if (SelectionModel.getImageModelSet().isEmpty()) {
                    SelectedModel.setSourcePath(im.getImageFilePath());
                    System.out.println("压缩图片源:" + im.getImageFilePath());
                } else {
                    SelectedModel.setSourcePath(SelectionModel.getImageModelSet());
                    System.out.println("压缩图片源集合" + SelectionModel.getImageModelSet());
                }
                boolean flag = SelectedModel.compressImage(800);

                try {
                    // 手动实现刷新
                    hc.placeImages(ImageListModel.initImgList(im.getImageParentPath()), im.getImageParentPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (flag) snackbar.enqueue(new JFXSnackbar.SnackbarEvent("已压缩图片并创建副本"));
                else snackbar.enqueue(new JFXSnackbar.SnackbarEvent("压缩图片条件为大于800KB"));
                break;
            case 4:
                System.out.println("拼接功能");
                // TODO: 2020/5/12 图片拼接功能对接
                Set<ImageModel> imSet = SelectionModel.getImageModelSet();
                if (imSet.isEmpty()
                        || imSet.size() == 1) {
                    snackbar.enqueue(new JFXSnackbar.SnackbarEvent("请选择两张以上图片进行拼接"));
                } else {
                    snackbar.enqueue(new JFXSnackbar.SnackbarEvent("在做啦在做啦^^"));
                    SplicePreviewWindow previewWindow = new SplicePreviewWindow();
                    previewWindow.initImageSet(imSet);
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
                if (SelectionModel.getImageModelSet().isEmpty()) {
                    SelectedModel.setSourcePath(im.getImageFilePath());
                    System.out.println("删除图片源:" + im.getImageFilePath());
                    new CustomDialog(hc, DialogType.DELETE, im,
                            "确认删除",
                            "要删除文件：" + im.getImageName() + " 吗？\n\n你可以在回收站处找回。").show();
                } else {
                    SelectedModel.setSourcePath(SelectionModel.getImageModelSet());
                    System.out.println("删除图片源集合" + SelectionModel.getImageModelSet());
                    new CustomDialog(hc, DialogType.DELETE, im,
                            "确认删除",
                            "要删除这" + SelectionModel.getImageModelSet().size() + "个文件吗？\n\n你可以在回收站处找回。").show();
                }
                imageBox.getPopUpMenu().hide();
                break;
            case 6:
                Image image = new Image(im.getImageFile().toURI().toString());
                StringBuilder info = new StringBuilder();
                info.append("尺寸：").append(image.getWidth()).append(" × ").append(image.getHeight()).append("\n");
                info.append("类型：").append(im.getImageType().toUpperCase()).append("\n");
                info.append("大小：").append(im.getFormatSize()).append("\n");
                info.append("日期：").append(im.getFormatTime()).append("\n");
                info.append("\n位置：").append(im.getImageFilePath());
                new CustomDialog(hc, DialogType.INFO, im,
                        im.getImageName(), info.toString()).show();
                imageBox.getPopUpMenu().hide();
                break;
            default:
        }
    }
}
