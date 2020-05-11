package splice;

import com.jfoenix.controls.JFXDecorator;
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
import splice.java.controllers.SplicePreviewController;

import java.util.ArrayList;
import java.util.Set;

public class SplicePreviewWindow extends Application {

    public static double windowWidth;
    public static double windowHeight;
    private SplicePreviewController sp;
    private Set<ImageModel> imageModelSet;

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
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/splice/resources/fxml/SplicePreview.fxml"));

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(new JFXDecorator(stage, root), windowWidth, windowHeight);

        sp = fxmlLoader.getController();  //通过FXMLLoader获取展示窗口的controller实例
        sp.setImageModelSet(imageModelSet);

        //加载css样式文件
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(this.getClass().getResource("/splice/resources/css/splice.css").toExternalForm());

        stage.setTitle("图片拼接");
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/home/resources/icons/app.png")));
        stage.setScene(scene);
        stage.show();
    }


    public void initImageSet(Set<ImageModel> imageModelSet){
        this.imageModelSet = imageModelSet;
    }

}
