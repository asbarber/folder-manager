package org.umich.asbarber.fm.file.manip;

import org.umich.asbarber.fm.file.manip.FileRenamer;
import java.io.File;
import org.umich.asbarber.fm.file.PartsFile;
import org.umich.asbarber.fm.file.Directory;

/**
 *
 * @author Aaron
 */
public class FileMover {

    private PartsFile main;

    public FileMover(File original) {
        main = new PartsFile(original);
    }

    public void move(String newPath) {
        FileRenamer renamer = new FileRenamer(main.getFile());
        renamer.rename(newPath, main.getName());
        main.setFile(renamer.getFile());
    }

    public void move(Directory newParent) {
        move(newParent.getPath());
    }

    public File getFile() {
        return main.getFile();
    }
}
