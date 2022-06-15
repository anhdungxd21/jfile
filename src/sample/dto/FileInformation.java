package sample.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class FileInformation {
    private final ImageView image;
    private final SimpleStringProperty fileName;
    private final SimpleStringProperty dateModified;
    private final SimpleIntegerProperty size;

    public FileInformation(ImageView image, String fileName, String dateModified, int size) {
        this.image = image;
        this.fileName = new SimpleStringProperty(fileName);
        this.dateModified = new SimpleStringProperty(dateModified);
        this.size = new SimpleIntegerProperty(size);
    }

    public ImageView getImage() {
        return image;
    }

    public String getFileName() {
        return fileName.get();
    }


    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public String getDateModified() {
        return dateModified.get();
    }


    public void setDateModified(String dateModified) {
        this.dateModified.set(dateModified);
    }

    public int getSize() {
        return size.get();
    }


    public void setSize(int size) {
        this.size.set(size);
    }
}
