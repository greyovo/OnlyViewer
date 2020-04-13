import home.java.components.MouseImageMenu;
import home.java.model.ImageModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MouseMenu extends Application {
    @Override
    public void start(Stage primaryStage)
    {
        Label label = new Label();
        // TODO 修改你的图片路径
        String path = "C:\\Users\\Kevin\\Pictures\\MY STYLE\\20180704114840_25326.jpg";
        label.setContextMenu(MouseImageMenu.getInstance(new ImageModel(path)));
        Scene scene1 = new Scene(label, 600, 400);
        primaryStage.setScene(scene1);
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
