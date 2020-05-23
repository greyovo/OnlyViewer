package home.java.controllers;

import com.jfoenix.controls.*;
import home.java.components.*;
import home.java.model.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * 主窗口界面的控制器
 *
 * @see home.java.controllers.AbstractController
 * @author Grey
 * @since 2020.03
 */

public class HomeController extends AbstractController implements Initializable {

    //文件夹工具(信息)栏功能按钮
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
    public AnchorPane anchorPane;
    @FXML
    public JFXButton selectAllButton;
    @FXML
    @Getter
    private JFXButton refreshButton;

    @FXML
    //缩略图上方工具(信息)栏
    private ToolBar infoBar;

    //状态信息
    @FXML
    private Label folderInfoLabel;
    @FXML
    public Label selectedNumLabel;

    @FXML
    @Getter
    private JFXComboBox<String> sortComboBox;
    @Getter
    @Setter
    private boolean comboBoxClicked = false;
    @FXML
    private JFXTextField pathTextField;
    @Getter
    private JFXSnackbar snackbar; //下方通知条

    //面板
    @FXML
    @Getter
    private StackPane rootPane;
    @FXML
    private FlowPane imageListPane = new FlowPane();
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane folderPane;

    //存储信息的变量
    private String currentPath = "";
    @Getter
    private Stack<String> pathStack1 = new Stack<>();
    @Getter
    private Stack<String> pathStack2 = new Stack<>();
    //当前图片列表
    private ArrayList<ImageModel> curImgList = new ArrayList<>();

    public HomeController() {
        //将本类的实例添加到全局映射中
        ControllerUtil.controllers.put(this.getClass().getSimpleName(), this);
    }

    /**
     * 初始化FXML文件
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageListPane.setPadding(new Insets(10));
        imageListPane.setVgap(10);
        imageListPane.setHgap(10);
        imageListPane.setCache(true);
        SplitPane.setResizableWithParent(folderPane, false);

        snackbar = new JFXSnackbar(rootPane);
        infoBar.setBackground(Background.EMPTY); //信息栏设置透明背景
        closeSearchButton.setVisible(false);

        initPasteButton();
        initSortComboBox();
        initIntroPage();       //设置欢迎页必须在scrollPane之后设置，否则会被imageListPane空白页覆盖
        initSearchTextField();
        initPathTextField();
        System.out.println("Main window initialization done...");
    }

    // 缩略图面板层级（从底到顶）:
    // AnchorPane > ScrollPane > FlowPane(imageListPane)

    /**
     * 生成并往面板中放置图像组。
     * 一个缩略图单元{@link ImageBox}包含：
     * 一个图片ImageView（由{@link WhiteRippler}包装从而实现水波纹效果）和一个标签 {@link ImageLabel}
     */
    public void placeImages(ArrayList<ImageModel> imageModelList, String folderPath) {
        //检查列表是否空，因为有可能处于初始页面
        if (imageModelList == null)
            return;

        // 每次生成前重置
        imageListPane.getChildren().clear();
        scrollPane.setContent(imageListPane);
        SelectionModel.clear();
        unSelectAll();
        sortComboBox.setVisible(true); //默认排序盒子不出现，除非触发了构建缩略图操作

        //设置初始加载数目,更改时需要更改滚动内的初始index值
        int firstLoad = Math.min(imageModelList.size(), 80);    // 修改了firstLoad 取值为列表与80之间的最小值

        //更新当前地址，并检测入栈
        pathTextField.setText(folderPath);
        currentPath = folderPath;

        //文件夹信息栏设置
        if (imageModelList.isEmpty()) {
            folderInfoLabel.setText("此文件夹下无可识别图片");
            return;
        } else {
            int total = ImageListModel.getListImgNum(imageModelList);
            String size = ImageListModel.getListImgSize(imageModelList);
            folderInfoLabel.setText(String.format("%d 张图片，共 %s ", total, size));
            selectedNumLabel.setText("| 已选中 0 张");
        }

        //TODO 新建新线程执行加载任务 可大幅缓解在图片较多时的卡顿情况
        //初始加载 firstload 张缩略图
        int i;
        for (i = 0; i < firstLoad; i++) {
            ImageBox imageBox = new ImageBox(imageModelList.get(i)); //装图片和文件名的盒子，一上一下放置图片和文件名
            imageListPane.getChildren().add(imageBox);
        }

        //加载后续的缩略图
        //2020/5/11 可以尝试设置在等待一段时间后，断续加载后面的而不用等滑动事件触发
        imageListPane.setOnScroll(new EventHandler<ScrollEvent>() {
            //初始加载后的位置
            int index = firstLoad - 1;

            @Override
            public void handle(ScrollEvent event) {
                int times = 30; // 每次滚动加载30张
                while (times > 0) {
                    index++;
                    if (event.getDeltaY() <= 0 && index < imageModelList.size()) {
//                    WAR/WAW ERROR
//                    index = loadPic(imageModelList, imageListPane, index);
                        ImageBox imageBox = new ImageBox(imageModelList.get(index)); //装图片和文件名的盒子，一上一下放置图片和文件名
                        imageListPane.getChildren().add(imageBox);
                    } else {
                        break;
                    }
                    times--;
                }

            }
        });
    }

    /**
     * 排序当前图片列表并刷新图片列表
     */
    public void refreshImagesList(String sort) {
        SelectionModel.clear();
        SelectedModel.getSourcePathList().clear();
        curImgList = ImageListModel.refreshList(currentPath, sort);
        placeImages(curImgList, currentPath);
    }

    public void initEnterFolder(String path) {
        currentPath = path;
        //入栈以便于后续前进后退
        if (pathStack1.isEmpty() || !pathStack1.peek().equals(path)) {
            pathStack1.push(path);
            pathStack2.clear();
        }
        placeImages(ImageListModel.refreshList(currentPath), currentPath);
    }

    // 以下是一些初始化操作----------------

    /**
     * 在初始启动时显示欢迎页面
     */
    @FXML
    private void initIntroPage() {
        ImageView welcomeImage = new ImageView(new Image("/home/resources/images/intro.png"));
        welcomeImage.setFitWidth(850);
        welcomeImage.setPreserveRatio(true);
        HBox hBox = new HBox(welcomeImage);
        hBox.setAlignment(Pos.CENTER);
        StackPane stackPane = new StackPane(hBox);
        scrollPane.setContent(stackPane);
    }

    public void showNotFoundPage() {
        ImageView img = new ImageView(new Image("/home/resources/images/no_result.png"));
        img.setFitHeight(500);
        img.setPreserveRatio(true);
        HBox hBox = new HBox(img);
        hBox.setAlignment(Pos.CENTER);
        StackPane stackPane = new StackPane(hBox);
        scrollPane.setContent(stackPane);
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

    private void initSearchTextField() {
        searchTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                searchImage();
        });
    }

    private void initPathTextField() {
        pathTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                gotoPath();
        });
    }

    private void initPasteButton() {
        if (SelectedModel.getSourcePath() == null || SelectedModel.getCopyOrMove() == -1) {
            pasteButton.setDisable(true);
        }
    }

    //各按钮事件操作------------------

    /**
     * 前进、后退和返回父目录
     */
    @FXML
    private void moveBack() {
        if (!pathStack1.isEmpty()) {
            if (pathStack1.peek().equals(currentPath)) {
                pathStack1.pop();
            }
            String path = pathStack1.pop();
            pathStack2.push(currentPath);
            placeImages(ImageListModel.refreshList(path), path);
        } else {
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("后退到底啦"));
        }
    }

    @FXML
    private void moveForward() {
        if (!pathStack2.isEmpty()) {
            String path = pathStack2.pop();
            pathStack1.push(currentPath);
            placeImages(ImageListModel.refreshList(path), path);
        } else {
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("前进到尽头啦"));
        }
    }

    @FXML
    private void toParentDir() {
        String parent;
        if (currentPath.lastIndexOf("\\") == 2) {
            if (currentPath.length() == 3) {
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent("到达根目录"));
                return;
            } else {
                parent = currentPath.substring(0, currentPath.lastIndexOf("\\") + 1);
            }
        } else {
            parent = currentPath.substring(0, currentPath.lastIndexOf("\\"));
        }
        placeImages(ImageListModel.refreshList(parent), parent);
        pathStack1.push(parent);
    }

    /**
     * 地址栏导航操作
     */
    @FXML
    private void gotoPath() {
        String path = pathTextField.getText();
        //用于处理以反斜杠 "\" 结尾的情况，需去掉反斜杠
        //(导航至磁盘根目录 即路径地址长度等于3的情况 除外)
        if (path.endsWith("\\") && path.length() != 3)
            path = path.substring(0, path.length() - 1);

        File directory = new File(path);
        if (!directory.exists()) {
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("路径不正确"));
        } else {
            ArrayList<ImageModel> list = ImageListModel.refreshList(path);
            // placeImages方法中已处理列表为空时的情况
            if (list != null)
                placeImages(list, path);
        }
    }

    /**
     * 刷新按钮操作
     */
    @FXML
    private void refresh() {
        unSelectAll();
        closeSearch();
        refreshImagesList(sortComboBox.getValue());
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent("已刷新"));
    }

    /**
     * 粘贴按钮操作
     */
    @FXML
    private void paste() {
        if (SelectionModel.getImageModelList().isEmpty()) {
            // 粘贴一张图片
            SelectedModel.setWaitingPasteNum(1);
        } else {
            // 粘贴多张图片
            SelectedModel.setWaitingPasteNum(SelectionModel.getImageModelList().size());
        }
        SelectedModel.pasteImage(currentPath);
        if (SelectedModel.getHavePastedNum() == SelectedModel.getWaitingPasteNum()) {
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("粘贴成功"));
            refreshImagesList(sortComboBox.getValue());
        }
        if (SelectedModel.getSourcePath() == null || SelectedModel.getCopyOrMove() == -1) {
            pasteButton.setDisable(true);
        }
    }

    @FXML
    private void searchImage() {
        String key = searchTextField.getText();
        if (key.equals("")) return; // 搜索内容为空时即返回
        ArrayList<ImageModel> result =
                SearchImageModel.fuzzySearch(key, curImgList);
        placeImages(result, currentPath);
        if (result.size() == 0) {
            folderInfoLabel.setText("未找到图片");
            showNotFoundPage();
        } else {
            folderInfoLabel.setText("共找到 " + result.size() + " 个结果");
        }
        closeSearchButton.setVisible(true);
    }

    /**
     * 关闭搜索
     */
    @FXML
    private void closeSearch() {
        closeSearchButton.setVisible(false);
        searchTextField.setText("");
        refreshImagesList(sortComboBox.getValue());
    }

    /**
     * 全选按钮
     */
    @FXML
    private void selectAll() {
        //如果没有可选的内容
        if (imageListPane.getChildren().isEmpty()) {
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("无内容可选"));
            return;
        }
        //避免重复选择，先清空已选
        SelectionModel.clear();
        for (Node node : imageListPane.getChildren()) {
            ImageBox imageBox = (ImageBox) node;
            imageBox.getCheckBox().setSelected(true);
        }
    }

    /**
     * 清空选择
     */
    @FXML
    private void unSelectAll() {
        SelectionModel.clear();
    }

    /**
     * 展示软件关于页面
     */
    @FXML
    private void showAboutDetail() {
        VBox vBox = new VBox();

        ImageView icon = new ImageView(new Image("home/resources/icons/app_icon_300px.png"));
        icon.setFitHeight(100);
        icon.setPreserveRatio(true);

        Label author = new Label("Made with ♥ by\n" + "Kevin & Grey & tudou daren\n\n");
        author.getStyleClass().add("normal-text-b");
        author.setTextAlignment(TextAlignment.CENTER);

        String repo = "Source code:\n" +
                "https://github.com/greyovo/onlyviewer\n" +
                "https://gitee.com/kevin996/OnlyViewer\n";

        JFXTextArea bodyTextArea = new JFXTextArea(repo);
        bodyTextArea.getStyleClass().addAll("dialog-text-area", "dialog-body");
        bodyTextArea.setEditable(false);
        bodyTextArea.setPrefHeight(100);

        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(icon, author, bodyTextArea);

        CustomDialog dialog = new CustomDialog(this, DialogType.INFO, null, "关于 OnlyViewer");
        dialog.setBodyContent(vBox);
        dialog.show();
    }

}
