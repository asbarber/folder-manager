package org.umich.asbarber.fm.gui.manip;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Aaron
 */
public class DescriptionPanel extends JPanel {

    private JComboBox item;
    private JTextArea description;
    private JLabel title;

    public DescriptionPanel() {
        initPanel();
        initComponents();
        addComponents();
    }

    private void initPanel() {
        setLayout(new BorderLayout());
    }

    private void initComponents() {
        title = new JLabel("Helper");
        title.setFont(title.getFont().deriveFont((float) (title.getFont().getSize() * 1.5)));

        item = new JComboBox();
        item.addItem("General");
        initList();
        item.addItemListener(new Selector());

        description = new JTextArea();
        description.setEditable(false);
        description.setText(GENERAL);
    }

    private void addComponents() {
        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        center.add(item, BorderLayout.NORTH);
        center.add(new JScrollPane(description), BorderLayout.CENTER);

        this.add(title, BorderLayout.NORTH);
        this.add(center, BorderLayout.CENTER);
    }

    private void initList() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(new ListItem("GENERAL", GENERAL));
        model.addElement(new ListItem("TAG", TAG));
        model.addElement(new ListItem("ENUMERATE", ENUMERATE));
        model.addElement(new ListItem("DENUMERATE", DENUMERATE));
        model.addElement(new ListItem("IMPLODE", IMPLODE));
        model.addElement(new ListItem("EXPLODE", EXPLODE));
        model.addElement(new ListItem("ENUMERATE TAG", ENUMERATE_TAG));
        model.addElement(new ListItem("EXPLODE TAG", EXPLODE_TAG));

        item.setModel(model);
    }

    private class ListItem {

        protected String title, description;
        protected int index;

        public ListItem(String title, String description) {
            this.title = title;
            this.description = description;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    private class Selector implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent ie) {
            description.setText(((ListItem) ie.getItem()).description);
        }
    }

    private static final String line = "\n";
    private static final String GENERAL = "The description of every action is listed here.",
            TAG = "Inserting a parent folder name to a file." + line
            + "Tags are separated by a delimiter such as '~' or '_'." + line
            + "Example: a file 'Bird.jpg' in 'Photos\\' gets tag renamed to 'Photos_Bird.jpg'",
            ENUMERATE = "Description: Each file in the root directory and all subdirectories is renamed to consecutive numbers." + line
            + "Example: a file 'Bird.jpg' becomes '(1).jpg'" + line
            + "See Action: Denumerate" + line
            + "Reverse Action: NONE",
            DENUMERATE = "Description: Removes numbers from the end of every file in the root directory and all subdirectories." + line
            + "Example: a file 'Bird (1).jpg' becomes 'Bird.jpg'" + line
            + "See Action: Enumerate" + line
            + "Reverse Action: NONE",
            IMPLODE = "Description: Moves every subfile to 'Root\\' with the full tag path." + line
            + "Example: 'Bird.jpg' in 'Root\\Photos\\Bird.jpg' moves to 'Root\\' with new name 'Root_Photos_Bird.jpg'" + line
            + "Reverse Action: Explode",
            EXPLODE = "Description: Moves files to directory based on the tag." + line
            + "Example: 'Root_Photos_Bird.jpg' in 'Root\\' moves to 'Root\\Photos\\' with name' Bird.jpg'" + line
            + "Reverse Action: Implode",
            ENUMERATE_TAG = "Description: Tags files in a subfolder and then enunmerates." + line
            + "Example: 'Bird.jpg' in Root\\Photos\\Bird.jpg' is renamed to 'Photos_(1).jpg'" + line
            + "See Action: Enumerate" + line
            + "Reverse Action: NONE",
            EXPLODE_TAG = "Description: Explodes subfiles and then tags all subfiles." + line
            + "Example: 'Photos_Bird.jpg' in 'Root\\' becomes 'Photos_Bird.jpg' in 'Root\\Photos\\'" + line
            + "See Action: Explode" + line
            + "Reverse Action: NONE";
}
