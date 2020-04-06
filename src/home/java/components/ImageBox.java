package home.java.components;

import com.jfoenix.controls.JFXRippler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * 用来存放图片和图片文件名的盒子。
 * 继承{@link VBox}，添加特定的样式。
 *
 * @author Grey
 */
public class ImageBox extends VBox {

    {
        setMaxSize(170,170);
        setAlignment(Pos.BOTTOM_CENTER);
    }

    public ImageBox(JFXRippler riv, Label imageLabel) {
        super();
        this.getChildren().add(riv);
        this.getChildren().add(imageLabel);
    }

    public ImageBox(ImageView iv, Label imageLabel) {
        super();
        this.getChildren().add(iv);
        this.getChildren().add(imageLabel);
    }

}
