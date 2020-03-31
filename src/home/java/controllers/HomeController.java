package home.java.controllers;

import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.awt.*;

@ViewController(value = "/fxml/Home.fxml", title = "Material Design Example")
public class HomeController {

    @FXML private Label imageNameLabel;

    @FXML private Image image;

    public HomeController() {

    }


}
