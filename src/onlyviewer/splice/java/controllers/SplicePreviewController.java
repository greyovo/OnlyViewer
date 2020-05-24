package onlyviewer.splice.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import onlyviewer.home.java.controllers.ControllerUtil;
import onlyviewer.home.java.controllers.HomeController;
import onlyviewer.home.java.model.ImageModel;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @ProjName: OnlyViewer
 * @ClassName: SplicePreviewController
 * @Author: tudou daren
 * @Author: grey
 * @Describe: 拼接窗口Controller
 * @since 2020.05
 */

public class SplicePreviewController implements Initializable {
    @FXML
    private StackPane rootPane;
    @FXML
    private JFXButton saveButton;
    @FXML
    private ImageView imageView;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;

    private ImageModel imageModel;

    private ArrayList<ImageModel> imageModelList;
    private HomeController hc;
    private JFXSnackbar snackbar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        scrollPane.setContent(new ImageView(new Image("file:/D:/result-2018-08-18-22-01-03.png")));
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(0);

        ControllerUtil.controllers.put(this.getClass().getSimpleName(), this);
        hc = (HomeController) ControllerUtil.controllers.get(HomeController.class.getSimpleName());

        //保存按钮设置自适应位置
        saveButton.translateYProperty().bind(rootPane.heightProperty().divide(15).multiply(5));
        saveButton.translateXProperty().bind(rootPane.widthProperty().divide(15).multiply(6));
        snackbar = new JFXSnackbar(hc.getRootPane());
    }

    //先调用这个设置好图片，否则会导致空指针
    public void setImageModelList(ArrayList<ImageModel> set) {
        this.imageModelList = set;
        scrollPane.setContent(vBox);
        int number = 0;
        //将图片加入到垂直盒子中
        for (ImageModel im : imageModelList) {
            Image image = new Image(im.getImageFile().toURI().toString());
            ImageView imageView = new ImageView(image);
            if (number == 0) {
                this.imageModel = im;
                this.imageView = imageView;
                number++;
            }
            imageView.setSmooth(true);
            imageView.setFitWidth(800);     //此处指定了拼接出的图片的宽度，注释掉此句则保持原图尺寸
            imageView.setPreserveRatio(true);
            imageView.setStyle("-fx-margin:0;-fx-padding:0;");
            vBox.getChildren().add(imageView);
        }

    }


    //截图 = 保存
    @FXML
    private void snap() {
        WritableImage wa = imageView.getParent().snapshot(null, null);
        //设置图片名字包含当前系统时间
        Date date = new Date();
        Stage stage = (Stage) imageView.getScene().getWindow();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String prefix = imageModelList.get(0).getImageNameNoExt();

        try {
            BufferedImage buff = SwingFXUtils.fromFXImage(wa, null);
            System.out.println("buff = " + buff);
            ImageIO.write(buff, "png",
                    //保存到当前文件夹
                    new File(imageModel.getImageParentPath() + "\\" + prefix + "_more_" + dateFormat.format(date) + ".png"));
            hc.refreshImagesList(hc.getSortComboBox().getValue());
            stage.close(); //为了处理卡顿关闭该窗口
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("拼接完成，已创建副本")); //信息条提示
        } catch (IOException e) {
            stage.close();
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("拼接失败，可能是图片过长"));
            e.printStackTrace();
        }
    }

}
