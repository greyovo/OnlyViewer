package display.java.controllers;

import com.jfoenix.controls.JFXToolbar;
import display.DisplayWindow;
import home.java.controllers.Util;
import home.java.model.ImageListModel;
import home.java.model.ImageModel;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import javax.annotation.PostConstruct;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

//@ViewController(value = "/fxml/displayWindow.fxml", title = "Display Window")
public class DisplayWindowController implements Initializable {

    @FXML @Getter
    public StackPane stackPane;

    public JFXToolbar toolbar;

    @FXML @Setter @Getter
    private ImageView imageView;

    private Image image;

    private ImageModel imageModel;

    public ArrayList<ImageModel> imageModelArrayList;

    public DisplayWindowController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Util.controllers.put(this.getClass().getSimpleName(),this);

    }

    public void test(){
        System.out.println("imageView in controller: "+imageView);
    }

    public void setImageModel(){

    }


}
