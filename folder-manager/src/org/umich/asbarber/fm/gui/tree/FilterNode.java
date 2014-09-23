package org.umich.asbarber.fm.gui.tree;

import java.io.FileFilter;
import org.umich.asbarber.fm.file.Directory;
import org.umich.asbarber.fm.file.IndexedFile;

/**
 *
 * @author Aaron
 */
public class FilterNode extends FileNode {

    private FileFilter filter;

    public FilterNode(String path, FileFilter filter) {
        super(path);
        this.filter = filter;
    }

    public FilterNode(Directory dir, FileFilter filter) {
        super(dir);
        this.filter = filter;
    }

    public FilterNode(IndexedFile file, FileFilter filter) {
        super(file);
        this.filter = filter;
    }

    @Override
    public void addChildren() {
        if (super.isDirectory()) {
            for (Directory subdir : ((Directory) super.userObject).getSubdirectories()) {
                if (filter.accept(subdir.getFile())) {
                    super.add(new FilterNode(subdir, filter));
                }
            }
            for (IndexedFile file : ((Directory) super.userObject).getFiles()) {
                if (filter.accept(file.getFile())) {
                    super.add(new FilterNode(file, filter));
                }
            }
        }
    }
}
