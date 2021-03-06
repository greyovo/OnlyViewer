package onlyviewer.display.java.model;

import com.jfoenix.controls.JFXSnackbar;
import onlyviewer.display.java.controllers.DisplayWindowController;
import onlyviewer.home.java.controllers.ControllerUtil;
import onlyviewer.home.java.model.ImageModel;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @ProjName: OnlyViewer
 * @ClassName: SwitchPics
 * @Author: tudou daren
 * @Describe: 用于处理上下张图片切换类
 * @since 2020.05
 */

public class SwitchPics {

    private ArrayList<ImageModel> ilist;

    JFXSnackbar snackbar;
    DisplayWindowController dw;

    public SwitchPics() {
        dw = (DisplayWindowController) ControllerUtil.controllers.get(DisplayWindowController.class.getSimpleName());
        snackbar = new JFXSnackbar(dw.getRootPane());
    }

    public SwitchPics(ArrayList<ImageModel> ilist) {
        this();
        this.ilist = ilist;
    }

    //返回下一张照片
    public ImageModel nextImage(ImageModel im) throws IOException {
        int i = 0;
        for (i = 0; i < ilist.size(); i++) {
            if (ilist.get(i).getImageName().equals(im.getImageName())) {
                if (i == ilist.size() - 1) {
                    System.out.println("已到达最后一张, 正在查看第一张");
                    snackbar.enqueue(new JFXSnackbar.SnackbarEvent("正在查看第一张"));
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
                    System.out.println("已到达第一张照片, 正在查看最后一张");
                    snackbar.enqueue(new JFXSnackbar.SnackbarEvent("正在查看最后一张"));
                    i = ilist.size();
                }
                break;
            }
        }
        return ilist.get((i - 1) % (ilist.size()));

    }
}
