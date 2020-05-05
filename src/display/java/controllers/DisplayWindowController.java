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

    public ArrayList<ImageModel> ilist;
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

    public void initImage(ImageModel im, ArrayList<ImageModel> ilist) {
        this.imageModel = im;
        this.ilist = ilist;
        this.image = new Image(im.getImageFile().toURI().toString());
        this.imageView.setImage(image);
        this.sw=new SwitchPics(this.ilist);
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


        imageModelArrayList = ImageListModel.refreshList(im.getImageFile().getParent());
        System.out.println("cur list:\n" + imageModelArrayList);
    }

    //TODO 放大缩小
    @FXML
    private void zoomIn() {
        imageView.setScaleX(imageView.getScaleX() * 1.25);
        imageView.setScaleY(imageView.getScaleY() * 1.25);
        System.out.println("放大");
    }

    @FXML
    private void zoomOut() {
        imageView.setScaleX(imageView.getScaleX() * 0.75);
        imageView.setScaleY(imageView.getScaleY() * 0.75);
        System.out.println("缩小");
    }

    // FIXME: 2020.5.5
     //放大缩小后，下一张图不会复原
    //TODO 下一张图
    @FXML
    private void showNextImg() throws IOException {
        System.out.println("下一张");
        //SwitchPics sw = new SwitchPics(this.ilist);
        initImage(sw.nextImage(imageModel),ilist);
        //initImage(nextImage(imageModel),ilist);
    }

    //TODO 上一张图
    @FXML
    private void showPreviousImg() throws IOException {

        System.out.println("上一张");
        //SwitchPics sw = new SwitchPics(this.ilist);
        initImage(sw.lastImage(imageModel),ilist);
        //initImage(lastImage(imageModel),ilist);
    }

    //TODO OCR
    @FXML
    private void ocr() {
        System.out.println("OCR");
    }

    //TODO 删除
    @FXML
    private void delete() {
        System.out.println("删除");
        new CustomDialog(this, DialogType.DELETE, imageModel,
                "删除图片",
                "删除文件: " + imageModel.getImageName() + "\n\n你可以在回收站处找回。").show();
    }

//    //返回下一张照片
//    private ImageModel nextImage(ImageModel im) throws IOException {
//        int i = 0;
//        for (i = 0; i < ilist.size(); i++) {
//            if (ilist.get(i).getImageName().equals(im.getImageName())) {
//                if (i == ilist.size() - 1) System.out.println("已到达最后一张");
//                break;
//            }
//        }
//        return ilist.get((i + 1) % (ilist.size()));
//
//    }
//
//    //返回上一张照片
//    private ImageModel lastImage(ImageModel im) throws IOException {
//        int i = 0;
//        for (i = 0; i < ilist.size(); i++) {
//            if (ilist.get(i).getImageName().equals(im.getImageName())) {
//                if (i == 0) {
//                    System.out.println("已到达第一张照片");
//                    i=ilist.size();
//                }
//                break;
//            }
//        }
//        return ilist.get((i - 1) % (ilist.size()));
//
//    }



}

