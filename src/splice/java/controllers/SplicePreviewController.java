package splice.java.controllers;

import home.java.model.ImageModel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import javax.swing.text.html.ImageView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

// TODO: 2020/5/12 拼接功能 
public class SplicePreviewController {

    private Set<ImageModel> imageModelSet;

    public void setImageModelSet(Set<ImageModel> set) {
        this.imageModelSet = set;
    }
    
    //截图
    private void snap(ImageView imageView) {
//        //将下面解除注释！！！当把imageview放到scene中则不会报错
//        WritableImage wa = imageView.getParent().snapshot(null, null);
//        try {
//            BufferedImage buff = SwingFXUtils.fromFXImage(wa, null);
//            ImageIO.write(buff, "png", new File("test" + ".png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


}
