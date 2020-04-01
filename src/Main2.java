
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;


import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;


public class Main2 extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        TreeView<File> treeview =new  TreeView<File>();

        File[] rootlist = File.listRoots();
        TreeItem<File> a = new TreeItem<>(rootlist[0]);

        for (File root:rootlist){
            TreeItem<File> c = new TreeItem<>(root);
            add_item2(c);
            a.getChildren().add(c);
        }

        treeview.setRoot(a);
        treeview.setShowRoot(false);

        //将节点输出为文件名称
        treeview.setCellFactory(TextFieldTreeCell.forTreeView(new StringConverter<File>() {

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
        treeview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<File>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<File>> observable, TreeItem<File> oldValue, TreeItem<File> newValue) {
                System.out.println(newValue.getValue().getAbsolutePath());
                try {
                    add_item2(newValue);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        StackPane root = new StackPane();
        root.getChildren().add(treeview);
        primaryStage.setScene(new Scene(root, 500, 500));

        primaryStage.setTitle("filetree_test");
        primaryStage.show();

    }

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        launch(args);


//        FileSystemView fsv = FileSystemView.getFileSystemView();
//        //获取当前电脑的所有磁盘名称(速度稍微有点慢)
//        File[] roots = File.listRoots();
//        for(File root :roots){
//            System.out.println(fsv.getSystemDisplayName(root));
//        }

    }
    //递归遍历所有文件夹，会报错
    public void add_item(TreeItem<File> in) throws IOException {
        File[] filelist = in.getValue().listFiles();
        if (filelist.length>0){
            for (File item:filelist){
                if (item.isDirectory()&item.canRead()){
                    TreeItem<File> b = new TreeItem<>(item);
                    in.getChildren().add(b);
                    add_item(b);
                }

            }
        }
    }
    //获取当前目录的文件并判断是否在该节点中，不在则加入，带有刷新机制
    public void add_item2(TreeItem<File> in) throws IOException {
        File[] filelist = in.getValue().listFiles();
        //System.out.println(in.getValue().getName());
        in.getChildren().remove(0,in.getChildren().size());
        if (filelist.length>0){
            for (int i = 1;i<filelist.length;i++){
                if (filelist[i].isDirectory()&filelist[i].canRead()&!filelist[i].isHidden()){
                    TreeItem<File> b = new TreeItem<File>(filelist[i]);
                    in.getChildren().add(b);

                }

            }
        }

    }
}


