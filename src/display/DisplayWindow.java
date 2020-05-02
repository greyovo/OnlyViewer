package display;

import com.jfoenix.controls.JFXDecorator;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * 图片单独展示窗口
 *
 * @author Grey
 */
public class DisplayWindow extends Application {

    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    private double windowWidth = 800;     //窗口宽度
    private double windowHeight = 600;    //窗口高度

    private ImageView imageView;

    private Image image;

    private double ratio; // 图片比例（宽/高），用于决定是适应图片的高度还是宽度

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

        Parent root = FXMLLoader.load(getClass().getResource("/display/resources/fxml/displayWindow.fxml"));
        Scene scene = new Scene(new JFXDecorator(stage, root), windowWidth, windowHeight);
        StackPane stackPane = (StackPane) root;

        imageView.fitHeightProperty().bind(stackPane.heightProperty());
        if (imageView.fitWidthProperty().greaterThan(stackPane.widthProperty()).get())
            imageView.fitWidthProperty().bind(stackPane.widthProperty());

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
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        ratio = image.getWidth() / image.getHeight();
        if (ratio > 1) {
            imageView.setFitWidth(windowWidth);
        } else {
            imageView.setFitHeight(windowHeight);
        }

        System.out.println(image.getWidth() + "*" + image.getHeight());
    }
}
