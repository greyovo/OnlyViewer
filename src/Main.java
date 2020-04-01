import com.jfoenix.controls.JFXDecorator;

import home.java.controllers.HomeController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main extends Application{

    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("OnlyViewer");

        /* Copy from JFoenix Demo */
        Flow flow = new Flow(HomeController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", primaryStage);
        flow.createHandler(flowContext).start(container);
        JFXDecorator decorator = new JFXDecorator(primaryStage, container.getView());  //自定义JFX的窗口边框样式
        decorator.setCustomMaximize(true);
        decorator.setGraphic(new ImageView("/home/resources/icons/app.png"));  //设置标题栏左侧小图标

        //根据屏幕大小自适应设置长宽
        double width = 800;
        double height = 600;
        try {
            Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
            width = bounds.getWidth() / 1.35;
            height = bounds.getHeight() / 1.35;
        } catch (Exception e){
            e.printStackTrace();
        }
        Scene scene = new Scene(decorator, width, height);

        //加载css样式文件
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(Main.class.getResource("home/resources/css/home.css").toExternalForm());

        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/home/resources/icons/app.png")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
