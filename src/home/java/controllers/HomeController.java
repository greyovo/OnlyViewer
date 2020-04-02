package home.java.controllers;

import com.jfoenix.controls.JFXTreeView;
import home.java.components.ImageBox;
import home.java.components.ImageLabel;
import home.java.components.ImageView2;
import home.java.components.RipplerImageView;
import home.java.model.ImageListModel;
import home.java.model.ImageModel;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.junit.Test;

import javax.annotation.PostConstruct;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

    @FXML
    private ImageView2 imageView;

    @FXML
    private VBox imageVBox;

    @FXML
    private FlowPane imageListPane;

    public HomeController() {
    }

    /**
     * 初始化FXML文件
     */
    @PostConstruct
    public void init() throws Exception {
        System.out.println("Home Window init running...");
        setFileTreeView();

        //这里换成你的本地路径
        String path = "D:\\TestImg";
        placeImages(ImageListModel.initImgList(path)); // 这里是要用初始化方法
    }

//    public void test() {
//        ArrayList<VBox> imageBoxList = new ArrayList<>();
//        //图片 - 缩略图
//        new JFXPanel();
//        ImageView2 imageView2 = new ImageView2(new Image("http://www.javafxchina.net/blog/wp-content/uploads/2015/08/08_03_03-basic-timeline.jpg"));
//        RipplerImageView riv = new RipplerImageView(imageView2);
//        //标签 - 文件名
//        ImageLabel imageLabel = new ImageLabel(imageView2.getImage().toString());
//        //装图片和文件名的盒子，一上一下放置图片和文件名
//        ImageBox imageBox = new ImageBox(riv, imageLabel);
//        imageBoxList.add(imageBox);
//        imageListPane.getChildren().addAll(imageBoxList);
//    }

    /**
     * 更新当前图片列表
     *
     * @param url 需要刷新的文件夹路径
     */
    private void refreshImagesList(String url) {
        /*TODO @Kevin 在这里获取图片ArrayList后，调用下面的placeImages渲染到界面*/
    }

    /**
     * 生成并往面板中放置图像组。
     * 一个缩略图单元包含：一个图片ImageView（由{@link RipplerImageView}包装从而实现水波纹效果）和一个标签 {@link ImageLabel}
     */
    private void placeImages(ArrayList<ImageModel> imageModelList) {
        ArrayList<VBox> imageBoxList = new ArrayList<>();
        System.out.println(imageModelList);
        for (ImageModel im : imageModelList) {
            //图片 - 缩略图
            ImageView2 imageView2 = new ImageView2(new Image("File:"+im.getImageFilePath(),true));
//            ImageView2 imageView2 = new ImageView2(new Image("File://"+"F:/Pictures"));
//            ImageView2 imageView2 = new ImageView2("File://"+im.getImageFilePath());
//            ImageView2 imageView2 = new ImageView2(new Image("https://edu-image.nosdn.127.net/1f51fa06a0b14fa3809af4ab20a65e14.png?imageView&quality=100"));
            RipplerImageView riv = new RipplerImageView(imageView2);

            //标签 - 文件名
            ImageLabel imageLabel = new ImageLabel(im.getImageName());

            //装图片和文件名的盒子，一上一下放置图片和文件名
            ImageBox imageBox = new ImageBox(riv, imageLabel);

            imageBoxList.add(imageBox);
        }
        imageListPane.getChildren().addAll(imageBoxList);
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
                addItems(c,0);
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
                for(File isListRoots:rootList){
                    if(object.toString().equals(isListRoots.toString())){
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
                    addItems(newValue,0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addItems(TreeItem<File> in,int flag) throws IOException {
        File[] filelist = in.getValue().listFiles();
        if(filelist != null){
            //System.out.println(in.getValue().getName());
            if(flag==0){
                in.getChildren().remove(0,in.getChildren().size());
            }

            if (filelist.length>0){
                for (int i = 1;i<filelist.length;i++){
                    if (filelist[i].isDirectory()&filelist[i].canRead()&!filelist[i].isHidden()){
                        TreeItem<File> b = new TreeItem<File>(filelist[i]);
                        if (flag<1){
                            addItems(b,flag+1);

                        }
                        in.getChildren().add(b);

                    }

                }
            }
        }
    }


}
