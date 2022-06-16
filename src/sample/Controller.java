package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.dto.FileInformation;
import sample.service.AccessHistoryService;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private Button backHistoryButton;
    @FXML
    private Button nextHistoryButton;
    @FXML
    private Button backParentButton;
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
    @FXML
    private TableColumn<FileInformation, String> source;

    private ObservableList<FileInformation> data = FXCollections.observableArrayList();

    private AccessHistoryService<String> historyService = new AccessHistoryService<>();

    private List<File> listRootDirectory = new ArrayList<>();


    @FXML
    public void backHistory(ActionEvent actionEvent) {
        nextHistoryButton.setDisable(false);
        String path = historyService.prevItem();
        if (path == null) {
            backToRootDirectory();
            backHistoryButton.setDisable(true);
        } else {
            observableListChangeElement(path);
        }
    }

    @FXML
    public void nextHistory(ActionEvent actionEvent) {
        if(historyService.isInLast()){
            nextHistoryButton.setDisable(true);
        }
        String path = historyService.nextItem();
        if (path == null) {
            return;
        }
        backHistoryButton.setDisable(false);
        observableListChangeElement(path);
    }

    @FXML
    public void backParentDirectory() {
        File file = new File(uri.getText());
        File parent = file.getParentFile();
        if(parent == null) {
            backParentButton.setDisable(true);
            backToRootDirectory();
            return;
        }
        historyServiceAdd(parent.getPath());
        observableListChangeElement(parent.getPath());
    }

    @FXML
    public void uriChangeDirection() {
        String text = uri.getText();
        if (text == null || "".equals(text)) {
            backToRootDirectory();
        }
        File file = new File(text);
        if (file.exists()) {
            historyServiceAdd(text);
            observableListChangeElement(text);
        }
    }

    @FXML
    public void searchBoxEnterKey() {
        String text = searchBox.getText();
        if (text == null || "".equals(text)) {
            backCurrent();
            return;
        }
        //TODO triển khai code search
        File currentFile = new File(historyService.currentItem());
        List<File> resultList = new ArrayList<>();
        preOderTreeSearch(currentFile, text, resultList);
        if (resultList.size() == 0) {
            return;
        }
        File[] result = new File[resultList.size()];
        resultList.toArray(result);
        observableListChangeElement(result);
    }


    private void historyServiceAdd(String text) {
        historyService.add(text);
        backHistoryButton.setDisable(false);
        backParentButton.setDisable(false);
    }

    private void backToRootDirectory() {
        uri.setText(null);
        observableListChangeElement(rootDirectory());
    }

    public void preOderTreeSearch(final File file, final String word, final List<File> resultList) {
        if (file.listFiles() != null && file.isDirectory() && !file.getName().contains("$")) {
            File[] list = file.listFiles();
            Arrays.asList(list).forEach(childFile -> {
                if (childFile.getName().contains(word)) {
                    resultList.add(childFile);
                }
                preOderTreeSearch(childFile, word, resultList);
            });
        }
    }

    private void observableListChangeElement(String path) {
        File file = new File(path);
        uri.setText(path);
        observableListChangeElement(file.listFiles());
    }

    private void backCurrent() {
        observableListChangeElement(historyService.currentItem());
    }

    private void observableListChangeElement(File... files) {
        if (files == null) {
            data.removeAll(data);
            return;
        }
        data.clear();
        FileInformation[] fileInformations = FileInformation.parse(files);
        data.addAll(fileInformations);
        tableView.setItems(data);
    }

    private File[] rootDirectory() {
        File[] array = new File[listRootDirectory.size()];
        listRootDirectory.toArray(array);
        return array;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backHistoryButton.setDisable(true);
        nextHistoryButton.setDisable(true);
        backParentButton.setDisable(true);
        //place hold setup
        uri.setPromptText("This PC");
        searchBox.setPromptText("Search");

        //table view init
        image.setCellValueFactory(new PropertyValueFactory<>("image"));
        fileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        dateModified.setCellValueFactory(new PropertyValueFactory<>("dateModified"));
        size.setCellValueFactory(new PropertyValueFactory<>("size"));
        source.setCellValueFactory(new PropertyValueFactory<>("path"));

        Iterator iterator = Paths.get(System.getProperty("user.dir")).getFileSystem().getRootDirectories().iterator();
        while (iterator.hasNext()) {
            Path path = (Path) iterator.next();
            File file = path.toFile();
            FileInformation fileInformation = FileInformation.parse(file);
            listRootDirectory.add(file);
            data.add(fileInformation);
        }

        tableView.setItems(data);

        tableView.setRowFactory(tv -> {
            TableRow<FileInformation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())) {
                    FileInformation fileInformation = row.getItem();
                    if(fileInformation.isDirectory()) {
                        historyServiceAdd(fileInformation.getPath());
                        observableListChangeElement(fileInformation.getPath());
                    }
                }
            });
            return row;
        });
    }
}