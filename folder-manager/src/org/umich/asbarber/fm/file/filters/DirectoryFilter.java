package org.umich.asbarber.fm.file.filters;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Aaron
 */
public class DirectoryFilter implements FileFilter {

    @Override
    public boolean accept(File file) {
        return file.isDirectory();
    }

    public String getDescription() {
        return "Directory";
    }
}
