package home.java.components;

import com.jfoenix.controls.JFXRippler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class RipplerImageView extends JFXRippler {

    {
        setAlignment(Pos.BOTTOM_CENTER);
        setPrefHeight(170);
        setPrefWidth(170);
    }

    public RipplerImageView(Node control) {
        super(control);
        super.setRipplerFill(Color.WHITE);
    }

}
