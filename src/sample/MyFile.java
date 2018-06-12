package sample;

import javafx.scene.control.CheckBox;

import java.io.File;

public class MyFile {

    private File file;
    private CheckBox checkBox = new CheckBox();
    private String name;

    MyFile(File file) {
        this.file = file;
        this.checkBox.setSelected(true);
        this.name = file.getName();
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
