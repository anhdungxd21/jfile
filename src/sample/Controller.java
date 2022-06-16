package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.dto.FileInformation;
import sample.service.AccessHistoryService;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
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

    private AccessHistoryService<File> historyService = new AccessHistoryService<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //place hold setup
        uri.setPromptText("This PC");
        searchBox.setPromptText("Search");

        //table view init
        image.setCellValueFactory(new PropertyValueFactory<>("image"));
        fileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        dateModified.setCellValueFactory(new PropertyValueFactory<>("dateModified"));
        size.setCellValueFactory(new PropertyValueFactory<>("size"));

        Iterator iterator = Paths.get(System.getProperty("user.dir")).getFileSystem().getRootDirectories().iterator();
        while (iterator.hasNext()) {
            Path path = (Path) iterator.next();
            data.add(FileInformation.parse(path.toFile()));
        }


        tableView.setItems(data);

        TableView.TableViewSelectionModel<FileInformation> fileSelect = tableView.getSelectionModel();
        fileSelect.selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                int index = (int) newVal;
                FileInformation fileInformation1 = data.get(index);
                if(fileInformation1.isDirectory()) {
                    observableListChangeElement(fileInformation1.getPath());
                }
            }
        });
    }

    private void observableListChangeElement(String path){
        File file = new File(path);
        if(file.listFiles() == null){
            data.removeAll(data);
            return;
        }
        data.clear();
        FileInformation[] files = FileInformation.parse(file.listFiles());
        data.addAll(files);
    }
}