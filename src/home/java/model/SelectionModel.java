package home.java.model;

import com.jfoenix.effects.JFXDepthManager;
import home.java.components.ImageBox;
import home.java.controllers.ControllerUtil;
import home.java.controllers.HomeController;
import lombok.Getter;

import java.util.ArrayList;

/**
 * 存放已选图片的工具类。
 * 包含一个集合Set，和一些对已选图片的显示状态的更改
 *
 * @author Grey
 */
public class SelectionModel {

    private static ArrayList<ImageBox> selection = new ArrayList<>();
    @Getter
    public static ArrayList<ImageModel> imageModelList = new ArrayList<>();
    private static HomeController hc = (HomeController) ControllerUtil.controllers.get(HomeController.class.getSimpleName());

    public static void add(ImageBox node) {
        if (!contains(node)){
            JFXDepthManager.setDepth(node, 4);
            node.getImageView2().setTranslateY(node.getImageView2().getTranslateY() - 5);
            selection.add(node);
            imageModelList.add(node.getIm());
            hc.selectedNumLabel.setText("| 已选中 " + selection.size() + " 张");
        }
        log();
    }

    public static void remove(ImageBox node) {
        JFXDepthManager.setDepth(node, 0);
        node.getCheckBox().setSelected(false);
        node.getImageView2().setTranslateY(node.getImageView2().getTranslateY() + 5);
        selection.remove(node);
        imageModelList.remove(node.getIm());
        hc.selectedNumLabel.setText("| 已选中 " + selection.size() + " 张");
        log();
    }

    public static void clear() {
        while (!selection.isEmpty()) {
            remove(selection.iterator().next());
        }
    }

    private static boolean contains(ImageBox node) {
        for (ImageBox ib : selection) {
            if (ib.equals(node))
                return true;
        }
        return false;
    }

    public static void log() {
        System.out.println("Items in model: " + selection);
    }
}
