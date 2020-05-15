package home.java.components;


import com.jfoenix.controls.*;
import display.java.controllers.DisplayWindowController;
import home.java.controllers.AbstractController;
import home.java.controllers.ControllerUtil;
import home.java.controllers.HomeController;
import home.java.model.ImageModel;
import home.java.model.SelectedModel;
import javafx.scene.Node;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO 自定义、可复用的对话框
 *
 * @author Grey
 */
public class CustomDialog extends JFXDialog {

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

    @Getter
    private Label headingLabel;
    @Getter
    private Label bodyLabel;

    @Getter
    private JFXTextArea bodyTextArea;
    private JFXTextField bodyTextField;

    @Getter
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

        this.setOverlayClose(true);
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
            layout.setBody(bodyLabel);
        }
    }

    public void setBodyContent(Node... body){
        layout.setBody(body);
    }

    public void setLoadingSpinner() {
//        layout.getChildren().clear();
        JFXSpinner spinner = new JFXSpinner(-1);
        layout.setBody(spinner);
    }

    private void setBodyTextArea(String text) {
        bodyTextArea = new JFXTextArea(text);
        bodyTextArea.getStyleClass().addAll("dialog-text-area", "dialog-body");
        bodyTextArea.setEditable(false);
        layout.setBody(bodyTextArea);
    }

    /**
     * 重命名用到
     */
    private void setBodyTextField() {
        bodyTextField = new JFXTextField();
        bodyTextField.setText(targetImage.getImageName());
        bodyTextField.getStyleClass().addAll("rename-text-field", "dialog-body");
        layout.setBody(bodyTextField);
    }

    private void setCloseAction(JFXButton button) {
        button.setOnAction(event -> {
            this.close();
            System.out.println("关闭对话框");
        });
    }

    private void makeDeleteDialog() {
        rightButton.setText("删除");
//        rightButton.getStyleClass().clear();
//        rightButton.getStyleClass().add("dialog-confirm-red");
        rightButton.setStyle("-fx-text-fill: RED;");
        rightButton.setOnAction(event -> {
            int n;
            n = SelectedModel.deleteImage();
            if (n > 0) {
                controller.getSnackbar().enqueue(new JFXSnackbar.SnackbarEvent("成功删除 " + n + " 张图片"));    //显示删除成功通知。
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
            if (SelectedModel.renameImage(bodyTextField.getText()))
                controller.getSnackbar().enqueue(new JFXSnackbar.SnackbarEvent("重命名成功"));
            this.close();
            hc.refreshImagesList();
        });
    }

    private void makeInfoDialog() {
        rightButton.getStyleClass().add("dialog-confirm");
        rightButton.setText("确认");
    }

    private void makeReplaceDialog() {
        leftButton.setText("跳过");
        rightButton.setText("替换");
        rightButton.setStyle("-fx-text-fill: BLUE;");
        leftButton.setOnAction(event -> {
            System.out.println("选择跳过");
            this.close();
            controller.getSnackbar().enqueue(new JFXSnackbar.SnackbarEvent("选择跳过"));
            hc.refreshImagesList();
//            SelectedModel.setHavePastedNum(SelectedModel.getHavePastedNum()+1);
        });
        rightButton.setOnAction(event -> {
            System.out.println("选择替换");
            if (SelectedModel.replaceImage()) {
                controller.getSnackbar().enqueue(new JFXSnackbar.SnackbarEvent("替换成功"));
            }
            hc.refreshImagesList();
            this.close();
            SelectedModel.setHavePastedNum(SelectedModel.getHavePastedNum()+1);
        });
    }

    /**
     * 展示对话框
     */
    @Override
    public void show() {
        if (leftButton != null && rightButton != null)
            layout.setActions(leftButton, rightButton);
        else
            System.out.println("ERROR: 未指定对话框按钮");
        this.setContent(layout);
        this.show(controller.getRootPane());
    }

}
