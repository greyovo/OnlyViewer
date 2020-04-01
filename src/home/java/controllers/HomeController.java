package home.java.controllers;

import com.jfoenix.controls.JFXTreeView;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.Image;
import javafx.util.StringConverter;

import javax.annotation.PostConstruct;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;

/**
 * 主窗口界面的控制器
 *
 * @author Grey
 * @since 2020.04.02
 */

@ViewController(value = "/fxml/Home.fxml", title = "Home Window")
public class HomeController {

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    private JFXTreeView<File> fileTreeView;

    @FXML
    private Label imageNameLabel;

    @FXML
    private Image image;

    public HomeController() {
    }

    /**
     * 初始化FXML文件
     */
    @PostConstruct
    public void init() throws Exception {
        System.out.println("Home Window init running...");
        setFileTreeView();
    }

    /**
     * 设置左侧文件目录树
     *
     * @author tudou daren
     */
    private void setFileTreeView() {

        File[] rootList = File.listRoots();
        TreeItem<File> a = new TreeItem<>(rootList[0]);

        for (File root : rootList) {
            TreeItem<File> c = new TreeItem<>(root);
            try {
                addItems(c);
            } catch (IOException e) {
                e.printStackTrace();
            }
            a.getChildren().add(c);
        }

        fileTreeView.setRoot(a);
        fileTreeView.setShowRoot(false);

        //将节点输出为文件名称
        fileTreeView.setCellFactory(TextFieldTreeCell.forTreeView(new StringConverter<File>() {

            @Override
            public String toString(File object) {
                FileSystemView fsv = FileSystemView.getFileSystemView();

                return object.getName();
                //return fsv.getSystemDisplayName(object);
            }

            @Override
            public File fromString(String string) {
                return null;
            }
        }));

        //获取点击操作并刷新当前结点
        fileTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<File>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<File>> observable, TreeItem<File> oldValue, TreeItem<File> newValue) {
                System.out.println(newValue.getValue().getAbsolutePath());
                try {
                    addItems(newValue);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addItems(TreeItem<File> in) throws IOException {
        File[] fileList = in.getValue().listFiles();
        //System.out.println(in.getValue().getName());
        in.getChildren().remove(0, in.getChildren().size());
        if (fileList.length > 0) {
            for (int i = 1; i < fileList.length; i++) {
                if (fileList[i].isDirectory() & fileList[i].canRead() & !fileList[i].isHidden()) {
                    TreeItem<File> b = new TreeItem<File>(fileList[i]);
                    in.getChildren().add(b);
                }

            }
        }

    }


}
