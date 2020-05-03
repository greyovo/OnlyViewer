package display;

import com.jfoenix.controls.JFXDecorator;
import display.java.controllers.DisplayWindowController;
import home.java.model.ImageListModel;
import home.java.model.ImageModel;
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

import java.util.ArrayList;

/**
 * 图片单独展示窗口
 *
 * @author Grey
 */
public class DisplayWindow extends Application {

    public static double windowWidth = 800;     //窗口宽度
    public static double windowHeight = 600;    //窗口高度

    private ImageView imageView;
    private Image image;
    private ImageModel im;
    private ArrayList<ImageModel> imageModelArrayList;
    private StackPane stackPane;

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

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/display/resources/fxml/displayWindow.fxml"));
        Parent root = fxmlLoader.load();
        DisplayWindowController dwController = fxmlLoader.getController();

        Scene scene = new Scene(new JFXDecorator(stage, root), windowWidth, windowHeight);
//        StackPane stackPane = (StackPane) root;
        stackPane = dwController.getStackPane();

        imageView.fitHeightProperty().bind(stackPane.heightProperty());
        imageView.fitWidthProperty().bind(stackPane.widthProperty());
//        dwController.getStackPane().getChildren().add(imageView);
        dwController.setImageView(imageView);
        dwController.getStackPane().getChildren().add(dwController.getImageView());

        //加载css样式文件
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(this.getClass().getResource("/display/resources/css/display.css").toExternalForm());

        stage.setTitle("图片查看窗口");
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/home/resources/icons/app.png")));
        stage.setScene(scene);
        stage.show();
        dwController.test();
    }

    public void setImage(ImageModel im) throws Exception {
        init();
        this.im = im;
        this.image = new Image(im.getImageFile().toURI().toString());
        this.imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        //适应图片比例，避免宽度过大显示不全
        ratio = image.getWidth() / image.getHeight();
        if (ratio > 1) {
            imageView.setFitWidth(windowWidth);
        } else {
            imageView.setFitHeight(windowHeight);
        }

        System.out.println("image size:\n"+image.getWidth() + "*" + image.getHeight());

        imageModelArrayList = ImageListModel.refreshList(im.getImageFilePath());
        System.out.println("In display window - current list:\n"+imageModelArrayList);
    }
}
