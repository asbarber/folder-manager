package org.umich.asbarber.fm.gui.fileviewer;

import org.umich.asbarber.fm.gui.tree.JFileTree;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import org.umich.asbarber.fm.file.IndexedFile;

/**
 *
 * @author Aaron
 */
public class SelectionPanel extends JPanel {

    private ViewPanel viewer;
    private JFileTree treePane;

    private JList<IndexedFile> list;
    private DefaultListModel<IndexedFile> model;

    private JButton rename, open;
    private JLabel title;

    public SelectionPanel() {
        super();
        this.setLayout(new BorderLayout());

        initComponents();
        addComponents();
    }

    private void initComponents() {
        //Init Components
        model = new DefaultListModel<IndexedFile>();
        list = new JList(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        title = new JLabel("File Operations");
        title.setFont(title.getFont().deriveFont((float) (title.getFont().getSize() * 1.5)));

        rename = new JButton("Rename");
        open = new JButton("Open");

        rename.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                IndexedFile file = list.getSelectedValue();
                if (file != null) {
                    renameAction(file);
                } else if (model.getSize() == 1) {
                    openAction(model.get(0));
                }
            }

        });
        open.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                IndexedFile file = list.getSelectedValue();
                if (file != null) {
                    openAction(file);
                } else if (model.getSize() == 1) {
                    openAction(model.get(0));
                }
            }

        });
    }

    private void addComponents() {
        JPanel east = new JPanel();
        east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
        east.add(rename);
        east.add(open);

        Component center = new JScrollPane(list);

        this.add(title, BorderLayout.NORTH);
        this.add(center, BorderLayout.CENTER);
        this.add(east, BorderLayout.EAST);
    }

    public void setFiles(ArrayList<IndexedFile> files) {
        model.clear();

        for (IndexedFile file : files) {
            model.addElement(file);
        }
    }

    private void renameAction(IndexedFile file) {
        String input = JOptionPane.showInputDialog(null, "New Name:");

        if (input != null) {
            file.renameFile(input);
        }

        if (treePane != null) {
            treePane.deselectAll();
            treePane.refresh();
        }
    }

    private void openAction(IndexedFile file) {
        if (viewer != null) {
            viewer.open(file);
        }
    }

    public void setViewer(ViewPanel viewer) {
        this.viewer = viewer;
    }

    public void setTree(JFileTree treePane) {
        this.treePane = treePane;
    }
}
