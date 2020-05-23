package home.java.components;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.effects.JFXDepthManager;
import display.DisplayWindow;
import home.java.controllers.PopupMenuController;
import home.java.model.ImageModel;
import home.java.model.SelectionModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;


/**
 * 主窗口的缩略图单元。
 * 用来包装图片{@link ImageView2}和图片文件名{@link ImageLabel}。
 * 继承自{@link VBox}，添加特定的样式。
 *
 * @author Grey
 * @since 2020.04
 */
@Getter
@Setter
public class ImageBox extends VBox {

    private ImageModel im;
    private ImageView2 imageView2;
    private JFXPopup popUpMenu;
    @Getter
    private JFXCheckBox checkBox = new JFXCheckBox();

    /**
     * 构造器，初始化一个缩略图单元
     *
     * @param im 图片文件类
     */
    public ImageBox(ImageModel im) {
        this.im = im;
        ImageView2 imageView = new ImageView2(new Image(im.getImageFile().toURI().toString(),
                100,
                100,
                true,
                true,
                true));
        this.imageView2 = imageView;                                //图片
        WhiteRippler riv = new WhiteRippler(imageView);     //一个水波纹点击效果的包装
        ImageLabel imageLabel = new ImageLabel(im.getImageName());  //标签 - 文件名
        imageLabel.setStyle("-fx-padding:7 0 7 -2;");

        HBox hBox = new HBox(checkBox, imageLabel);
        hBox.setAlignment(Pos.CENTER);
        hBox.setStyle("-fx-padding:5 5 3 5;");

        getChildren().addAll(riv, hBox);
        setMaxSize(170, 170);
        setAlignment(Pos.BOTTOM_CENTER);

        //设置文件信息tips
        String tooltip = String.format("名称: %s\n大小: %s", im.getImageName(), im.getFormatSize());
        Tooltip.install(this, new Tooltip(tooltip));

        JFXDepthManager.setDepth(this, 0);
        initMouseAction();
//        initPopUpMenu();
        initCheckBox();
    }

    /**
     * 设置选中与否的属性绑定
     */
    ImageBox imageBox = this;

    private void initCheckBox() {
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    SelectionModel.add(imageBox);
                } else {
                    SelectionModel.remove(imageBox);
                }
            }
        });
    }

    /**
     * 加载右键菜单
     */
    private void initPopUpMenu() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PopupMenu.fxml"));
        loader.setController(new PopupMenuController(this));
        try {
            popUpMenu = new JFXPopup(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        popUpMenu.setAutoHide(true);
    }

    /**
     * 图片对鼠标操作的反馈
     */
    private void initMouseAction() {
        //鼠标点击事件
        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                // 鼠标左键双击看大图
                DisplayWindow dw = new DisplayWindow();
                dw.setImage(im);
                dw.start(new Stage());
            } else if (event.getButton() == MouseButton.SECONDARY) {
                // 鼠标右键菜单
                initPopUpMenu();
                popUpMenu.show(this,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.LEFT,
                        100, 100);
            }
        });

        //当鼠标指向时
        this.setOnMouseMoved(event -> {
            this.setStyle("-fx-background-color:rgba(0, 0, 0, 0.07);");
        });

        //当鼠标离开时
        this.setOnMouseExited(event -> {
            this.setStyle("-fx-background-color:transparent;");
        });

    }
}
