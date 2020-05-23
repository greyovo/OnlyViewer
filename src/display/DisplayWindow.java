package display;

import com.jfoenix.controls.JFXDecorator;
import display.java.controllers.DisplayWindowController;
import home.java.model.ImageModel;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;

/**
 * 图片单独展示窗口
 *
 * @author Grey
 */
public class DisplayWindow extends Application {

    public static double windowWidth = 800;     //窗口宽度
    public static double windowHeight = 600;    //窗口高度
    @Getter
    private static Stage stage;


    private ImageModel im;
    DisplayWindowController dwController;

    public DisplayWindow() {

    }

    @Override
    public void init() throws Exception {
        super.init();
        //根据屏幕大小自适应设置长宽
        try {
            Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
            windowWidth = bounds.getWidth() / 1.5;
            windowHeight = bounds.getHeight() / 1.5;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        DisplayWindow.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/display/resources/fxml/DisplayWindow.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = null;
        if (root != null) {
            scene = new Scene(new JFXDecorator(stage, root), windowWidth, windowHeight);
            //加载css样式文件
            ObservableList<String> stylesheets = scene.getStylesheets();
            stylesheets.addAll(this.getClass().getResource("/display/resources/css/display.css").toExternalForm());
        }

        //窗口属性设置
        stage.setTitle(im.getImageName());
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/home/resources/icons/app.png")));
        stage.setScene(scene);

        dwController = fxmlLoader.getController();  //通过FXMLLoader获取展示窗口的controller实例
        dwController.initImage(im);

        stage.show();
    }

    public void setImage(ImageModel im) {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.im = im;
    }
}
