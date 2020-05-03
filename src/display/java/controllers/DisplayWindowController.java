package display.java.controllers;

import com.jfoenix.controls.JFXToolbar;
import display.DisplayWindow;
import home.java.controllers.Util;
import home.java.model.ImageListModel;
import home.java.model.ImageModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

//@ViewController(value = "/fxml/displayWindow.fxml", title = "Display Window")
public class DisplayWindowController implements Initializable {

    @FXML
    @Getter
    public StackPane stackPane;

    public HBox toolbar;

    @FXML
    @Setter
    @Getter
    private ImageView imageView;

    private Image image;

    private ImageModel imageModel;

    public ArrayList<ImageModel> imageModelArrayList;

    public DisplayWindowController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Util.controllers.put(this.getClass().getSimpleName(), this);
        toolbar.translateYProperty().bind(stackPane.heightProperty().divide(5).multiply(2));
    }

    public void initImage(ImageModel im) {
        this.imageModel = im;
        this.image = new Image(im.getImageFile().toURI().toString());
        this.imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        //适应图片比例，避免宽度过大显示不全
        double ratio = image.getWidth() / image.getHeight();
        double sysRatio = DisplayWindow.windowWidth / DisplayWindow.windowHeight;
        if (ratio > sysRatio) {
            imageView.fitWidthProperty().bind(stackPane.widthProperty());
        } else {
            imageView.fitHeightProperty().bind(stackPane.heightProperty());
        }

        imageModelArrayList = ImageListModel.refreshList(im.getImageFile().getParent());
        System.out.println("cur list:\n"+imageModelArrayList);
    }

    //TODO 放大缩小
    @FXML
    private void zoomIn(){
        System.out.println("放大");
    }

    @FXML
    private void zoomOut(){
        System.out.println("缩小");
    }

    //TODO 下一张图
    @FXML
    private void showNextImg(){
        System.out.println("下一张");
    }

    //TODO 上一张图
    @FXML
    private void showPreviousImg(){
        System.out.println("上一张");
    }

    //TODO OCR
    @FXML
    private void ocr(){
        System.out.println("OCR");
    }

    //TODO 删除
    @FXML
    private void delete(){
        System.out.println("删除");
    }
}
