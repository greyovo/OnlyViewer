package display.java.controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSpinner;
import display.DisplayWindow;
import display.java.model.Ocr;
import home.java.components.CustomDialog;
import home.java.components.DialogType;
import home.java.controllers.AbstractController;
import home.java.controllers.ControllerUtil;
import home.java.model.ImageListModel;
import home.java.model.ImageModel;
import display.java.model.SwitchPics;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

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

    @Setter @Getter
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
        setImageMouseAction();
    }

    // TODO: 2020/5/7 设置窗口标题随图片名称变化
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
        System.out.println("cur list:\n" + imageModelArrayList);
    }

    private void setImageMouseAction(){
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
    }

    /**
     * 恢复初始缩放比例和位置
     */
    @FXML
    private void initStatus() {
        imageView.setScaleX(1.0);
        imageView.setScaleY(1.0);
        imageView.getTransforms().clear();
    }

    //-------------以下为工具栏按钮事件---------------
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

    //上一张图
    @FXML
    private void showPreviousImg() throws IOException {
        initStatus();
        System.out.println("上一张");
        //为了防止删除后显示空白，自动刷新
        imageModelArrayList = ImageListModel.refreshList(imageModel.getImageFile().getParent());
        if (imageModelArrayList.size() == 0) {
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("此文件夹照片已空"));
        } else {
            initImage(sw.lastImage(imageModel));
//            imageView.setImage(new Image(imageModel.getImageFile().toURI().toString()));
        }
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
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("此文件夹照片已空"));
        } else {
            initImage(sw.nextImage(imageModel));
//            imageView.setImage(new Image(imageModel.getImageFile().toURI().toString()));
        }
    }

    @FXML
    //幻灯片放映
    private void playSlide(){
        //使工具栏不可见
        toolbar.setVisible(false);
        //以下设置窗口为全屏
        Stage stage = (Stage) imageView.getScene().getWindow();
        stage.setFullScreen(true);
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent("开始幻灯片放映"));

        //以下实现定时器功能
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //定时任务中安排切换下一页功能
                Platform.runLater(()->{
                    try {
                        showNextImg();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        };
        Timer timer = new Timer();
        // 定义开始等待时间
        long delay = 3000;
        //每次执行的间隔
        long intervalPeriod = 3000;
        // 定时器执行
        timer.scheduleAtFixedRate(task, delay, intervalPeriod);

        //当鼠标点击时，暂停计时器，恢复工具栏
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                timer.cancel();
                toolbar.setVisible(true);
            }
        });

        //键盘输入任意键退出
        imageView.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                timer.cancel();
                toolbar.setVisible(true);
                stage.setFullScreen(false);
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent("幻灯片放映结束"));
            }
        });
    }

    @FXML
    private void ocr() {
        System.out.println("OCR");
        CustomDialog loading = new CustomDialog(this, DialogType.INFO, imageModel, "正在处理");
        loading.setLoadingSpinner();
        loading.show();

        DisplayWindowController dwc = (DisplayWindowController)ControllerUtil.controllers.get(this.getClass().getSimpleName());

        Task ocrTask = new Task() {
            @Override
            protected Object call() throws Exception {
                String path = imageModel.getImageFilePath();
                File file = new File(path);
                if (!file.exists()) {
                    System.out.println("图片不存在!");
                }
                String result = Ocr.doOcr(path, Ocr.ENG);
                System.out.println(result);
                loading.close();
                updateMessage(result);
                return true;
            }
        };
        ocrTask.messageProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            CustomDialog dialog = new CustomDialog(dwc, DialogType.INFO, imageModel, "识别结果");
            dialog.setBodyLabel(newValue);
            dialog.show();
        });
        new Thread(ocrTask).start();
    }

    @FXML
    public void showInfo() {
        Image image = new Image(imageModel.getImageFile().toURI().toString());
        StringBuilder info = new StringBuilder();
        info.append("尺寸：").append(image.getWidth()).append(" × ").append(image.getHeight()).append("\n");
        info.append("类型：").append(imageModel.getImageType().toUpperCase()).append("\n");
        info.append("大小：").append(imageModel.getFormatSize()).append("\n");
        info.append("日期：").append(imageModel.getFormatTime()).append("\n");
        info.append("\n位置：").append(imageModel.getImageFilePath());
        new CustomDialog(this, DialogType.INFO, imageModel,
                imageModel.getImageName(), info.toString()).show();
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

