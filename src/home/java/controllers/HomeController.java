package home.java.controllers;

import com.jfoenix.controls.*;
import home.java.components.ImageBox;
import home.java.components.ImageLabel;
import home.java.components.ImageView2;
import home.java.components.RipplerImageView;
import home.java.model.ImageListModel;
import home.java.model.ImageModel;
import home.java.model.SelectedModel;
import home.java.model.SortParam;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * 主窗口界面的控制器。
 *
 * @author Grey
 */

public class HomeController implements Initializable {
    @FXML
    private Label folderInfoLabel;

    @FXML
    private StackPane rootPane;

    @FXML
    private Image image;

    @FXML
    private ImageView2 imageView;

    @FXML
    private VBox imageVBox;

    @FXML
    private FlowPane imageListPane = new FlowPane();

    @FXML
    private ToolBar infoBar;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private JFXTreeView<File> fileTreeView;

    @FXML
    private JFXTextField pathLabel; //TODO 通过地址栏导航去指定目录 2020-4-7 11:49:32

    @FXML
    private JFXButton refreshButton;

    @FXML
    private AnchorPane folderPane;

    @FXML
    private SplitPane splitPane;

    @FXML
    private JFXComboBox<String> sortComboBox;

    @FXML
    private JFXDialog dialog;

    @Setter
    @Getter
    private boolean IsClickCombobox = false;

    public HomeController() {
    }

    /**
     * 初始化FXML文件
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Home Window init running...");

        //将本类的实例添加到全局映射中
        Util.controllers.put(this.getClass().getSimpleName(), this);

        setFileTreeView(); //初始化目录树
        infoBar.setBackground(Background.EMPTY); //信息栏设置透明背景

        imageListPane.setPadding(new Insets(10));
        imageListPane.setVgap(20);
        imageListPane.setHgap(20);

        scrollPane.setContent(imageListPane);

        SplitPane.setResizableWithParent(folderPane, false);

        refreshButton.setOnAction(event -> refreshImagesList());

        sortComboBox.getItems().addAll(SortParam.SBNR, SortParam.SBND, SortParam.SBSR, SortParam.SBSD, SortParam.SBDR, SortParam.SBDD);
        sortComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                refreshImagesList(newValue);
                if (!IsClickCombobox)
                    setIsClickCombobox(true);
            }

        });
        setWelcomePage();
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
    private void placeImages(ArrayList<ImageModel> imageModelList, String folderPath) {
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

//    private int loadPic(ArrayList<ImageModel> imageModelList, FlowPane imageListPane, int index){
//        //滚动时的刷新速度 一张一张
//        int speed = 1;
//        for (int i = index; i<=index+speed && i<imageModelList.size()  ; i++) {
//            ImageBox imageBox = new ImageBox(imageModelList.get(i)); //装图片和文件名的盒子，一上一下放置图片和文件名
//            imageListPane.getChildren().add(imageBox);
//        }
//        return index+speed;
//    }


    /**
     * 设置左侧文件目录树
     *
     * @author tudou daren
     */
    private void setFileTreeView() {
        //定义目录树
        File[] rootList = File.listRoots();
        TreeItem<File> a = new TreeItem<>(rootList[0]);

        for (File root : rootList) {
            TreeItem<File> c = new TreeItem<>(root);
            try {
                addItems(c, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            a.getChildren().add(c);
        }

        fileTreeView.setRoot(a);
        fileTreeView.setShowRoot(false);

        //自定义单元格，设置展开箭头为图片
        fileTreeView.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
            @Override
            public TreeCell<File> call(TreeView<File> param) {
                TreeCell<File> treeCell = new TreeCell<File>() {
                    @Override
                    protected void updateItem(File item, boolean empty) {

                        if (!empty) {
                            super.updateItem(item, empty);
                            HBox hBox = new HBox();
                            Label label = new Label(isListRoots(item));

                            this.setGraphic(hBox);

                            if (this.getTreeItem().isExpanded() ) {
                                ImageView folderImage = new ImageView("icons/opened_folder.png");
                                folderImage.setPreserveRatio(true);
                                folderImage.setFitWidth(22);
                                hBox.getChildren().add(folderImage);//加图片
                                //this.setDisclosureNode(folderImage);
                                this.setGraphic(hBox);
                            } else if (!this.getTreeItem().isExpanded() ) {
                                ImageView folderImage = new ImageView("icons/folder.png");
                                folderImage.setPreserveRatio(true);
                                folderImage.setFitWidth(22);
                                hBox.getChildren().add(folderImage);//加图片

                                this.setGraphic(hBox);
                            }
                            hBox.getChildren().add(label);//加文字
                        } else if (empty) {
                            this.setGraphic(null);

                        }
                    }
                };
                return treeCell;
            }
        });


        //获取点击操作并刷新当前结点
        fileTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<File>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<File>> observable, TreeItem<File> oldValue, TreeItem<File> newValue) {
                //此处点击可获得文件夹绝对路径
                String path = newValue.getValue().getAbsolutePath();
                System.out.println(path);
                try {
                    placeImages(ImageListModel.initImgList(path), path);
                    // 只要点击一次排序后以后每次进入新页面就置为"默认排序"
                    if (IsClickCombobox)
                        sortComboBox.setValue("默认排序");
                    addItems(newValue, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //递归将节点加入目录树中
    public void addItems(TreeItem<File> in, int flag) throws IOException {
        File[] filelist = in.getValue().listFiles();
        //flag判断当前遍历的层数
        if (filelist != null) {
            if (flag == 0) {
                in.getChildren().remove(0, in.getChildren().size());
            }

            if (filelist.length > 0) {
                for (int i = 1; i < filelist.length; i++) {
                    if (filelist[i].isDirectory()  & !filelist[i].isHidden()) {
                        TreeItem<File> b = new TreeItem<File>(filelist[i]);
                        if (flag < 1) {
                            addItems(b, flag + 1);
                        }
                        in.getChildren().add(b);
                    }
                }
            }
        }
    }

    //判断是否为根目录
    public String isListRoots(File item) {
        File[] rootlist = File.listRoots();
        for (File isListRoots : rootlist) {
            if (item.toString().equals(isListRoots.toString())) {
                return item.toString();
            }
        }
        return item.getName();
    }

    //确认删除的对话框
    public void callDeleteDialog(ImageModel im) {

        JFXButton confirm = new JFXButton("删除");
        JFXButton cancel = new JFXButton("取消");
        confirm.getStyleClass().add("dialog-confirm");
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
            System.out.println("取消删除。");
        });

        confirm.setOnAction(event -> {
            SelectedModel.setSourcePath(im.getImageFilePath());
            SelectedModel.deleteImage();
            dialog.close();
            System.out.println("删除成功!");
        });

        layout.setActions(cancel, confirm);
        dialog.setContent(layout);
        dialog.show(rootPane);
    }


}
