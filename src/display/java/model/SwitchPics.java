package display.java.model;

import com.jfoenix.controls.JFXSnackbar;
import home.java.model.ImageModel;
import javafx.scene.layout.StackPane;

import java.io.IOException;

import java.util.ArrayList;

/**
 *@ProjName: OnlyViewer
 * @ClassName: FindImageModel
 * @Author: tudoudaren233
 * @Describe: 用于处理上下张图片的问题，暂未起用
 */

public class SwitchPics {

    private ArrayList<ImageModel> ilist;


    public SwitchPics(ArrayList<ImageModel> ilist){

        this.ilist = ilist;
    }

    //返回下一张照片
    public ImageModel nextImage(ImageModel im) throws IOException {
        int i = 0;
        for (i = 0; i < ilist.size(); i++) {
            if (ilist.get(i).getImageName().equals(im.getImageName())) {
                if (i == ilist.size() - 1) {
                    System.out.println("已到达最后一张");
                    return null;
                }
                break;
            }
        }
        return ilist.get((i + 1) % (ilist.size()));

    }

    //返回上一张照片
   public ImageModel lastImage(ImageModel im) throws IOException {
        int i = 0;
        for (i = 0; i < ilist.size(); i++) {
            if (ilist.get(i).getImageName().equals(im.getImageName())) {
                if (i == 0) {
                    System.out.println("已到达第一张照片");
                    return null;
                    //i=ilist.size();
                }
                break;
            }
        }
        return ilist.get((i - 1) % (ilist.size()));

    }
}
