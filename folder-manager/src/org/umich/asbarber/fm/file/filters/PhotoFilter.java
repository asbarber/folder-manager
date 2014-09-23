package org.umich.asbarber.fm.file.filters;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Aaron
 */
public class PhotoFilter implements FileFilter {

    private static final String[] extensions = {".gif", ".tiff", ".png", ".jpg", ".jpeg"};

    @Override
    public boolean accept(File file) {
        for (String ext : extensions) {
            if (file.getName().toLowerCase().endsWith(ext)) {
                return true;
            }
        }

        return false;
    }

}
