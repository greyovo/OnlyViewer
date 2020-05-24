package onlyviewer.display.java.controllers;

import com.jfoenix.controls.JFXSnackbar;
import javafx.scene.transform.Scale;
import onlyviewer.display.DisplayWindow;
import onlyviewer.display.java.model.Ocr;
import onlyviewer.home.java.components.CustomDialog;
import onlyviewer.home.java.components.DialogType;
import onlyviewer.home.java.controllers.AbstractController;
import onlyviewer.home.java.controllers.ControllerUtil;
import onlyviewer.home.java.controllers.HomeController;
import onlyviewer.home.java.model.ImageListModel;
import onlyviewer.home.java.model.ImageModel;
import onlyviewer.display.java.model.SwitchPics;
import onlyviewer.home.java.model.SelectedModel;
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

/**
 * 展示窗口的控制器
 *
 * @author Grey
 * @author tudou daren
 * @see onlyviewer.home.java.controllers.AbstractController
 * @since 2020.05
 */
public class DisplayWindowController extends AbstractController implements Initializable {

    @FXML
    @Getter
    public StackPane rootPane;
    public HBox toolbar;
    private Stage stage; //获取当前窗口

    @FXML
    @Setter
    @Getter
    private ImageView imageView;

    @Setter
    @Getter
    private Image image;
    private ImageModel imageModel;
    public ArrayList<ImageModel> imageModelArrayList;

    @Getter
    private JFXSnackbar snackbar; //下方通知条
    private SwitchPics sw;
    private HomeController hc;

    public DisplayWindowController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllerUtil.controllers.put(this.getClass().getSimpleName(), this);
        hc = (HomeController) ControllerUtil.controllers.get(HomeController.class.getSimpleName());

        toolbar.translateYProperty().bind(rootPane.heightProperty().divide(5).multiply(2));
        snackbar = new JFXSnackbar(rootPane);
        stage = DisplayWindow.getStage();

        System.out.println("Display window initialization done...");
    }

    public void initImage(ImageModel im) {

        if (im == null) {
            this.imageModel = null;
            imageView.setImage(null);
            return;
        }

        if (hc.isComboBoxClicked()) {
            imageModelArrayList = ImageListModel.refreshList(im.getImageFile().getParent(), hc.getSortComboBox().getValue());
        } else {
            imageModelArrayList = ImageListModel.refreshList(im.getImageFile().getParent());
        }

        this.imageModel = im;
        this.image = new Image(im.getImageFile().toURI().toString());
        this.imageView.setImage(image);
        this.sw = new SwitchPics(imageModelArrayList);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        //以下适应图片比例，避免宽度过大显示不全
        //获取当前窗口高度比例
        double winWidth, winHeight;
        if (stage != null) {
            winWidth = stage.getScene().getWidth();
            winHeight = stage.getScene().getHeight();
        } else {
            winWidth = DisplayWindow.windowWidth;
            winHeight = DisplayWindow.windowHeight;
        }
        double sysRatio = winWidth / winHeight;                 //窗口长宽比
        double ratio = image.getWidth() / image.getHeight();    //图片长宽比

        //清空上次设置后遗留下的数据
        imageView.fitWidthProperty().unbind();
        imageView.fitHeightProperty().unbind();
        imageView.setFitHeight(0);
        imageView.setFitWidth(0);

        //若图片长或宽比窗口大，缩小至窗口大小并随窗口绑定长宽，否则以原尺寸显示
        if (image.getWidth() > winWidth || image.getHeight() > winHeight) {
            if (ratio > sysRatio) {
                imageView.fitWidthProperty().bind(rootPane.widthProperty());
            } else {
                imageView.fitHeightProperty().bind(rootPane.heightProperty());
            }
        } else {
            imageView.fitWidthProperty().setValue(image.getWidth());
        }

        setImageMouseAction();
    }

    private void setImageMouseAction() {
        //以下实现滚轮的放大缩小
        imageView.getScene().setOnScroll(new EventHandler<ScrollEvent>() {
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

        //记录鼠标点击的每次位置
        final double[] lastPosition = new double[2];
        imageView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                lastPosition[0] = event.getX();
                lastPosition[1] = event.getY();
            }
        });

        //以下实现鼠标拖拽移动
        imageView.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Translate tran = new Translate(event.getX() - lastPosition[0], event.getY() - lastPosition[1]);
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
    }

    @FXML
    private void zoomOut() {
        imageView.setScaleX(imageView.getScaleX() * 0.75);
        imageView.setScaleY(imageView.getScaleY() * 0.75);
    }

    //上一张图
    @FXML
    private void showPreviousImg() throws IOException {
        initStatus();

        //为了防止删除后显示空白，自动刷新
        if (imageModel != null)
            imageModelArrayList = ImageListModel.refreshList(imageModel.getImageFile().getParent());

        if (imageModelArrayList == null || imageModelArrayList.size() == 0) {
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("此文件夹图片已空"));
            initImage(null);
            stage.setTitle("无图片");
        } else {
            initImage(sw.lastImage(imageModel));
            stage.setTitle(imageModel.getImageName()); //更新图片名字
        }
    }

    //下一张图
    @FXML
    public void showNextImg() throws IOException {
        initStatus();

        //为了防止删除后显示空白，自动刷新
        if (imageModel != null)
            imageModelArrayList = ImageListModel.refreshList(imageModel.getImageFile().getParent());

        if (imageModelArrayList == null || imageModelArrayList.size() == 0) {
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("此文件夹图片已空"));
            this.imageModel = null;
            imageView.setImage(null);
            stage.setTitle("无图片");
        } else {
            initImage(sw.nextImage(imageModel));
            stage.setTitle(imageModel.getImageName()); //更新图片名字
        }
    }

    @FXML
    //幻灯片放映
    private void playSlide() {
        initStatus();   //比例重新设定
        toolbar.setVisible(false);  //使工具栏不可见
        stage.setFullScreen(true);  //设置窗口为全屏
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent("开始幻灯片放映，点击任意键结束"));

        //以下实现定时器功能翻页
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //定时任务中安排切换下一页功能
                Platform.runLater(() -> {
                    try {
                        showNextImg();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        };
        Timer timer = new Timer();
        long delay = 5000;  // 定义开始等待时间
        long intervalPeriod = 5000;  //每次执行的间隔
        timer.scheduleAtFixedRate(task, delay, intervalPeriod); // 定时器执行

        //当鼠标点击时，暂停计时器，恢复工具栏
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stopSlide(timer, stage);
            }
        });

        //键盘输入任意键退出
        imageView.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                stopSlide(timer, stage);
            }
        });
    }

    //停止幻灯片播放
    private void stopSlide(Timer timer, Stage stage) {
        timer.cancel();
        toolbar.setVisible(true);
        stage.setFullScreen(false);
        stage.sizeToScene();
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent("幻灯片放映结束"));

        //清空事件
        imageView.getScene().setOnKeyPressed(event -> {
        });
        imageView.setOnMouseClicked(event -> {
        });
    }

    @FXML
    private void ocr() {
        CustomDialog loading = new CustomDialog(this, DialogType.INFO, imageModel, "正在处理");
        loading.setLoadingSpinner();
        loading.show();

        Task ocrTask = new Task() {
            @Override
            protected Object call() throws Exception {
                //执行OCR识别，在成功后关闭加载窗口
                String path = imageModel.getImageFilePath();
                File file = new File(path);
                if (!file.exists()) {
                    System.out.println("图片不存在!");
                }
                String result = Ocr.doOcr(path, Ocr.ENG);
                loading.close();
                updateMessage(result);   //向监听器更新结果
                return true;
            }
        };
        //设置加载任务的监听，在接收到信息回传时展示结果
        ocrTask.messageProperty().addListener((observable, oldValue, newValue) -> {
            CustomDialog dialog = new CustomDialog(this, DialogType.INFO, imageModel, "识别结果");
            dialog.setBodyLabel(newValue);
            dialog.show();
        });
        new Thread(ocrTask).start(); //启动新的线程进行识别任务
    }

    @FXML
    public void showInfo() {
        if (imageModel == null) {
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("无属性展示"));
            return;
        }
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
        if (imageModel == null) {
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("无文件可删除"));
            return;
        }
        SelectedModel.setSourcePath(imageModel);
        new CustomDialog(this, DialogType.DELETE, imageModel,
                "删除图片",
                "删除文件: " + imageModel.getImageName() + "\n\n你可以在回收站处找回。").show();
    }

    @FXML
    private void fullScreen() {
        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
            stage.sizeToScene();
        } else {
            stage.setFullScreen(true);
        }
    }

    @FXML
    private void compress() {
        SelectedModel.setSourcePath(imageModel.getImageFilePath());
        int success = SelectedModel.compressImage(800);
        if (success != 0) {
            initImage(imageModel);
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("已压缩" + success + "张图片并创建副本"));
            try {
                // 刷新缩略图列表
                hc.placeImages(ImageListModel.initImgList(imageModel.getImageParentPath()),
                        imageModel.getImageParentPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("没有图片执行压缩。压缩条件:大于800KB"));
        }
    }

}