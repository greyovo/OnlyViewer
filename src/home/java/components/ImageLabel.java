package home.java.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;


import static javafx.geometry.Pos.CENTER;

public class ImageLabel extends Label {

    //初始化样式
    {
        super.setAlignment(CENTER);
        super.setFont(Font.font(18));
//        super.setWrapText(true);
        super.setStyle("-fx-padding:10 0 0 0;");
    }

    public ImageLabel() {
    }

    public ImageLabel(String text) {
        super(text);
    }

    public ImageLabel(String text, Node graphic) {
        super(text, graphic);
    }
}
