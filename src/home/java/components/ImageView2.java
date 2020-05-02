package home.java.components;

import com.jfoenix.effects.JFXDepthManager;
import com.sun.javafx.scene.layout.region.Margins;
import display.DisplayWindow;
import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 继承自JavaFX的ImageView。
 * 默认使用JFXDepthManager添加投影。并添加鼠标事件：在鼠标滑入时加深投影，移出时恢复。
 *
 * @author Grey
 * */
public class ImageView2 extends ImageView {

    {
        setImageDepth();
        preserveRatioProperty().setValue(true);
        setFitHeight(150);
        setFitWidth(150);
    }

    public ImageView2(Image image) {
        super(image);
    }

    private void setImageDepth() {
        //默认投影样式
        JFXDepthManager.setDepth(this, 1);
    }
}
