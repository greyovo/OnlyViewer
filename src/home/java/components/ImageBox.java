package home.java.components;

import com.jfoenix.animation.JFXNodesAnimation;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.effects.JFXDepthManager;
import display.DisplayWindow;
import home.java.model.ImageModel;
import home.java.model.SelectedModel;
import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * 用来存放图片{@link ImageView2}和图片文件名{@link ImageLabel}的盒子。
 * 继承{@link VBox}，添加特定的样式。
 *
 * @author Grey
 */
public class ImageBox extends VBox {

    {
        setMaxSize(170, 170);
        setAlignment(Pos.BOTTOM_CENTER);
    }

    private ImageModel im;
    private ImageView2 imageView2;

    public ImageBox(ImageModel im) {
        this.im = im;
        ImageView2 imageView = new ImageView2(new Image(im.getImageFile().toURI().toString(),
                120,
                120,
                true,
                true,
                true));
        this.imageView2 = imageView;
        RipplerImageView riv = new RipplerImageView(imageView); //一个水波纹点击效果的包装
        ImageLabel imageLabel = new ImageLabel(im.getImageName()); //标签 - 文件名
        this.getChildren().addAll(riv, imageLabel);

        //设置文件信息tips
        String tooltip = String.format("名称: %s\n大小: %s", im.getImageName(), im.getFormatSize());
        Tooltip.install(this, new Tooltip(tooltip));

        setMouseAction();

    }

    private void setMouseAction() {

        MouseImageMenu menu = new MouseImageMenu(im);

        //鼠标点击事件
        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                // TODO 鼠标左键单击图片显示选中框
            } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                // 鼠标左键双击看大图
                DisplayWindow dw = new DisplayWindow();
                try {
                    dw.setImage(new Image(im.getImageFile().toURI().toString()));
                    dw.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                // TODO 鼠标右键菜单
                menu.show(this, event.getScreenX(),event.getScreenY());
            }
        });

        //当鼠标指向时
        EventHandler<? super MouseEvent> mouseEnterImage = (EventHandler<MouseEvent>) event -> {
            JFXDepthManager.setDepth(this, 3);
        };
        this.setOnMouseMoved(mouseEnterImage);

        //当鼠标离开时
        EventHandler<? super MouseEvent> mouseExitImage = (EventHandler<MouseEvent>) event -> {
            JFXDepthManager.setDepth(this, 0);
        };
        this.setOnMouseExited(mouseExitImage);

    }


}
