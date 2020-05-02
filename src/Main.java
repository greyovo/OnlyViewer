import com.jfoenix.controls.JFXDecorator;

import home.java.controllers.HomeController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application{

    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("OnlyViewer");

        //根据屏幕大小自适应设置长宽
        double width = 800;
        double height = 600;
        try {
            Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
            width = bounds.getWidth() / 1.45;
            height = bounds.getHeight() / 1.45;
        } catch (Exception e){
            e.printStackTrace();
        }

        Parent root = FXMLLoader.load(getClass().getResource("fxml/home2.fxml"));
        Scene scene = new Scene(new JFXDecorator(primaryStage, root), width, height);

        //加载css样式文件
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(Main.class.getResource("home/resources/css/home.css").toExternalForm());

//        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/icons/app.png")));
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/home/resources/icons/app.png")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
