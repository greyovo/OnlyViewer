package home.java.components;

import com.jfoenix.controls.JFXRippler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * 继承自{@link JFXRippler}，设置特定样式
 *
 * @see com.jfoenix.controls.JFXRippler
 * @author Grey
 * @since 2020.04
 * */

public class WhiteRippler extends JFXRippler {

    public WhiteRippler(Node control) {
        super(control);
        super.setRipplerFill(Color.WHITE);
        setAlignment(Pos.BOTTOM_CENTER);
        setPrefHeight(170);
        setPrefWidth(170);
    }

}
