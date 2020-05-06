package home.java.controllers;

import com.jfoenix.controls.*;
import home.java.components.ImageBox;
import home.java.components.ImageLabel;
import home.java.components.RipplerImageView;
import home.java.model.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;

import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 主窗口界面的控制器。
 *
 * @author Grey
 */

public class HomeController extends AbstractController implements Initializable {
    @FXML
    @Getter
    public JFXButton pasteButton;

    @FXML
    public JFXTextField searchTextField;
    @FXML
    public JFXButton closeSearchButton;
    @FXML
    public JFXButton gotoButton;

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
    @Getter
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

    private String currentPath;

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

        scrollPane.setContent(imageListPane);
        SplitPane.setResizableWithParent(folderPane, false);
        closeSearchButton.setVisible(false);
        searchTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                searchImage();
        });
        pathLabel.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                gotoPath();
        });

        initSortComboBox();
        setWelcomePage();       //设置欢迎页必须在scrollPane之后设置，否则会被imageListPane空白页覆盖

        if (SelectedModel.getSourcePath() == null || SelectedModel.getOption() == -1) {
            pasteButton.setDisable(true);
        }
    }

    /**
     * 生成并往面板中放置图像组。
     * 一个缩略图单元{@link ImageBox}包含：一个图片ImageView（由{@link RipplerImageView}包装从而实现水波纹效果）和一个标签 {@link ImageLabel}
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
        currentPath = folderPath;

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
            ImageBox imageBox = new ImageBox(imageModelList.get(i), imageModelList); //装图片和文件名的盒子，一上一下放置图片和文件名
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
                    ImageBox imageBox = new ImageBox(imageModelList.get(index), imageModelList); //装图片和文件名的盒子，一上一下放置图片和文件名
                    imageListPane.getChildren().add(imageBox);
                }
            }
        });

    }

    /**
     * 更新当前图片列表
     */
    public void refreshImagesList() {
        placeImages(Objects.requireNonNull(ImageListModel.refreshList(currentPath)), currentPath);
        System.out.println("已刷新。");
    }

    /**
     * 在初始启动时显示欢迎页面
     */
    private void setWelcomePage() {
        ImageView welcomeImage = new ImageView(new Image("/home/resources/images/welcome.png"));
        welcomeImage.setFitWidth(400);
        welcomeImage.setPreserveRatio(true);
        HBox hBox = new HBox(welcomeImage);
        hBox.setAlignment(Pos.CENTER);
        StackPane stackPane = new StackPane(hBox);
        scrollPane.setContent(stackPane);
        System.out.println(welcomeImage);
    }

    @FXML
    private void gotoPath() {
        String path = pathLabel.getText();
        ArrayList<ImageModel> list = ImageListModel.refreshList(path);
        if (list != null) {
            placeImages(list, path);
        } else {
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("路径不正确"));
        }
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
        if (SelectedModel.getSourcePath() == null || SelectedModel.getOption() == -1) {
            pasteButton.setDisable(true);
        }
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

    @FXML
    private void searchImage() {
        String key = searchTextField.getText();
        ArrayList<ImageModel> result =
                SearchImageModel.fuzzySearch(key, ImageListModel.refreshList(currentPath));
        placeImages(result, currentPath);
        if (result.size() == 0) {
            folderInfoLabel.setText("未找到图片");
        } else {
            folderInfoLabel.setText("共找到 " + result.size() + " 个结果");
        }
        closeSearchButton.setVisible(true);
    }

    @FXML
    private void closeSearch() {
        closeSearchButton.setVisible(false);
        searchTextField.setText("");
        refreshImagesList();
    }


    /**
     * 排序当前图片列表并更新到页面
     */
    private void refreshImagesList(String sort) {
        placeImages(ImageListModel.sortList(currentPath, sort), currentPath);
        System.out.println("已排序。");
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
