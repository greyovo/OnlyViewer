package display;

import com.jfoenix.controls.JFXDecorator;
import display.java.controllers.DisplayWindowController;
import home.java.controllers.HomeController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class DisplayWindow extends Application {

    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    private double width = 800;
    private double height = 600;


    private ImageView imageView;

    private Image image;

    @Override
    public void init() throws Exception {
        super.init();
        //根据屏幕大小自适应设置长宽
        try {
            Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
            width = bounds.getWidth() / 1.5;
            height = bounds.getHeight() / 1.5;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
//        init();

        /* Copy from JFoenix Demo */
//        Flow flow = new Flow(DisplayWindowController.class);
//        DefaultFlowContainer container = new DefaultFlowContainer();
//        flowContext = new ViewFlowContext();
//        flowContext.register("Stage", stage);
//        flow.createHandler(flowContext).start(container);
//        JFXDecorator decorator = new JFXDecorator(stage, container.getView());  //自定义JFX的窗口边框样式
//        decorator.setCustomMaximize(true);
//        decorator.setGraphic(new ImageView("/home/resources/icons/app.png"));  //设置标题栏左侧小图标
//        decorator.setContent(anchorPane);
//        Scene scene = new Scene(decorator, width, height);

        Parent root = FXMLLoader.load(getClass().getResource("/display/resources/fxml/displayWindow.fxml"));
        Scene scene = new Scene(new JFXDecorator(stage, root), width, height);
        StackPane stackPane = (StackPane) root;
        imageView.fitHeightProperty().bind(stackPane.heightProperty());
        stackPane.getChildren().add(imageView);

        //加载css样式文件
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(this.getClass().getResource("/display/resources/css/display.css").toExternalForm());

        stage.setTitle("图片查看窗口");
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/home/resources/icons/app.png")));
        stage.setScene(scene);
        stage.show();
    }

    public void setImage(Image image) throws Exception {
        init();
        this.image = image;
        this.imageView = new ImageView(image);
        System.out.println(image.getWidth() + "*" + image.getHeight());
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
//        imageView.setFitHeight(height*0.8);
//        anchorPane.getChildren().add(imageView);
    }
}
