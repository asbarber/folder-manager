package org.umich.asbarber.fm.gui.manip;

import org.umich.asbarber.fm.gui.tree.JFileTree;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.umich.asbarber.fm.file.Directory;
import org.umich.asbarber.fm.file.manip.FileManipulator;

/**
 *
 * @author Aaron
 */
public class ManipulatorPanel extends JPanel {

    private JFileTree tree;

    private JCheckBox subfolders;
    private JLabel iDelimiter;
    private JTextField delimiter;

    private JButton enumerate, denumerate,
            implode, explode,
            enumerate_tag, explode_tag;

    private JLabel title;

    public ManipulatorPanel() {
        initPanel();
        initComponents();
        addComponents();
    }

    private void initPanel() {
        setLayout(new BorderLayout());
    }

    private void initComponents() {
        title = new JLabel("Root Operations");
        title.setFont(title.getFont().deriveFont((float) (title.getFont().getSize() * 1.5)));

        //Properties (North)
        subfolders = new JCheckBox("Affect Subfolders");
        subfolders.setSelected(true);

        iDelimiter = new JLabel("Specify Delimiter");
        delimiter = new JTextField("~");
        delimiter.setColumns(3);

        //Manipulation Buttons (Center)
        enumerate = new JButton("Enumerate");
        denumerate = new JButton("Denumerate");

        implode = new JButton("Implode");
        explode = new JButton("Explode");

        enumerate_tag = new JButton("Enumerate Tag");
        explode_tag = new JButton("Explode Tag");

        ActionListener listener = new Listener();
        enumerate.addActionListener(listener);
        denumerate.addActionListener(listener);
        implode.addActionListener(listener);
        explode.addActionListener(listener);
        enumerate_tag.addActionListener(listener);
        explode_tag.addActionListener(listener);

    }

    private void addComponents() {
        //North
        JPanel north = new JPanel();
        north.setLayout(new GridLayout(0, 1));

        JPanel northTop = new JPanel();
        northTop.add(subfolders);

        JPanel northBottom = new JPanel();
        northBottom.add(iDelimiter);
        northBottom.add(delimiter);

        north.add(northTop);
        north.add(northBottom);

        //Center
        JPanel center = new JPanel();
        center.setLayout(new GridLayout(2, 3));

        center.add(enumerate);
        center.add(implode);
        center.add(enumerate_tag);

        center.add(denumerate);
        center.add(explode);
        center.add(explode_tag);

        JPanel tmp = new JPanel();
        tmp.setLayout(new BorderLayout());
        tmp.add(north, BorderLayout.NORTH);
        tmp.add(center, BorderLayout.CENTER);

        this.add(title, BorderLayout.NORTH);
        this.add(tmp, BorderLayout.CENTER);
    }

    public String validateDelimiter(String orig) {
        //Invalid
        if (orig == null || orig.trim().equals("")) {
            String msg = "Invalid Delimiter: The default delimiter '~' will be used. Proceed with the action?";
            int i = JOptionPane.showConfirmDialog(null, msg, "Delimiter Warning", JOptionPane.YES_NO_OPTION);

            //Proceed
            if (i == JOptionPane.YES_OPTION) {
                return "~";
            } //Cancel
            else {
                return null;
            }
        } //Valid
        else {
            return orig.trim();
        }
    }

    public void setTree(JFileTree treePane) {
        this.tree = treePane;
    }

    private class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (tree == null) {
                return;
            }
            Directory root = new Directory(tree.getRootFile());

            //Validates
            String delim = validateDelimiter(delimiter.getText());
            if (delim == null) {
                return;
            }
            FileManipulator manip = new FileManipulator(root, delim);

            if (ae.getActionCommand().equals("Enumerate")) {
                manip.enumerate();
            } else if (ae.getActionCommand().equals("Denumerate")) {
                manip.denumerate();
            } else if (ae.getActionCommand().equals("Implode")) {
                manip.implode();
            } else if (ae.getActionCommand().equals("Explode")) {
                manip.explode();
            } else if (ae.getActionCommand().equals("Enumerate Tag")) {
                manip.enumerateTag();
            } else if (ae.getActionCommand().equals("Explode Tag")) {
                manip.explodeTag();
            }

            tree.refresh();
        }
    }
}
