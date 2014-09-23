package org.umich.asbarber.fm.file.filters;

import java.io.FileFilter;
import java.util.List;
import org.umich.asbarber.fm.file.PartsFile;
import java.io.File;

/**
 *
 * @author Aaron
 */
public class CustomFilter implements FileFilter {

    public final static String FILE_EXT_DELIM = "|",
            FILE_NAME_DELIM = "||";

    private List<String> extensions;
    private String name;

    public CustomFilter(List<String> extensions) {
        this("New Filter", extensions);
    }

    public CustomFilter(String name, List<String> extensions) {
        this.name = name;
        this.extensions = extensions;

        for (int i = 0; i < extensions.size(); i++) {
            extensions.set(i, extensions.get(i).trim().toLowerCase());
        }
    }

    @Override
    public boolean accept(File file) {
        PartsFile partsFile = new PartsFile(file);

        return extensions.contains(partsFile.getExtension().trim().toLowerCase());
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public String toString() {
        String toString = getName() + FILE_NAME_DELIM;

        for (String s : extensions) {
            toString += s + FILE_EXT_DELIM;
        }

        return toString;
    }
}
