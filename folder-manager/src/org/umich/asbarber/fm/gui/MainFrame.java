package org.umich.asbarber.fm.gui;

import org.umich.asbarber.fm.gui.fileviewer.SelectionPanel;
import org.umich.asbarber.fm.gui.fileviewer.ViewPanel;
import org.umich.asbarber.fm.gui.filters.FilterControlPanel;
import org.umich.asbarber.fm.gui.manip.DescriptionPanel;
import org.umich.asbarber.fm.gui.manip.ManipulatorPanel;
import org.umich.asbarber.fm.gui.tree.FileNode;
import org.umich.asbarber.fm.gui.tree.JFileTree;
import org.umich.asbarber.fm.gui.tree.TreeControlPanel;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 *
 * @author Aaron
 */
public class MainFrame extends JFrame {

    //Middle

    private JFileTree treePane;
    private TreeControlPanel treeControl;

    //Left
    private ManipulatorPanel manipulator;
    private DescriptionPanel description;

    //Right
    private FilterControlPanel filter;
    private SelectionPanel selector;
    private ViewPanel viewer;

    public MainFrame() {
        super("Folder Manager");

        initComponents();
        addComponents();
        finalizeFrame();
    }

    private void initComponents() {
        //Right
        filter = new FilterControlPanel();
        selector = new SelectionPanel();
        viewer = new ViewPanel();

        //Middle
        FileNode node = new FileNode("Sample");
        treePane = new JFileTree(node, selector);
        treeControl = new TreeControlPanel(treePane);

        //Left
        manipulator = new ManipulatorPanel();
        description = new DescriptionPanel();

        manipulator.setTree(treePane);
        filter.setTree(treePane);
        selector.setTree(treePane);
        selector.setViewer(viewer);
    }

    private void addComponents() {
        JPanel main = new JPanel();
        main.setLayout(new GridLayout(0, 3));

        //Bottom Splits
        JSplitPane sRightBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, selector, viewer);

        //Splits
        JSplitPane sLeft = new JSplitPane(JSplitPane.VERTICAL_SPLIT, manipulator, description);
        sLeft.setEnabled(false);
        JSplitPane sMiddle = new JSplitPane(JSplitPane.VERTICAL_SPLIT, treeControl, JFileTree.makePanel(treePane));
        sMiddle.setEnabled(false);
        JSplitPane sRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT, filter, sRightBottom);

        main.add(sLeft);
        main.add(sMiddle);
        main.add(sRight);
        super.add(main);
    }

    private void finalizeFrame() {
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public static void main(String[] args) {
        MainFrame main = new MainFrame();
    }
}
