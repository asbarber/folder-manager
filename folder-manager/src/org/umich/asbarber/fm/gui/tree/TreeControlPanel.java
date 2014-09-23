package org.umich.asbarber.fm.gui.tree;

import org.umich.asbarber.fm.gui.ToggleButton;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import org.umich.asbarber.fm.file.Directory;

/**
 *
 * @author Aaron
 */
public class TreeControlPanel extends JPanel {

    private JFileTree tree;

    private JButton browseRoot, setRoot, navigateUp,
            refresh,
            selectAll, deselectAll,
            selectFiles;
    private ToggleButton toggleExtensions, toggleFolders, toggleFiles;
    private JButton expandTree, collapseTree;

    private JLabel title;

    public TreeControlPanel(JFileTree tree) {
        super();
        this.tree = tree;

        initPanel();
        initComponents();
        addComponents();
    }

    private void initPanel() {
        this.setLayout(new BorderLayout());
    }

    private void initComponents() {
        //Components
        title = new JLabel("Tree Operations");
        title.setFont(title.getFont().deriveFont((float) (title.getFont().getSize() * 1.5)));

        browseRoot = new JButton("Browse For Root");
        setRoot = new JButton("Set Root");
        navigateUp = new JButton("Navigate Up");

        refresh = new JButton("Refresh");
        selectAll = new JButton("Select All");
        deselectAll = new JButton("Deselect All");

        selectFiles = new JButton("Select Files");

        toggleExtensions = new ToggleButton("Show Extensions", "Hide Extensions", true);
        toggleFolders = new ToggleButton("Show Folders", "Hide Folders");
        toggleFiles = new ToggleButton("Show Files", "Hide Files");

        expandTree = new JButton("Expand Tree");
        collapseTree = new JButton("Collapse Tree");

        //Action Listener
        ButtonListener listener = new ButtonListener();
        browseRoot.addActionListener(listener);
        navigateUp.addActionListener(listener);
        refresh.addActionListener(listener);
        setRoot.addActionListener(listener);
        selectAll.addActionListener(listener);
        deselectAll.addActionListener(listener);
        selectFiles.addActionListener(listener);
        toggleExtensions.addActionListener(listener);
        toggleFolders.addActionListener(listener);
        toggleFiles.addActionListener(listener);
        expandTree.addActionListener(listener);
        collapseTree.addActionListener(listener);
    }

    private void addComponents() {
        JPanel grid = new JPanel();
        grid.setLayout(new GridLayout(0, 3));

        grid.add(browseRoot);
        grid.add(setRoot);
        grid.add(navigateUp);

        grid.add(blankCell());
        grid.add(refresh);
        grid.add(blankCell());

        grid.add(selectAll);
        grid.add(deselectAll);
        grid.add(selectFiles);

        grid.add(toggleExtensions);
        grid.add(toggleFolders);
        grid.add(toggleFiles);

        grid.add(expandTree);
        grid.add(blankCell());
        grid.add(collapseTree);

        this.add(title, BorderLayout.NORTH);
        this.add(grid, BorderLayout.CENTER);
    }

    private JSeparator blankCell() {
        return new JSeparator();
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getActionCommand().equals("Refresh")) {
                tree.refresh();
            } else if (ae.getActionCommand().equals("Set Root")) {
                tree.setRoot();
            } else if (ae.getActionCommand().equals("Browse For Root")) {
                File file = RootBrowser.browseForRoot(tree.getRootFile());

                if (file != null) {
                    tree.setRoot(new FileNode(new Directory(file)));
                }
            } else if (ae.getActionCommand().equals("Navigate Up")) {
                tree.setParentAsRoot();
            } else if (ae.getActionCommand().equals("Select All")) {
                tree.selectAll();
            } else if (ae.getActionCommand().equals("Deselect All")) {
                tree.deselectAll();
            } else if (ae.getActionCommand().equals("Select Files")) {
                tree.expandTree();
                tree.selectAllFiles();
            } else if (ae.getActionCommand().equals("Show Extensions")) {
                tree.showExtensions();
            } else if (ae.getActionCommand().equals("Hide Extensions")) {
                tree.hideExtensions();
            } else if (ae.getActionCommand().equals("Show Folders")) {
                tree.showFolders();
            } else if (ae.getActionCommand().equals("Hide Folders")) {
                tree.hideFolders();
            } else if (ae.getActionCommand().equals("Show Files")) {
                tree.showFiles();
            } else if (ae.getActionCommand().equals("Hide Files")) {
                tree.hideFiles();
            } else if (ae.getActionCommand().equals("Expand Tree")) {
                tree.expandTree();
            } else if (ae.getActionCommand().equals("Collapse Tree")) {
                tree.collapseTree();
            }
        }
    }
}
