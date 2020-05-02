package home.java.components;

import com.jfoenix.controls.JFXPopup;
import com.jfoenix.effects.JFXDepthManager;
import display.DisplayWindow;
import home.java.controllers.PopupMenuController;
import home.java.model.ImageModel;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;


/**
 * 主窗口缩略图单元。
 * 用来包装图片{@link ImageView2}和图片文件名{@link ImageLabel}。
 * 继承自{@link VBox}，添加特定的样式。
 *
 * @author Grey
 */
@Getter @Setter
public class ImageBox extends VBox {

    {
        setMaxSize(170, 170);
        setAlignment(Pos.BOTTOM_CENTER);
    }

    private ImageModel im;
    private ImageView2 imageView2;
    private MouseImageMenu menu = new MouseImageMenu(im);
    private JFXPopup popUpMenu;

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
        try {
            setPopUpMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPopUpMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PopupMenu.fxml"));
        loader.setController(new PopupMenuController(this));
        popUpMenu = new JFXPopup(loader.load());
        popUpMenu.setAutoHide(true);
    }


    private void setMouseAction() {

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
//                menu.show(this, event.getScreenX(),event.getScreenY());
                popUpMenu.show(this,
                        JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT,
                        100,100);
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
