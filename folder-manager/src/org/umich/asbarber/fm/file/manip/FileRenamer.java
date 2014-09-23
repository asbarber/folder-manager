package org.umich.asbarber.fm.file.manip;

import java.io.File;
import org.umich.asbarber.fm.file.PartsFile;

/**
 *
 * @author Aaron
 */
public class FileRenamer {

    private PartsFile main;

    public FileRenamer(File original) {
        main = new PartsFile(original);
    }

    public void renameFull(String fullPath) {
        File renamedFile = new File(fullPath);

        //Override Prevention
        if (!canRename(renamedFile, main.getFile())) {
            renamedFile = fixRename(renamedFile, main.getFile());
        }

        main.getFile().renameTo(renamedFile);
        main.setFile(renamedFile);
    }

    public void rename(String path, String name, String ext) {
        renameFull(path + name + ext);
    }

    public void rename(String path, String name) {
        rename(path, name, main.getExtension());
    }

    public void rename(String name) {
        rename(main.getPath(), name, main.getExtension());
    }

    public File getFile() {
        return main.getFile();
    }

    public static boolean canRename(File newFile, File original) {
        PartsFile partsNew = new PartsFile(newFile);
        PartsFile partsOrig = new PartsFile(original);

        //New File Does Not Exist or New File is Equal To Original File
        return !newFile.exists() || partsNew.getName().equals(partsOrig.getName());
    }

    public static File fixRename(File newFile, File original) {
        PartsFile partsNew = new PartsFile(newFile);
        PartsFile partsOrig = new PartsFile(original);

        String extension = partsOrig.getExtension();
        String newName = partsNew.getPath() + partsNew.getName();

        int j = 0;
        do {
            j++;
            String unduplicatedName = newName + " (" + j + ")" + extension;
            newFile = new File(unduplicatedName);
        } while (!canRename(newFile, original));

        return newFile;
    }
}
