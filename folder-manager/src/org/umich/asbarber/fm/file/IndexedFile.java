package org.umich.asbarber.fm.file;

import org.umich.asbarber.fm.file.format.ComponentSystem;
import org.umich.asbarber.fm.file.format.NameFormatter;
import java.io.File;
import java.util.List;

/**
 *
 * @author Aaron
 */
public class IndexedFile extends PartsFile {

    public IndexedFile(File f) {
        super(f);
    }

    public IndexedFile(String path) {
        this(new File(path));
    }

    public void numerate(int number) {
        super.renameFile(super.getName(), number);
    }

    public void denumerate() {
        NameFormatter format = new NameFormatter(super.getName());
        format.trimNumber();
        super.renameFile(format.getName());
    }

    public void explode(String delim) {
        //Explodes name into pieces
        ComponentSystem comps = new ComponentSystem(getName(), delim);
        comps.explode();
        List<String> pieces = comps.getPieces();
        String pathSUM = "";

        //Traverses pieces: ignores first piece and file name
        for (int i = 0; i < pieces.size() - 1; i++) {
            //Adds to path
            pathSUM += pieces.get(i);

            //Creates File and Then Folder
            File subfolder = new File(getPath() + pathSUM);
            if (!subfolder.exists()) {
                subfolder.mkdir();
            }

            //Prepares path for next element
            pathSUM += "\\";
        }

        super.moveFile(super.getPath() + pathSUM);
    }
}
