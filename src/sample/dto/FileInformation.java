package sample.dto;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.Controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FileInformation {
    private final ImageView image;
    private final SimpleStringProperty fileName;
    private final SimpleStringProperty dateModified;
    private final SimpleLongProperty size;
    private String path;
    private boolean isDirectory;

    public FileInformation(ImageView image, String fileName, String dateModified, long size, String path, boolean isDirectory) {
        this.image = image;
        this.fileName = new SimpleStringProperty(fileName);
        this.dateModified = new SimpleStringProperty(dateModified);
        this.size = new SimpleLongProperty(size);
        this.path = path;
        this.isDirectory = isDirectory;
    }

    public static FileInformation parse(File file) {
        ImageView imageView = file.isDirectory() ?
                new ImageView(new Image(Controller.class.getResourceAsStream("resource/folder.png"))) :
                new ImageView(new Image(Controller.class.getResourceAsStream("resource/file.png")));

        String fileName = file.getName().equals("") ? file.getPath() : file.getName();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm aa");
        String dateModified = sdf.format(file.lastModified());

        long bytes =  file.length()/1024;

        return new FileInformation(imageView, fileName, dateModified, bytes, file.getPath(), file.isDirectory());
    }

    public static FileInformation[] parse(File... files){
        List<FileInformation> list = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().equals("") || !files[i].isHidden()) {
                list.add(parse(files[i]));
            }
        }
        FileInformation[] fileInformations = new FileInformation[list.size()];
        list.toArray(fileInformations);
        return fileInformations;
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

    public long getSize() {
        return size.get();
    }


    public void setSize(int size) {
        this.size.set(size);
    }

    public String getPath() {
        return path;
    }

    public boolean isDirectory() {
        return isDirectory;
    }
}
