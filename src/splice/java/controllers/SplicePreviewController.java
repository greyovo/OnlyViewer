package splice.java.controllers;

import com.jfoenix.controls.JFXButton;
import home.java.controllers.ControllerUtil;
import home.java.model.ImageModel;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

        //将图片加入到垂直盒子中
        for (ImageModel im : imageModelSet) {
            Image image = new Image(im.getImageFile().toURI().toString());
            ImageView imageView = new ImageView(image);
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
    // FIXME: 2020/5/12 应当保存到图片所在的目录
    private void snap() {
        WritableImage wa = imageView.getParent().snapshot(null, null);
        try {
            BufferedImage buff = SwingFXUtils.fromFXImage(wa, null);
            ImageIO.write(buff, "png", new File("test" + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
