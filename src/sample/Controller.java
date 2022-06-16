package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    private AccessHistoryService<String> historyService = new AccessHistoryService<>();

    private List<File> listRootDirectory = new ArrayList<>();

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
            File file = path.toFile();
            FileInformation fileInformation = FileInformation.parse(file);
            listRootDirectory.add(file);
            data.add(fileInformation);
        }

        tableView.setItems(data);

        TableView.TableViewSelectionModel<FileInformation> fileSelect = tableView.getSelectionModel();
        fileSelect.selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                int index = (int) newVal;
                FileInformation fileInformation1 = data.get(index);
                if(fileInformation1.isDirectory()) {
                    historyService.add(fileInformation1.getPath());
                    observableListChangeElement(fileInformation1.getPath());
                }
                index = -1;
            }
        });
    }

    @FXML
    public void backHistory(ActionEvent actionEvent){
        String path = historyService.prevItem();
        if(path == null){
            uri.setText(null);
            observableListChangeElement(rootDirectory());
        } else {
            observableListChangeElement(path);
        }
    }

    @FXML
    public void nextHistory(ActionEvent actionEvent){
        String path = historyService.nextItem();
        if(path == null){
            return;
        }
        observableListChangeElement(path);
    }

    private void observableListChangeElement(String path){
        File file = new File(path);
        uri.setText(path);
        observableListChangeElement(file.listFiles());
    }

    private void observableListChangeElement(File... files){
        if(files == null){
            data.removeAll(data);
            return;
        }
        data.clear();
        FileInformation[] fileInformations = FileInformation.parse(files);
        data.addAll(fileInformations);
    }

    private File[] rootDirectory(){
        File[] array = new File[listRootDirectory.size()];
        listRootDirectory.toArray(array);
        return array;
    }
}