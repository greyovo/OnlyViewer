package home.java.components;

import com.jfoenix.controls.JFXRippler;
import home.java.model.ImageModel;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.tools.Tool;

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


    public ImageBox(ImageModel im){
        ImageView2 imageView = new ImageView2(new Image(im.getImageFile().toURI().toString(),
                120,
                120,
                true,
                true,
                true));
        RipplerImageView riv = new RipplerImageView(imageView); //一个水波纹点击效果的包装
        ImageLabel imageLabel = new ImageLabel(im.getImageName()); //标签 - 文件名
        this.getChildren().addAll(riv,imageLabel);

        String tooltip = String.format("名称: %s\n大小: %s",im.getImageName(),im.getFormatSize());
        Tooltip.install(this, new Tooltip(tooltip));

    }


}
