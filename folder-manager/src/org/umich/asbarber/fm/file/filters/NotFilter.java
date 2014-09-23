package org.umich.asbarber.fm.file.filters;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Aaron
 */
public class NotFilter implements FileFilter {

    private FileFilter filter;

    public NotFilter(FileFilter filter) {
        this.filter = filter;
    }

    @Override
    public boolean accept(File file) {
        return !filter.accept(file);
    }

}
