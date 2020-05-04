package home.java.components;


import com.jfoenix.controls.*;
import display.java.controllers.DisplayWindowController;
import home.java.controllers.AbstractController;
import home.java.controllers.ControllerUtil;
import home.java.controllers.HomeController;
import home.java.model.ImageModel;
import home.java.model.SelectedModel;
import javafx.scene.control.Label;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TODO 自定义、可复用的对话框
 *
 * @author Grey
 */
public class CustomDialog {

    @Setter
    private ImageModel targetImage;

    private AbstractController controller;
    private HomeController hc = (HomeController) ControllerUtil.controllers.get(HomeController.class.getSimpleName());
    private DisplayWindowController dwc = (DisplayWindowController) ControllerUtil.controllers.get(DisplayWindowController.class.getSimpleName());

    private DialogType type;

    @Setter
    private String buttonText1;

    @Setter
    private String buttonText2;

    private JFXButton leftButton;
    private JFXButton rightButton;

    private Label heading;
    private Label body;

    @Setter
    private JFXTextField textField;

    private JFXDialog dialog = new JFXDialog();
    private JFXDialogLayout layout = new JFXDialogLayout();

    public CustomDialog(AbstractController controller, DialogType type, ImageModel targetImage) {
        this.controller = controller;
        this.type = type;
        this.targetImage = targetImage;
        leftButton = new JFXButton(buttonText1);
        setLeftButtAction();
        rightButton = new JFXButton(buttonText2);
        dialog.setOverlayClose(true);

        if (type == DialogType.DELETE) {
            rightButton.getStyleClass().add("dialog-confirm-red");
            rightButton.setText("删除");
//            setHeading("删除图片");
//            setBody("删除文件: " + targetImage.getImageName() + "\n\n你可以在回收站找回。");
            deleteAction();
        } else {
            rightButton.getStyleClass().add("dialog-confirm");
            rightButton.setText("确认");
            if (type == DialogType.RENAME) {
                renameAction();
            } else if (type == DialogType.REPLACE) {
                replaceAction();
            }
        }
        leftButton.getStyleClass().add("dialog-cancel");
        leftButton.setText("取消");
    }

    public CustomDialog(AbstractController controller, DialogType type, ImageModel targetImage,
                        String headingText, String bodyText) {

        this(controller, type, targetImage);

        setHeading(headingText);
        setBody(bodyText);

        layout.setHeading(heading);
        layout.setBody(body);
    }

    public void setHeading(String headingText) {
        heading = new Label(headingText);
        heading.getStyleClass().add("dialog-heading");
    }

    public void setBody(String bodyText) {
        body = new Label(bodyText);
        body.getStyleClass().add("dialog-body");
    }

    public void setLeftButtAction() {
        leftButton.setOnAction(event -> {
            dialog.close();
            System.out.println("关闭对话框");
        });
    }

    public void setTextField() {

    }

    public void deleteAction() {
        rightButton.setOnAction(event -> {
            SelectedModel.setSourcePath(targetImage.getImageFilePath());
            if (SelectedModel.deleteImage()) {
                controller.getSnackbar().enqueue(new JFXSnackbar.SnackbarEvent("删除成功"));    //显示删除成功通知。
            } else {
                controller.getSnackbar().enqueue(new JFXSnackbar.SnackbarEvent("删除失败"));    //显示删除成功通知。
            }
            hc.refreshImagesList();
            dialog.close();
        });
    }

    public void replaceAction() {
        rightButton.setOnAction(event -> {
            //TODO
            dialog.close();
        });
    }

    public void renameAction() {
        rightButton.setOnAction(event -> {
            //TODO
            dialog.close();
        });
    }

    /**
     * 展示对话框
     */
    public void show() {
        if (leftButton != null && rightButton != null)
            layout.setActions(leftButton, rightButton);
        else
            System.out.println("ERROR: 未指定对话框按钮");
        dialog.setContent(layout);
        dialog.show(controller.getRootPane());
    }

}
