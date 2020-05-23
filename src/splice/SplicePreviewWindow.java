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

import java.io.IOException;
import java.util.ArrayList;

/**
 * 图片拼接窗口
 *
 * @author Grey
 * @since 2020.05
 */
public class SplicePreviewWindow extends Application {

    public static double windowWidth;
    public static double windowHeight;
    private SplicePreviewController sp;
    private ArrayList<ImageModel> imageModelList;

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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/splice/resources/fxml/SplicePreview.fxml"));

        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(new JFXDecorator(stage, root), windowWidth, windowHeight);

        sp = fxmlLoader.getController();  //通过FXMLLoader获取窗口的controller实例
        sp.setImageModelList(imageModelList);

        //加载css样式文件
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(this.getClass().getResource("/splice/resources/css/splice.css").toExternalForm());

        stage.setTitle("图片拼接预览");
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/home/resources/icons/app.png")));
        stage.setScene(scene);
        stage.show();
    }


    public void initImageList(ArrayList<ImageModel> imageModelList){
        this.imageModelList = imageModelList;
    }

}
