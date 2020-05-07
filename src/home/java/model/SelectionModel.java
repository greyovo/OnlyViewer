package home.java.model;

import com.jfoenix.effects.JFXDepthManager;
import home.java.components.ImageBox;
import javafx.scene.Node;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 存放已选图片的工具类。
 * 包含一个集合Set，和一些对已选图片的显示状态的更改
 *
 * @author Grey
 * */
public class SelectionModel {

    private static Set<Node> selection = new HashSet<>();

    public static void add(Node node) {
        JFXDepthManager.setDepth(node, 4);
        node.setTranslateY(node.getTranslateY() - 5);
        selection.add(node);
        log();
    }

    public static void remove(Node node) {
        JFXDepthManager.setDepth(node, 0);
        ImageBox imageBox = (ImageBox) node;
        imageBox.getCheckBox().setSelected(false);
        node.setTranslateY(node.getTranslateY() + 5);
        selection.remove(node);
        log();
    }

    public static void clear() {
        while (!selection.isEmpty()) {
            remove(selection.iterator().next());
        }
    }

    public static boolean contains(Node node) {
        return selection.contains(node);
    }

    public static void log() {
        System.out.println("Items in model: " + Arrays.asList(selection.toArray()));
    }
}
