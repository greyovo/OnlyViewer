package home.java.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * 用来存放图片和图片文件名的盒子。
 * 继承{@link VBox}，添加特定的样式。
 *
 * @author Grey
 */
public class ImageBox extends VBox {

    {
        super.setPrefHeight(150);
        super.setPrefWidth(150);
        super.setAlignment(Pos.BOTTOM_CENTER);
        super.paddingProperty().setValue(new Insets(15.0));
    }

    public ImageBox(RipplerImageView riv, ImageLabel imageLabel) {
        super.getChildren().add(riv);
        super.getChildren().add(imageLabel);
    }

}
