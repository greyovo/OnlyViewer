package home.java.components;


import com.jfoenix.controls.*;
import display.java.controllers.DisplayWindowController;
import home.java.controllers.AbstractController;
import home.java.controllers.ControllerUtil;
import home.java.controllers.HomeController;
import home.java.model.ImageListModel;
import home.java.model.ImageModel;
import home.java.model.SelectedModel;
import javafx.scene.control.Label;
import lombok.Setter;

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

    private Label headingLabel;
    private Label bodyLabel;

    private JFXTextArea bodyTextArea;
    private JFXTextField bodyTextField;

    private JFXDialog dialog = new JFXDialog();
    private JFXDialogLayout layout = new JFXDialogLayout();

    /**
     * @param controller  对话框出现所在的界面的控制器
     *                    如需要在主界面弹出，则传入{@linkplain HomeController}的实例
     * @param type        对话框种类，详见{@link DialogType}
     * @param targetImage 待处理的目标图片对象
     */
    public CustomDialog(AbstractController controller, DialogType type, ImageModel targetImage) {
        this.controller = controller;
        this.type = type;
        this.targetImage = targetImage;

        leftButton = new JFXButton(buttonText1);
        rightButton = new JFXButton(buttonText2);
        leftButton.getStyleClass().add("dialog-cancel");
        leftButton.setText("取消");
        rightButton.getStyleClass().add("dialog-confirm");
        rightButton.setText("确认");
        setCloseAction(leftButton);
        setCloseAction(rightButton);

        dialog.setOverlayClose(true);
        layout.setMaxWidth(500);

        if (type == DialogType.DELETE) {
            makeDeleteDialog();
        } else if (type == DialogType.RENAME) {
            makeRenameDialog();
        } else if (type == DialogType.REPLACE) {
            makeReplaceDialog();
        } else {
            makeInfoDialog();
        }
    }


    /**
     * @param controller  对话框出现所在的界面的控制器
     *                    如需要在主界面弹出，则传入{@linkplain HomeController}的实例
     * @param type        对话框种类，详见{@link DialogType}
     * @param targetImage 待处理的目标图片对象
     * @param headingText 标题
     */
    public CustomDialog(AbstractController controller,
                        DialogType type, ImageModel targetImage,
                        String headingText) {
        this(controller, type, targetImage);
        setHeadingLabel(headingText);
    }

    /**
     * @param bodyText 正文
     */
    public CustomDialog(AbstractController controller,
                        DialogType type, ImageModel targetImage,
                        String headingText, String bodyText) {
        this(controller, type, targetImage, headingText);
        setBodyLabel(bodyText);
    }


    public void setHeadingLabel(String headingText) {
        headingLabel = new Label(headingText);
        headingLabel.getStyleClass().add("dialog-heading");
        layout.setHeading(headingLabel);
    }

    public void setBodyLabel(String bodyText) {
        bodyLabel = new Label(bodyText);
        bodyLabel.getStyleClass().add("dialog-body");
        if (type == DialogType.INFO) {
            setBodyTextArea(bodyText);
        } else {
            layout.getChildren().add(bodyLabel);
        }
    }

    private void setBodyTextArea(String text) {
        bodyTextArea = new JFXTextArea(text);
        bodyTextArea.getStyleClass().add("dialog-text-area");
        bodyTextArea.setEditable(false);
        layout.setBody(bodyTextArea);
//        layout.getChildren().add(bodyTextArea);
    }

    /**
     * 重命名用到
     */
    private void setBodyTextField() {
        bodyTextField = new JFXTextField();
        bodyTextField.setText(targetImage.getImageName());
        bodyTextField.getStyleClass().add("rename-text-field");
        bodyTextField.getStyleClass().add("dialog-body");
//        bodyTextField.setAccessibleText("请输入新名称");
//        layout.getChildren().add(bodyTextField);
        layout.setBody(bodyTextField);
    }

    private void setCloseAction(JFXButton button) {
        button.setOnAction(event -> {
            dialog.close();
            System.out.println("关闭对话框");
        });
    }

    private void makeDeleteDialog() {
        rightButton.getStyleClass().clear();
        rightButton.getStyleClass().add("dialog-confirm-red");
        rightButton.setText("删除");
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

    private void makeRenameDialog() {
        setBodyTextField();
        rightButton.setOnAction(event -> {
            SelectedModel.renameImage(bodyTextField.getText());
            dialog.close();
        });
    }

    private void makeInfoDialog() {
        rightButton.getStyleClass().add("dialog-confirm");
        rightButton.setText("确认");
    }

    private void makeReplaceDialog() {
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
