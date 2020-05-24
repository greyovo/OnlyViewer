package onlyviewer.home.java.controllers;

import com.jfoenix.controls.JFXSnackbar;
import javafx.scene.layout.StackPane;
import lombok.Getter;

public class AbstractController {

    @Getter
    private StackPane rootPane;

    @Getter
    private JFXSnackbar snackbar;

}
