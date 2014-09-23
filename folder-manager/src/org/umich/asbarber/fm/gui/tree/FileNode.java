package org.umich.asbarber.fm.gui.tree;

import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import org.umich.asbarber.fm.file.Directory;
import org.umich.asbarber.fm.file.IndexedFile;
import org.umich.asbarber.fm.file.PartsFile;

/**
 *
 * @author Aaron
 */
public class FileNode extends DefaultMutableTreeNode {

    private boolean isDirectory;

    public FileNode(String path) {
        super();

        if (Directory.isDirectory(path)) {
            isDirectory = true;
            super.setUserObject(new Directory(path));
        } else {
            isDirectory = false;
            super.setUserObject(new IndexedFile(path));
        }
    }

    public FileNode(Directory dir) {
        super(dir);
        isDirectory = true;
    }

    public FileNode(IndexedFile file) {
        super(file);
        isDirectory = false;
    }

    public void reload() {
        super.removeAllChildren();
        this.addChildren();

        for (int i = 0; i < super.getChildCount(); i++) {
            ((FileNode) super.getChildAt(i)).reload();
        }
    }

    public void addChildren() {
        if (isDirectory) {
            for (Directory subdir : ((Directory) super.getUserObject()).getSubdirectories()) {
                super.add(new FileNode(subdir));
            }
            for (IndexedFile subdir : ((Directory) super.getUserObject()).getFiles()) {
                super.add(new FileNode(subdir));
            }
        }
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public String showExtension() {
        PartsFile file = (PartsFile) this.getUserObject();
        return file.getName() + file.getExtension().toLowerCase();
    }

    public String hideExtension() {
        return ((PartsFile) this.getUserObject()).getName();
    }

    public void display() {
        Enumeration e = super.preorderEnumeration();

        while (e.hasMoreElements()) {
            System.out.println(e.nextElement());
        }

        System.out.println(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FileNode) {
            FileNode node = (FileNode) o;
            PartsFile otherFile = (PartsFile) node.getUserObject();

            return otherFile.equals((PartsFile) this.getUserObject());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.isDirectory ? 1 : 0);
        return hash;
    }
}
