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
 * 关联fxml：FileTreeView.fxml
 * @author tudou daren
 */
public class FileTreeViewController implements Initializable {

    @FXML
    private JFXTreeView<File> fileTreeView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


}
