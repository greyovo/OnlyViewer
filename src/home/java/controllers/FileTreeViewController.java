package home.java.controllers;

import com.jfoenix.controls.JFXTreeView;
import home.java.model.SortParam;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 左侧文件目录树
 *
 * @author tudou daren
 */
public class FileTreeViewController implements Initializable {

    @FXML
    private JFXTreeView<File> fileTreeView;

    private HomeController hc;

    public FileTreeViewController() {
        //将本类的实例添加到全局映射中
        ControllerUtil.controllers.put(this.getClass().getSimpleName(), this);
        System.out.println("put FileTreeViewCon in Map...");
        hc = (HomeController) ControllerUtil.controllers.get(HomeController.class.getSimpleName());
        System.out.println("hc in FileTreeViewCon: " + hc);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setFileTreeView();
    }

    private void setFileTreeView() {
        //定义目录树
        File[] rootList = File.listRoots();
        TreeItem<File> mainTreeItem = new TreeItem<>(rootList[0]);

        for (File root : rootList) {
            TreeItem<File> rootItem = new TreeItem<>(root);
            try {
                addItems(rootItem, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mainTreeItem.getChildren().add(rootItem);
        }

        fileTreeView.setRoot(mainTreeItem);
        fileTreeView.setShowRoot(false);

        //自定义单元格，设置文件夹图片
        fileTreeView.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
            @Override
            public TreeCell<File> call(TreeView<File> param) {
                TreeCell<File> treeCell = new TreeCell<File>() {
                    @Override
                    protected void updateItem(File item, boolean empty) {

                        if (!empty) {
                            super.updateItem(item, empty);
                            HBox hBox = new HBox();
                            Label label = new Label(isListRoots(item));

                            this.setGraphic(hBox);

                            if (this.getTreeItem().isExpanded()) {
                                ImageView folderImage = new ImageView("icons/opened_folder.png");
                                folderImage.setPreserveRatio(true);
                                folderImage.setFitWidth(22);
                                hBox.getChildren().add(folderImage);//加图片
                                //this.setDisclosureNode(folderImage);
                                this.setGraphic(hBox);
                            } else if (!this.getTreeItem().isExpanded()) {
                                ImageView folderImage = new ImageView("icons/folder.png");
                                folderImage.setPreserveRatio(true);
                                folderImage.setFitWidth(22);
                                hBox.getChildren().add(folderImage);//加图片
                                this.setGraphic(hBox);
                            }
                            hBox.getChildren().add(label);//加文字
                        } else if (empty) {
                            this.setGraphic(null);

                        }
                    }
                };
                return treeCell;
            }
        });

        System.out.println("hc: " + hc);

        //获取点击操作并刷新当前结点
        fileTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<File>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<File>> observable, TreeItem<File> oldValue, TreeItem<File> newValue) {
                //此处点击可获得文件夹绝对路径
                String path = newValue.getValue().getAbsolutePath();
                System.out.println(path);
                try {
                    //入栈以便于后续前进后退
                    hc.initEnterFolder(path);
                    // 只要点击一次排序后以后每次进入新页面就置为"按名字升序"
                    hc.getSortComboBox().setValue(SortParam.SBNR);
                    addItems(newValue, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 递归将节点加入目录树中
     */
    public void addItems(TreeItem<File> in, int flag) throws IOException {
        File[] filelist = in.getValue().listFiles();
        //flag判断当前遍历的层数
        if (filelist != null) {
            if (flag == 0) {
                in.getChildren().remove(0, in.getChildren().size());
            }
            if (filelist.length > 0) {
                for(File f:filelist){
                    if(f.isDirectory()&!f.isHidden()){
                        TreeItem<File> newItem = new TreeItem<File>(f);
                        if (flag < 1) {
                            addItems(newItem, flag + 1);
                        }
                        in.getChildren().add(newItem);
                    }
                    }
                }
            }
        }


    /**
     * 判断是否为根目录
     */
    public String isListRoots(File item) {
        File[] rootlist = File.listRoots();
        for (File isListRoots : rootlist) {
            if (item.toString().equals(isListRoots.toString())) {
                return item.toString();
            }
        }
        return item.getName();
    }


}
