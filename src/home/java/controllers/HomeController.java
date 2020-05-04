package home.java.controllers;

import com.jfoenix.controls.*;
import home.java.components.ImageBox;
import home.java.components.ImageLabel;
import home.java.components.RipplerImageView;
import home.java.model.ImageListModel;
import home.java.model.ImageModel;
import home.java.model.SelectedModel;
import home.java.model.SortParam;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;

import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 主窗口界面的控制器。
 *
 * @author Grey
 */

public class HomeController implements Initializable {
    @FXML
    public JFXButton pasteButton;

    @FXML
    private Label folderInfoLabel;

    @FXML
    @Getter
    private StackPane rootPane;

    @FXML
    private FlowPane imageListPane = new FlowPane();

    @FXML
    private ToolBar infoBar; //文件夹上方信息栏 位于刷新按钮左侧

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private JFXTextField pathLabel; //TODO 通过地址栏导航去指定目录 2020-4-7 11:49:32

    @FXML
    private JFXButton refreshButton;

    @FXML
    private AnchorPane folderPane;

    @FXML
    private SplitPane splitPane;

    @FXML
    @Getter
    private JFXComboBox<String> sortComboBox;

    @Getter
    @Setter
    private boolean comboBoxClicked = false;

    @Getter
    private JFXSnackbar snackbar; //下方通知条

    public HomeController() {
        //将本类的实例添加到全局映射中
        ControllerUtil.controllers.put(this.getClass().getSimpleName(), this);
        System.out.println("put HomeCon in Map...");
    }

    /**
     * 初始化FXML文件
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Home Window started running...");

        snackbar = new JFXSnackbar(rootPane);
        infoBar.setBackground(Background.EMPTY); //信息栏设置透明背景

        imageListPane.setPadding(new Insets(10));
        imageListPane.setVgap(20);
        imageListPane.setHgap(20);

        initSortComboBox();
        setWelcomePage();

        scrollPane.setContent(imageListPane);
        SplitPane.setResizableWithParent(folderPane, false);
    }

    /**
     * 刷新按钮操作
     */
    @FXML
    private void refresh() {
        refreshImagesList();
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent("已刷新"));
    }

    /**
     * 粘贴按钮操作
     */
    @FXML
    private void paste() {
        if (SelectedModel.pasteImage(pathLabel.getText())) {
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("粘贴成功"));
        } else {
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("粘贴失败"));
        }
        refreshImagesList();
    }

    /**
     * 初始化排序下拉盒子
     */
    private void initSortComboBox() {
        sortComboBox.getItems().addAll(SortParam.SBNR, SortParam.SBND, SortParam.SBSR, SortParam.SBSD, SortParam.SBDR, SortParam.SBDD);
        sortComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                refreshImagesList(newValue);
                if (!comboBoxClicked)
                    setComboBoxClicked(true);
            }
        });
    }

    /**
     * 在初始启动时显示欢迎页面
     */
    private void setWelcomePage() {
        ImageView welcomeImage = new ImageView(new Image("home/resources/images/welcome.png"));
        welcomeImage.setFitWidth(400);
        welcomeImage.setPreserveRatio(true);
        HBox hBox = new HBox(welcomeImage);
        hBox.setAlignment(Pos.CENTER);
        StackPane stackPane = new StackPane(hBox);
        scrollPane.setContent(stackPane);
    }


    /**
     * 更新当前图片列表
     */
//    private void refreshImagesList(String url) {
//        placeImages(ImageListModel.refreshList(url), url);
//    }
    private void refreshImagesList() {
        placeImages(ImageListModel.refreshList(pathLabel.getText()), pathLabel.getText());
        System.out.println("已刷新。");
    }

    private void refreshImagesList(String sort) {
        placeImages(ImageListModel.sortList(pathLabel.getText(), sort), pathLabel.getText());
        System.out.println("已排序。");
    }

    /**
     * 生成并往面板中放置图像组。
     * 一个缩略图单元包含：一个图片ImageView（由{@link RipplerImageView}包装从而实现水波纹效果）和一个标签 {@link ImageLabel}
     */
    public void placeImages(ArrayList<ImageModel> imageModelList, String folderPath) {
        // 每次点击就重置
        imageListPane.getChildren().clear();
        scrollPane.setContent(imageListPane);
        //设置初始加载数目,更改时需要更改滚动内的初始index值！！
        // 修改了firstLoad 取值为列表与15之间的最小值
        int firstLoad = Math.min(imageModelList.size(), 15);
        //地址栏更新
        pathLabel.setText(folderPath);

        //文件夹信息栏设置
        if (imageModelList.isEmpty()) {
            folderInfoLabel.setText("此文件夹下无可识别图片");
        } else {
            int total = ImageListModel.getListImgNum(imageModelList);
            String size = ImageListModel.getListImgSize(imageModelList);
            folderInfoLabel.setText(total + " 张图片，共 " + size);
            System.out.println(imageModelList);
        }

        //初始加载缩略图
        for (int i = 0; i < firstLoad; i++) {
            ImageBox imageBox = new ImageBox(imageModelList.get(i)); //装图片和文件名的盒子，一上一下放置图片和文件名
            imageListPane.getChildren().add(imageBox);
        }

        //加载缩略图
        imageListPane.setOnScroll(new EventHandler<ScrollEvent>() {
            //初始加载后的位置
            int index = firstLoad - 1;

            @Override
            public void handle(ScrollEvent event) {
                index++;
                if (event.getDeltaY() <= 0 && index < imageModelList.size()) {
//                    WAR/WAW ERROR
//                    index = loadPic(imageModelList, imageListPane, index);
                    ImageBox imageBox = new ImageBox(imageModelList.get(index)); //装图片和文件名的盒子，一上一下放置图片和文件名
                    imageListPane.getChildren().add(imageBox);
                }
            }
        });

    }

    /**
     * 确认删除的对话框
     */
    //TODO 提高复用性 将其写成组件类
    public void callDeleteDialog(ImageModel im) {

        JFXButton confirm = new JFXButton("删除");
        JFXButton cancel = new JFXButton("取消");
        confirm.getStyleClass().add("dialog-confirm-red");
        cancel.getStyleClass().add("dialog-cancel");

        Label heading = new Label("确认删除");
        heading.getStyleClass().add("dialog-heading");

        Label body = new Label("删除文件: " + im.getImageName() + "\n\n此操作不可逆。");
        body.getStyleClass().add("dialog-body");

        JFXDialog dialog = new JFXDialog();
        dialog.setOverlayClose(true);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(heading);
        layout.setBody(body);

        cancel.setOnAction(event -> {
            dialog.close();
            System.out.println("取消删除");
        });

        confirm.setOnAction(event -> {
            SelectedModel.setSourcePath(im.getImageFilePath());
            if (SelectedModel.deleteImage()) {
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent("删除成功"));    //显示删除成功通知。
                refreshImagesList();
            }
            dialog.close();
        });

        layout.setActions(cancel, confirm);
        dialog.setContent(layout);
        dialog.show(rootPane);
    }

    /**
     * 确认替换对话框
     */
    //FIXME 还没想到怎么将按钮事件回传
    //TODO 提高复用性 将其写成组件类
    public boolean callReplaceDialog(ImageModel im) {
        AtomicBoolean replace = new AtomicBoolean(false);

        JFXButton confirm = new JFXButton("替换");
        JFXButton cancel = new JFXButton("取消");
        confirm.getStyleClass().add("dialog-confirm");
        cancel.getStyleClass().add("dialog-cancel");

        Label heading = new Label("存在同名文件");
        heading.getStyleClass().add("dialog-heading");

        Label body = new Label("存在同名文件，是否替换？此操作不可逆。");
        body.getStyleClass().add("dialog-body");

        JFXDialog dialog = new JFXDialog();
        dialog.setOverlayClose(true);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(heading);
        layout.setBody(body);

        cancel.setOnAction(event -> {
            dialog.close();
            System.out.println("取消替换。");
            replace.set(false);
        });

        confirm.setOnAction(event -> {
            SelectedModel.setSourcePath(im.getImageFilePath());
            SelectedModel.deleteImage();
            dialog.close();
            replace.set(true);
            System.out.println("替换成功!");
        });

        layout.setActions(cancel, confirm);
        dialog.setContent(layout);
        dialog.show(rootPane);

        return replace.get();
    }

    /**
     * 通过图片名字查找图片（精准匹配）
     */
    private ImageModel findPic(ArrayList<ImageModel> imageModelList, String picName) {
        for (ImageModel im : imageModelList) {
            if (im.getImageName().equals(picName)) {
                return im;
            }
        }
        return null;
    }


}
