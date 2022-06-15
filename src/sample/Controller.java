package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.dto.FileInformation;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField uri;
    @FXML
    private TextField searchBox;

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<FileInformation, String> image;
    @FXML
    private TableColumn<FileInformation, String> fileName;
    @FXML
    private TableColumn<FileInformation, String> dateModified;
    @FXML
    private TableColumn<FileInformation, String> size;

    private ObservableList<FileInformation> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        image.setCellValueFactory(new PropertyValueFactory<>("image"));
        fileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        dateModified.setCellValueFactory(new PropertyValueFactory<>("dateModified"));
        size.setCellValueFactory(new PropertyValueFactory<>("size"));

        ImageView photo = new ImageView(new Image(this.getClass().getResourceAsStream("resource/file.png")));
        ImageView photo2 = new ImageView(new Image(this.getClass().getResourceAsStream("resource/folder.png")));
        FileInformation fileInformation = new FileInformation(photo,"D", "22/01",3000);
        FileInformation fileInformation2 = new FileInformation(photo2,"E", "23/01",3500);
        data.addAll(fileInformation,fileInformation2);

        tableView.setItems(data);

//        ObservableList obsList
//        Refilling a list with completely new content then works as follows:
//
//        obsList.removeAll(obsList);
//        obsList.add(...);  //e.g. in a loop...
//        or
//
//        obsList.removeAll(obsList);
//        FXCollections.copy(obsList, someSourceList)
    }
}
