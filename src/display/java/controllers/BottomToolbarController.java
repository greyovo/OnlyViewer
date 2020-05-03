package display.java.controllers;

import home.java.controllers.Util;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class BottomToolbarController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Util.controllers.put(this.getClass().getSimpleName(),this);
    }


}
