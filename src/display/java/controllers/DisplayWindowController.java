package display.java.controllers;

import com.jfoenix.controls.JFXToolbar;
import home.java.controllers.Util;
import home.java.model.ImageListModel;
import home.java.model.ImageModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    public JFXToolbar toolbar;

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

    }

    public void initImage(ImageModel im) {
        this.imageModel = im;
        this.image = new Image(im.getImageFile().toURI().toString());
        this.imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        //适应图片比例，避免宽度过大显示不全
        double ratio = image.getWidth() / image.getHeight();
        if (ratio > 1) {
            imageView.fitHeightProperty().bind(stackPane.heightProperty());
        } else {
            imageView.fitWidthProperty().bind(stackPane.widthProperty());
        }

        imageModelArrayList = ImageListModel.refreshList(im.getImageFile().getParent());
        System.out.println("cur list:\n"+imageModelArrayList);
    }



}
