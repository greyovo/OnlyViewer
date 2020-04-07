package home.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeView;
import home.java.components.ImageBox;
import home.java.components.ImageLabel;
import home.java.components.ImageView2;
import home.java.components.RipplerImageView;
import home.java.model.ImageListModel;
import home.java.model.ImageModel;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import org.junit.Test;

import javax.annotation.PostConstruct;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * 主窗口界面的控制器
 *
 * @author Grey
 * @since 2020.04.02
 */

@ViewController(value = "/fxml/Home.fxml", title = "Home Window")
public class HomeController {

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    private Label folderInfoLabel;

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
    private JFXTextField pathLabel;

    @FXML
    private JFXButton refreshButton;

    public HomeController() {
    }

    /**
     * 初始化FXML文件
     */
    @PostConstruct
    public void init() throws Exception {
        System.out.println("Home Window init running...");

        setWelcomePage();
        setFileTreeView(); //初始化目录树
        infoBar.setBackground(Background.EMPTY); //信息栏设置透明背景

        imageListPane.setPadding(new Insets(10));
        imageListPane.setVgap(20);
        imageListPane.setHgap(20);
        imageListPane.setPrefWidth(scrollPane.getPrefWidth());

        scrollPane.setContent(imageListPane);
        scrollPane.setStyle("-fx-background-color: transparent;-fx-control-inner-background: transparent;"); //隐藏边框

        refreshButton.setOnAction(event -> refreshImagesList());
    }

    /**
     * TODO 在无照片显示时显示欢迎页面 2020-4-7 00:17:17
     */
    private void setWelcomePage() {
        ImageView welcomeImage = new ImageView(new Image("home/resources/icons/app.png"));
        Label welcomeLabel = new Label("欢迎使用!");
        welcomeLabel.setFont(new Font("default", 22));
        VBox vBox = new VBox(welcomeImage, welcomeLabel);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefSize(imageListPane.getPrefWidth(), imageListPane.getPrefHeight());
        StackPane stackPane = new StackPane(vBox);
        imageListPane.getChildren().add(stackPane);
    }

    /**
     * 更新当前图片列表
     *
     * @param url 需要刷新的文件夹路径
     */
    private void refreshImagesList(String url) {
        placeImages(ImageListModel.refreshList(url), url);
    }

    private void refreshImagesList() {
        placeImages(ImageListModel.refreshList(pathLabel.getText()), pathLabel.getText());
        System.out.println("已刷新。");
    }

    /**
     * 生成并往面板中放置图像组。
     * 一个缩略图单元包含：一个图片ImageView（由{@link RipplerImageView}包装从而实现水波纹效果）和一个标签 {@link ImageLabel}
     */
    private void placeImages(ArrayList<ImageModel> imageModelList, String folderPath) {
        // 每次点击就重置
        imageListPane.getChildren().clear();

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

        ArrayList<ImageBox> list = new ArrayList<>();
        //加载缩略图
        for (ImageModel im : imageModelList) {
            ImageBox imageBox = new ImageBox(im); //装图片和文件名的盒子，一上一下放置图片和文件名
            imageListPane.getChildren().add(imageBox);
        }
    }


    /**
     * 设置左侧文件目录树
     *
     * @author tudou daren
     */
    private void setFileTreeView() {

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

        //将节点输出为文件名称
        fileTreeView.setCellFactory(TextFieldTreeCell.forTreeView(new StringConverter<File>() {

            //FileSystemView fsv = FileSystemView.getFileSystemView();
            @Override
            public String toString(File object) {
                //简单判断是否为根目录，是则换一种方式显示
                for (File isListRoots : rootList) {
                    if (object.toString().equals(isListRoots.toString())) {
                        //return fsv.getSystemDisplayName(object);
                        return object.toString();
                    }
                }
                return object.getName();
                //return fsv.getSystemDisplayName(object);
            }

            @Override
            public File fromString(String string) {
                return null;
            }
        }));

        //获取点击操作并刷新当前结点
        fileTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<File>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<File>> observable, TreeItem<File> oldValue, TreeItem<File> newValue) {
                String path = newValue.getValue().getAbsolutePath();
                System.out.println(path);
                try {
                    placeImages(ImageListModel.initImgList(path), path);
                    addItems(newValue, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addItems(TreeItem<File> in, int flag) throws IOException {
        File[] filelist = in.getValue().listFiles();
        if (filelist != null) {
            //System.out.println(in.getValue().getName());
            if (flag == 0) {
                in.getChildren().remove(0, in.getChildren().size());
            }

            if (filelist.length > 0) {
                for (int i = 1; i < filelist.length; i++) {
                    if (filelist[i].isDirectory() & filelist[i].canRead() & !filelist[i].isHidden()) {
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


}
