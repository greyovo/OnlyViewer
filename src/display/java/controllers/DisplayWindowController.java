package display.java.controllers;

import display.DisplayWindow;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import org.junit.Test;

import javax.annotation.PostConstruct;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

//@ViewController(value = "/fxml/displayWindow.fxml", title = "Display Window")
public class DisplayWindowController implements Initializable {

    @FXML
    private ImageView imageView;

    @FXML
    private Image image;


    public DisplayWindowController() {
    }

    //    @PostConstruct
    public void init() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
