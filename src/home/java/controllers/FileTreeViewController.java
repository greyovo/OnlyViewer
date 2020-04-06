package home.java.controllers;

import com.jfoenix.controls.JFXTreeView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 左侧文件目录树
 *
 * FIXME 此类因为无法生效，暂时未启用 2020-4-6
 * 关联fxml：fileTreeView.fxml
 * @author tudou daren
 */
public class FileTreeViewController implements Initializable {

    @FXML
    private JFXTreeView<File> fileTreeView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setFileTreeView();
    }

    private void setFileTreeView() {

        File[] rootList = File.listRoots();
        TreeItem<File> a = new TreeItem<>(rootList[0]);

        for (File root : rootList) {
            TreeItem<File> c = new TreeItem<>(root);
            try {
                addItems(c, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            a.getChildren().add(c);
        }

        fileTreeView.setRoot(a);
        fileTreeView.setShowRoot(false);

        //将节点输出为文件名称
        fileTreeView.setCellFactory(TextFieldTreeCell.forTreeView(new StringConverter<File>() {

            //FileSystemView fsv = FileSystemView.getFileSystemView();
            @Override
            public String toString(File object) {
                //简单判断是否为根目录，是则换一种方式显示
                for (File isListRoots : rootList) {
                    if (object.toString().equals(isListRoots.toString())) {
                        //return fsv.getSystemDisplayName(object);
                        return object.toString();
                    }
                }
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
                    addItems(newValue, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addItems(TreeItem<File> in, int flag) throws IOException {
        File[] filelist = in.getValue().listFiles();
        if (filelist != null) {
            //System.out.println(in.getValue().getName());
            if (flag == 0) {
                in.getChildren().remove(0, in.getChildren().size());
            }

            if (filelist.length > 0) {
                for (int i = 1; i < filelist.length; i++) {
                    if (filelist[i].isDirectory() & filelist[i].canRead() & !filelist[i].isHidden()) {
                        TreeItem<File> b = new TreeItem<File>(filelist[i]);
                        if (flag < 1) {
                            addItems(b, flag + 1);
                        }
                        in.getChildren().add(b);
                    }

                }
            }
        }
    }
}
