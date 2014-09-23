package org.umich.asbarber.fm.file;

import org.umich.asbarber.fm.file.filters.DirectoryFilter;
import org.umich.asbarber.fm.file.filters.NotFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Aaron
 */
public class Directory extends PartsFile {

    public Directory(File f) {
        super(init(f));
    }

    public Directory(String path) {
        this(new File(path));
    }

    private static File init(File f) {
        if (f.isDirectory()) {
            return f;
        } else {
            return null;
        }
    }

    public static boolean isDirectory(String filepath) {
        return new File(filepath).isDirectory();
    }

    public List<File> getAllFiles() {
        return Arrays.asList(getFile().listFiles());
    }

    public List<Directory> getSubdirectories() {
        ArrayList<Directory> list = new ArrayList<>();
        List<File> files = Arrays.asList(getFile().listFiles(new DirectoryFilter()));

        files.stream().forEach((f) -> {
            list.add(new Directory(f));
        });

        return list;
    }

    public List<IndexedFile> getFiles() {
        ArrayList<IndexedFile> list = new ArrayList<>();
        List<File> files = Arrays.asList(getFile().listFiles(new NotFilter(new DirectoryFilter())));

        files.stream().forEach((f) -> {
            list.add(new IndexedFile(f));
        });

        return list;
    }

    public void removeAllSubdirectories() {
        getSubdirectories().stream().forEach((subdir) -> {
            subdir.getFile().delete();
        });
    }
}
