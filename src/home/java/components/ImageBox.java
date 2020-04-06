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

    public ImageBox(RipplerImageView riv, ImageLabel imageLabel) {
        super();
        setMaxSize(120,120);
        setAlignment(Pos.BOTTOM_CENTER);
        paddingProperty().setValue(new Insets(15.0));
        this.getChildren().add(riv);
        this.getChildren().add(imageLabel);
    }

}
