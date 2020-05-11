package home.java.model;

import com.jfoenix.effects.JFXDepthManager;
import home.java.components.ImageBox;
import home.java.controllers.ControllerUtil;
import home.java.controllers.HomeController;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 存放已选图片的工具类。
 * 包含一个集合Set，和一些对已选图片的显示状态的更改
 *
 * @author Grey
 */
public class SelectionModel {

    private static Set<ImageBox> selection = new HashSet<>();
    @Getter
    public static Set<ImageModel> imageModelSet = new HashSet<>();
    private static HomeController hc = (HomeController) ControllerUtil.controllers.get(HomeController.class.getSimpleName());

    public static void add(ImageBox node) {
        JFXDepthManager.setDepth(node, 4);
        node.getImageView2().setTranslateY(node.getImageView2().getTranslateY() - 5);
        selection.add(node);
        imageModelSet.add(node.getIm());
        hc.selectedNumLabel.setText("| 已选中 " + selection.size() + " 张");
        log();
    }

    public static void remove(ImageBox node) {
        JFXDepthManager.setDepth(node, 0);
        node.getCheckBox().setSelected(false);
        node.getImageView2().setTranslateY(node.getImageView2().getTranslateY() + 5);
        selection.remove(node);
        imageModelSet.remove(node.getIm());
        hc.selectedNumLabel.setText("| 已选中 " + selection.size() + " 张");
        log();
    }

    public static void clear() {
        while (!selection.isEmpty()) {
            remove(selection.iterator().next());
        }
    }

    public static boolean contains(ImageBox node) {
        return selection.contains(node);
    }

    public static void log() {
        System.out.println("Items in model: " + Arrays.asList(selection.toArray()));
    }
}
