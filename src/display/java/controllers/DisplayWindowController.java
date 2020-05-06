package display.java.controllers;

import com.jfoenix.controls.JFXSnackbar;
import display.DisplayWindow;
import home.java.components.CustomDialog;
import home.java.components.DialogType;
import home.java.controllers.AbstractController;
import home.java.controllers.ControllerUtil;
import home.java.model.ImageListModel;
import home.java.model.ImageModel;
import display.java.model.SwitchPics;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

//@ViewController(value = "/fxml/displayWindow.fxml", title = "Display Window")
public class DisplayWindowController extends AbstractController implements Initializable {

    @FXML
    @Getter
    public StackPane rootPane;

    public HBox toolbar;

    @FXML
    @Setter
    @Getter
    private ImageView imageView;

    private Image image;

    private ImageModel imageModel;

    public ArrayList<ImageModel> imageModelArrayList;

    @Getter
    private JFXSnackbar snackbar; //下方通知条

    private SwitchPics sw;

    public DisplayWindowController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllerUtil.controllers.put(this.getClass().getSimpleName(), this);
        toolbar.translateYProperty().bind(rootPane.heightProperty().divide(5).multiply(2));
        snackbar = new JFXSnackbar(rootPane);
    }

    public void initImage(ImageModel im) {
        imageModelArrayList = ImageListModel.refreshList(im.getImageFile().getParent());
        this.imageModel = im;
        this.image = new Image(im.getImageFile().toURI().toString());
        this.imageView.setImage(image);
        this.sw = new SwitchPics(imageModelArrayList);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        //适应图片比例，避免宽度过大显示不全
        double ratio = image.getWidth() / image.getHeight();
        double sysRatio = DisplayWindow.windowWidth / DisplayWindow.windowHeight;
        if (ratio > sysRatio) {
            imageView.fitWidthProperty().bind(rootPane.widthProperty());
        } else {
            imageView.fitHeightProperty().bind(rootPane.heightProperty());
        }

        //以下实现滚轮的放大缩小
        imageView.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                //如果滚轮向下滑动，缩小
                if (event.getDeltaY() < 0) {
                    Scale scale = new Scale(0.9, 0.9, event.getX(), event.getY());
                    imageView.getTransforms().add(scale);
                }
                //如果滚轮向上滑动，放大
                if (event.getDeltaY() > 0) {
                    Scale scale = new Scale(1.1, 1.1, event.getX(), event.getY());
                    imageView.getTransforms().add(scale);
                }
            }
        });

        //记录鼠标点击的初始位置
        final double[] k = new double[2];
        imageView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                k[0] = event.getX();
                k[1] = event.getY();
            }
        });

        //以下实现鼠标拖拽移动
        imageView.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Translate tran = new Translate(event.getX() - k[0], event.getY() - k[1]);
                imageView.getTransforms().add(tran);
            }
        });
        //imageModelArrayList = ImageListModel.refreshList(im.getImageFile().getParent());
        System.out.println("cur list:\n" + imageModelArrayList);
    }

    /**
     * 恢复初始缩放比例和位置
     */
    private void initStatus() {
        imageView.setScaleX(1.0);
        imageView.setScaleY(1.0);
        imageView.getTransforms().clear();
    }

    //TODO 放大缩小
    @FXML
    private void zoomIn() {
        imageView.setScaleX(imageView.getScaleX() * 1.25);
        imageView.setScaleY(imageView.getScaleY() * 1.25);
        System.out.println("放大" + imageView.getScaleX() + " x " + imageView.getScaleY());
    }

    @FXML
    private void zoomOut() {
        imageView.setScaleX(imageView.getScaleX() * 0.75);
        imageView.setScaleY(imageView.getScaleY() * 0.75);
        System.out.println("缩小" + imageView.getScaleX() + " x " + imageView.getScaleY());
    }

    //下一张图
    @FXML
    private void showNextImg() throws IOException {
        initStatus();
        System.out.println("下一张");
        //为了防止删除后显示空白，自动刷新
        imageModelArrayList = ImageListModel.refreshList(imageModel.getImageFile().getParent());
        if (imageModelArrayList.size() == 0) {
            System.out.println("此文件夹中的照片已空！");
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("此文件夹中的照片已空！"));
        } else {
            if(sw.nextImage(imageModel)!=null){
                initImage(sw.nextImage(imageModel));
            }
            else{
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent("已到达最后一张"));
            }
        }
    }

    //上一张图
    @FXML
    private void showPreviousImg() throws IOException {
        initStatus();
        System.out.println("上一张");
        //为了防止删除后显示空白，自动刷新
        imageModelArrayList = ImageListModel.refreshList(imageModel.getImageFile().getParent());
        if (imageModelArrayList.size() == 0) {
            System.out.println("此文件夹中的照片已空！");
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("此文件夹中的照片已空！"));
        } else {
            if(sw.lastImage(imageModel)!=null){
                initImage(sw.lastImage(imageModel));
            }
            else{
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent("已到达第一张"));
            }
        }

    }

    //TODO OCR
    @FXML
    private void ocr() {
        System.out.println("OCR");
    }

    //删除
    @FXML
    private void delete() {
        System.out.println("删除");
        new CustomDialog(this, DialogType.DELETE, imageModel,
                "删除图片",
                "删除文件: " + imageModel.getImageName() + "\n\n你可以在回收站处找回。").show();
    }

}

