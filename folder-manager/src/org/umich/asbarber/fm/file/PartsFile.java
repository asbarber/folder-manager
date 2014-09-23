package org.umich.asbarber.fm.file;

import org.umich.asbarber.fm.file.Directory;
import org.umich.asbarber.fm.file.manip.FileMover;
import org.umich.asbarber.fm.file.manip.FileRenamer;
import java.io.File;

/**
 *
 * @author Aaron
 */
public class PartsFile {

    private File main;

    public PartsFile(File f) {
        main = f;
    }

    public PartsFile(String path) {
        this(new File(path));
    }

    public void setFile(File newFile) {
        main = newFile;
    }

    public String getPath() {
        String path = main.getPath();
        int i = path.lastIndexOf("\\");

        //No parent
        if (i == -1) {
            return path + "\\";
        }

        return path.substring(0, i + 1);
    }

    public String getName() {
        String name = main.getName();
        String ext = getExtension();
        return name.substring(0, name.length() - ext.length());
    }

    public String getExtension() {
        int i = main.getName().lastIndexOf(".");

        if (i == -1) {
            return "";
        }

        return main.getName().substring(i);
    }

    public String getParent() {
        int i = getPath().lastIndexOf("\\");

        if (i == -1) {
            return "";
        }

        return main.getPath().substring(0, i);
    }

    public Directory getParentDirectory() {
        String parent = getParent();
        if (parent == null) {
            return null;
        }

        return new Directory(new File(parent));
    }

    public File getFile() {
        return main;
    }

    public boolean isDirectory() {
        return main.isDirectory();
    }

    public void renameFile(String newName) {
        FileRenamer renamer = new FileRenamer(main);
        renamer.rename(newName);
        main = renamer.getFile();
    }

    public void renameFile(String newName, int index) {
        renameFile(newName + " (" + index + ")");
    }

    public void moveFile(String newPath) {
        FileMover mover = new FileMover(main);
        mover.move(newPath);
        main = mover.getFile();
    }

    public void moveUp() {
        moveFile(getParentDirectory().getPath());
    }

    public String toString() {
        return getName() + getExtension().toLowerCase();
    }

    public void debugPrint() {
        String parDir = "";
        if (this.getParentDirectory() != null) {
            parDir = "Parent Path: " + this.getParentDirectory().getPath() + "\n";
        }

        System.out.println("Path: " + this.getPath() + "\n"
                + "Name: " + this.getName() + "\n"
                + "Extension: " + this.getExtension() + "\n"
                + "Parent: " + this.getParent() + "\n"
                + parDir
                + "Directory: " + this.isDirectory()
        );
    }
}
