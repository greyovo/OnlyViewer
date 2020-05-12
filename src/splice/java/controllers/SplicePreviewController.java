package splice.java.controllers;

import com.jfoenix.controls.JFXButton;
import home.java.controllers.ControllerUtil;
import home.java.model.ImageModel;
import home.java.model.SelectedModel;
import javafx.application.Platform;
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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;

// TODO: 2020/5/12 拼接功能 
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

//    @FXML
//    private Image image1;
//    @FXML
//    private Image image2;
//    @FXML
//    private Image image3;
//    @FXML
//    private Image image4;
//    @FXML
//    private Image image5;

    private Set<ImageModel> imageModelSet;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        scrollPane.setContent(new ImageView(new Image("file:/D:/result-2018-08-18-22-01-03.png")));
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        vBox.setAlignment(Pos.CENTER);
        ControllerUtil.controllers.put(this.getClass().getSimpleName(), this);

        //保存按钮设置自适应位置
        saveButton.translateYProperty().bind(rootPane.heightProperty().divide(15).multiply(5));
        saveButton.translateXProperty().bind(rootPane.widthProperty().divide(15).multiply(6));
    }

    //先调用这个设置好图片，否则会导致空指针
    public void setImageModelSet(Set<ImageModel> set) {
        this.imageModelSet = set;
        scrollPane.setContent(vBox);
        int number = 0;
        //将图片加入到垂直盒子中
        for (ImageModel im : imageModelSet) {
            Image image = new Image(im.getImageFile().toURI().toString());
            ImageView imageView = new ImageView(image);
            if (number==0){
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
    // FIXME: 2020/5/12 点完截图之后会卡住
    private void snap() {
        WritableImage wa = imageView.getParent().snapshot(null, null);
        //设置图片名字为当前系统时间
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy_MM_dd_HH_mm");
            try {
                BufferedImage buff = SwingFXUtils.fromFXImage(wa, null);
                ImageIO.write(buff, "png",
                        //保存到当前文件夹
                       new File(imageModel.getImageParentPath()+"\\" +"拼接"+ dateFormat.format(date) + ".png"));
                //为了处理卡顿关闭该窗口
                Stage stage =(Stage) imageView.getScene().getWindow();
                stage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


    }

}
