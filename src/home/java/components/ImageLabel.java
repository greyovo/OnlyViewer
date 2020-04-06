package home.java.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;


import static javafx.geometry.Pos.CENTER;

public class ImageLabel extends Label {

    public ImageLabel(String text) {
        super(text);
        super.setAlignment(CENTER);
        super.setFont(Font.font(16));
//        super.setWrapText(true);
        super.setStyle("-fx-padding:10 0 0 0;");
    }

}
