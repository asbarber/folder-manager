package org.umich.asbarber.fm.file.filters;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Aaron
 */
public class AndFilter implements FileFilter {

    private FileFilter first, second;

    public AndFilter(FileFilter one, FileFilter two) {
        first = one;
        second = two;
    }

    @Override
    public boolean accept(File file) {
        return first.accept(file) && second.accept(file);
    }

}
