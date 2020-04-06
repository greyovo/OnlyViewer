package home.java.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;


import static javafx.geometry.Pos.CENTER;

public class ImageLabel extends Label {

    {
        setAlignment(CENTER);
        setFont(Font.font(16));
//        super.setWrapText(true);
        setStyle("-fx-padding:15 0 0 0;");
    }

    public ImageLabel(String text) {
        super(text);
    }

}
