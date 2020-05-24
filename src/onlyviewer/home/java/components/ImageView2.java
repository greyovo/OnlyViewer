package onlyviewer.home.java.components;

import com.jfoenix.effects.JFXDepthManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 继承自JavaFX的ImageView。
 * 默认使用JFXDepthManager添加投影。
 *
 * @author Grey
 * @since 2020.04
 * */
public class ImageView2 extends ImageView {

    public ImageView2(Image image) {
        super(image);
        setImageDepth();
        setPreserveRatio(true);
        setFitHeight(150);
        setFitWidth(150);
    }

    private void setImageDepth() {
        //默认投影样式
        JFXDepthManager.setDepth(this, 1);
    }
}
