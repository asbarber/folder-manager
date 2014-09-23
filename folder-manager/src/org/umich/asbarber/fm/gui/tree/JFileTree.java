package org.umich.asbarber.fm.gui.tree;

import org.umich.asbarber.fm.gui.fileviewer.SelectionPanel;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.umich.asbarber.fm.file.Directory;
import org.umich.asbarber.fm.file.IndexedFile;
import org.umich.asbarber.fm.file.PartsFile;
import org.umich.asbarber.fm.file.filters.AndFilter;
import org.umich.asbarber.fm.file.filters.DirectoryFilter;
import org.umich.asbarber.fm.file.filters.NotFilter;

/**
 *
 * @author Aaron
 */
public class JFileTree extends JTree {

    private final SelectionPanel selectionPanel;

    private FileNode root;
    private boolean showExtensions, hideFolders, hideFiles;

    public JFileTree(FileNode root, SelectionPanel selectionPanel) {
        super(root);

        //Tree Preferences
        super.setCellRenderer(new FileTreeRenderer());

        //Default FileTree Specifics
        this.root = root;
        this.showExtensions = false;
        this.hideFolders = false;
        this.hideFiles = false;

        //Initial Tree Conditions
        this.refresh();
        super.expandRow(0);

        //Selection
        super.selectionModel.setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        this.selectionPanel = selectionPanel;
        super.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreePath[] treePaths = JFileTree.this.getSelectionModel().getSelectionPaths();
                ArrayList<IndexedFile> files = new ArrayList<IndexedFile>();

                for (TreePath treePath : treePaths) {
                    if (treePath != null && treePath.getPath().length != 0) {
                        FileNode node = (FileNode) treePath.getPath()[treePath.getPath().length - 1];

                        if (node.getUserObject() instanceof IndexedFile) {
                            files.add((IndexedFile) node.getUserObject());
                        }
                    }
                }
                JFileTree.this.selectionPanel.setFiles(files);
            }

        });
    }

    public File getRootFile() {
        return ((PartsFile) root.getUserObject()).getFile();
    }

    public void setRoot(FileNode newRoot) {
        root = newRoot;
        ((DefaultTreeModel) super.treeModel).setRoot(root);
        refresh();
    }

    public void setRoot() {
        FileNode node = (FileNode) super.getLastSelectedPathComponent();

        if (node != null && node.isDirectory()) {
            setRoot(node);
        }
    }

    public void setParentAsRoot() {
        Directory dir = ((PartsFile) root.getUserObject()).getParentDirectory();
        if (dir != null) {
            setRoot(new FileNode(dir));
        }
    }

    public void refresh() {
        root.reload();
        ((DefaultTreeModel) super.treeModel).nodeStructureChanged(root);
        super.repaint();
    }

    public void selectAll() {
        super.setSelectionInterval(0, super.getRowCount());
    }

    public void deselectAll() {
        super.clearSelection();
    }

    public void selectAllFiles() {
        List<Integer> tmp = new ArrayList<Integer>();

        Enumeration<FileNode> enume = root.preorderEnumeration();
        for (int i = 0; enume.hasMoreElements(); i++) {
            if (!enume.nextElement().isDirectory()) {
                tmp.add(i);
            }
        }

        //Convert to int
        int[] rows = new int[tmp.size()];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = tmp.get(i);
        }

        //Select
        super.addSelectionRows(rows);
    }

    public void hideFiles() {
        this.hideFiles = true;
        updateFilterRoot();
    }

    public void showFiles() {
        this.hideFiles = false;
        updateFilterRoot();
    }

    public void hideFolders() {
        this.hideFolders = true;
        updateFilterRoot();
    }

    public void showFolders() {
        this.hideFolders = false;
        updateFilterRoot();
    }

    private FileFilter makeFilter() {
        if (hideFolders && hideFiles) {
            return new AndFilter(new NotFilter(new DirectoryFilter()), new DirectoryFilter());
        } else if (hideFolders) {
            return new NotFilter(new DirectoryFilter());
        } else if (hideFiles) {
            return new DirectoryFilter();
        } else {
            return null;
        }
    }

    private void updateFilterRoot() {
        filter(null);
    }

    public void filter(FileFilter customFilter) {
        FileFilter filter = makeFilter();

        //Adds custom filter if exists
        if (filter != null && customFilter != null) {
            filter = new AndFilter(filter, customFilter);
        } else if (filter == null && customFilter != null) {
            filter = customFilter;
        }

        //Updates root
        if (filter == null) {
            FileNode node;

            if (root.isDirectory()) {
                node = new FileNode((Directory) root.getUserObject());
            } else {
                node = new FileNode((IndexedFile) root.getUserObject());
            }

            setRoot(node);
        } else {
            FilterNode filterRoot;

            if (root.isDirectory()) {
                filterRoot = new FilterNode((Directory) root.getUserObject(), filter);
            } else {
                filterRoot = new FilterNode((IndexedFile) root.getUserObject(), filter);
            }

            setRoot(filterRoot);
        }
    }

    public void hideExtensions() {
        this.showExtensions = false;
        refresh();
    }

    public void showExtensions() {
        this.showExtensions = true;
        refresh();
    }

    public void expandTree() {
        for (int i = 0; i < super.getRowCount(); i++) {
            super.expandRow(i);
        }
    }

    public void collapseTree() {
        for (int i = 0; i < super.getRowCount(); i++) {
            super.collapseRow(i);
        }
    }

    @Override
    public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (showExtensions) {
            return ((FileNode) value).showExtension();
        } else {
            return ((FileNode) value).hideExtension();
        }
    }

    public static JPanel makePanel(JFileTree tree) {
        JLabel title;
        title = new JLabel("Tree Viewer");
        title.setFont(title.getFont().deriveFont((float) (title.getFont().getSize() * 1.5)));

        JPanel tmp = new JPanel();

        tmp.setLayout(new BorderLayout());
        tmp.add(title, BorderLayout.NORTH);
        tmp.add(new JScrollPane(tree), BorderLayout.CENTER);

        return tmp;
    }
}
