package home.java.model;

import com.jfoenix.effects.JFXDepthManager;
import home.java.components.ImageBox;
import javafx.scene.Node;
import lombok.Getter;

import java.util.ArrayList;
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

//    public static Set<ImageModel> toSet() {
//        imageModelSet.clear();
//        for (ImageBox node :
//                selection) {
//            imageModelSet.add(node.getIm());
//        }
//        return imageModelSet;
//    }

    public static void add(ImageBox node) {
        JFXDepthManager.setDepth(node, 4);
        node.setTranslateY(node.getTranslateY() - 5);
        selection.add(node);
        imageModelSet.add(node.getIm());
        log();
    }

    public static void remove(ImageBox node) {
        JFXDepthManager.setDepth(node, 0);
        node.getCheckBox().setSelected(false);
        node.setTranslateY(node.getTranslateY() + 5);
        selection.remove(node);
        imageModelSet.remove(node.getIm());
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
