package home.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import home.java.components.DeleteDialogController;
import home.java.components.ImageBox;
import home.java.model.ImageModel;
import home.java.model.SelectedModel;

import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Optional;

public class PopupMenuController {

    @FXML
    private JFXListView<?> popupList;

    @FXMLViewFlowContext
    private ViewFlowContext context;

    ImageModel im;
    ImageBox imageBox;
    JFXDialog dialog;

    public PopupMenuController(ImageBox imageBox) {
        this.imageBox = imageBox;
        this.im = imageBox.getIm();
        try {
            setDeleteDialog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void action() {
        switch (popupList.getSelectionModel().getSelectedIndex()) {
            case 0:
                System.out.println("点击复制 复制图片源:" + im.getImageFilePath());
                SelectedModel.sourceImage(im.getImageFilePath());
                imageBox.getPopUpMenu().hide();
                break;
            case 1:
                System.out.println("点击剪切 剪切图片源:" + im.getImageFilePath());
                SelectedModel.sourceImage(im.getImageFilePath());
                imageBox.getPopUpMenu().hide();
                break;
            case 2:
                System.out.println("点击重命名 重命名图片源:" + im.getImageFilePath());
                // TODO 重命名输入名字
                imageBox.getPopUpMenu().hide();
                break;
            case 3:
                //TODO 弹窗修改为Material Design样式
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
                imageBox.getPopUpMenu().hide();
//                dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
//                dialog.show((StackPane) context.getRegisteredObject("ContentPane"));
                break;
            default:
        }
    }

    private void setDeleteDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DeleteDialog.fxml"));
        loader.setController(new DeleteDialogController(this.imageBox));
        dialog = new JFXDialog(new StackPane(),loader.load(), JFXDialog.DialogTransition.CENTER);
    }
}
