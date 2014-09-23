package org.umich.asbarber.fm.gui.filters;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.umich.asbarber.fm.file.Directory;
import org.umich.asbarber.fm.file.IndexedFile;
import org.umich.asbarber.fm.file.filters.CustomFilter;

/**
 *
 * @author Aaron
 */
public class FilterOperations {

    public final static String filterDir = "resources\\filters";
    private final static String filterPath = filterDir + "\\";
    public final static String filterExt = ".flr";

    public static List<CustomFilter> getSavedFilters() {
        List<CustomFilter> stored = new ArrayList<CustomFilter>();

        Directory mainDir = new Directory(filterDir);

        for (IndexedFile file : mainDir.getFiles()) {
            stored.add(readFilter(file.getFile()));
        }

        return stored;
    }

    public static void viewFilter(CustomFilter filter) {
        JPanel tmp = new JPanel();
        tmp.setLayout(new BorderLayout());

        JLabel name = new JLabel("Filter Name:    " + filter.getName());
        JList list = new JList(filter.getExtensions().toArray());

        tmp.add(name, BorderLayout.NORTH);
        tmp.add(list, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(null, tmp, filter.getName(), JOptionPane.INFORMATION_MESSAGE);
    }

    public static CustomFilter readFilter(File filterFile) {
        try {
            //Streams
            FileReader r = new FileReader(filterFile);
            BufferedReader reader = new BufferedReader(r);

            //Reads
            String text = (reader.readLine());

            String name;
            List<String> extensions = new ArrayList<String>();

            //Name Component
            int iName = text.indexOf(CustomFilter.FILE_NAME_DELIM);
            if (iName == -1) {
                return null;
            }
            name = text.substring(0, iName);
            text = text.substring(iName + CustomFilter.FILE_NAME_DELIM.length());

            //Extensions Component
            StringTokenizer conch = new StringTokenizer(text, CustomFilter.FILE_EXT_DELIM);
            while (conch.hasMoreElements()) {
                extensions.add((String) conch.nextElement());
            }

            //Closes
            r.close();

            //Returns
            return new CustomFilter(name, extensions);
            //Catchs File Error
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "There was a file operation error in reading "
                    + filterFile.getName(), "File Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static void deleteFilter(CustomFilter filter) {
        File toDelete = new File(filterPath + filter.getName() + filterExt);
        toDelete.delete();
    }

    public static CustomFilter createFilter() {
        CreatorPanel panel = new CreatorPanel();
        int i = JOptionPane.showConfirmDialog(null, panel, "Filter Builder", JOptionPane.DEFAULT_OPTION);

        if (i == JOptionPane.OK_OPTION) {
            CustomFilter newFilter = new CustomFilter(panel.getFilterName(), panel.getObjects());
            saveFilter(newFilter);
            return newFilter;
        }

        return null;
    }

    private static class CreatorPanel extends JPanel {

        private JLabel lblName;
        private JTextField name;
        private JButton instructions, add, remove;
        private JTextField input;
        private JComboBox listDisplay;

        public CreatorPanel() {
            super();
            initPanel();
            initComponents();
            addComponents();
        }

        private void initPanel() {
            super.setLayout(new BorderLayout());
        }

        private void initComponents() {
            lblName = new JLabel("Filter Name: ");
            name = new JTextField(8);

            instructions = new JButton("Instructions");
            instructions.setBackground(Color.lightGray);

            add = new JButton("Add");
            input = new JTextField(4);
            remove = new JButton("Remove");

            listDisplay = new JComboBox();
            listDisplay.setSize(listDisplay.getWidth() + 50, listDisplay.getHeight());
            listDisplay.setEditable(false);

            ActionListener listener = new ButtonListener();
            instructions.addActionListener(listener);
            add.addActionListener(listener);
            remove.addActionListener(listener);
        }

        private void addComponents() {
            JPanel tmp = new JPanel();
            tmp.add(instructions);
            tmp.add(lblName);
            tmp.add(name);
            this.add(tmp, BorderLayout.NORTH);
            this.add(add, BorderLayout.WEST);
            this.add(input, BorderLayout.CENTER);
            this.add(remove, BorderLayout.SOUTH);
            this.add(listDisplay, BorderLayout.EAST);
        }

        private List<String> getObjects() {
            List<String> objects = new ArrayList<String>();

            for (int i = 0; i < listDisplay.getItemCount(); i++) {
                objects.add((String) listDisplay.getItemAt(i));
            }

            return objects;
        }

        private String getFilterName() {
            return name.getText();
        }

        private class ButtonListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (ae.getActionCommand().equals("Add")) {
                    addAction();
                } else if (ae.getActionCommand().equals("Remove")) {
                    removeAction();
                } else if (ae.getActionCommand().equals("Instructions")) {
                    showInstructions();
                }
            }

            private void showInstructions() {
                String msg = "Input and 'Add' the extensions of this filter.\nOr 'Remove' the currently selected item.";
                JOptionPane.showMessageDialog(null, msg, "Instructions", JOptionPane.DEFAULT_OPTION);
            }

            private void addAction() {
                String i = input.getText();

                if (isExtension(i)) {
                    listDisplay.addItem(i);
                    listDisplay.setSelectedIndex(listDisplay.getItemCount() - 1);
                } else {
                    JOptionPane.showMessageDialog(null, "Please Enter a Unique and Valid Extension!", "Error", JOptionPane.WARNING_MESSAGE);
                }

                input.setText("");
            }

            private boolean isExtension(String i) {
                return !(i == null || i.trim().equals("") || i.charAt(0) != '.' || listboxContains(i));
            }

            private boolean listboxContains(String item) {
                for (int i = 0; i < listDisplay.getItemCount(); i++) {
                    if (item.equals(listDisplay.getItemAt(i))) {
                        return true;
                    }
                }

                return false;
            }

            private void removeAction() {
                if (listDisplay.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(null, "There are no extensions to remove!", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                listDisplay.removeItemAt(listDisplay.getSelectedIndex());
            }
        }
    }

    public static CustomFilter saveFilter(CustomFilter filter) {
        //Validates Name
        String name = filter.getName();
        name = SaveOperations.validateName(name);
        filter.setName(name);

        //Creates File
        File output = new File(filterPath + name + filterExt);

        //Already Exists
        if (output.exists()) {
            //Don't Overwrite
            if (!SaveOperations.isOverwrite(output)) {
                //EXIT
                return null;
            }
        }

        //Attempts To Save File
        try {
            output.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(output));
            writer.write(filter.toString());
            writer.close();
            return readFilter(output);
        } catch (IOException ex) {
            SaveOperations.displayMessage(SaveOperations.IO_WRITER_ERROR, output);
            return null;
        }
    }

    private static class SaveOperations {

        public final static int INVALID_NAME = 1,
                OVERRIDE_WARNING = 2,
                IO_WRITER_ERROR = 3;

        public static boolean isValidFileName(String name) {
            return !name.contains("\\")
                    && !name.trim().equals("")
                    && !name.contains(filterExt)
                    && !name.contains("/")
                    && !name.contains(CustomFilter.FILE_NAME_DELIM)
                    && !name.contains(CustomFilter.FILE_EXT_DELIM);
        }

        public static boolean isFileExists(File toSave) {
            return toSave.exists();
        }

        public static boolean isOverwrite(File toSave) {
            return ((Integer) displayMessage(OVERRIDE_WARNING, toSave)).equals(JOptionPane.YES_OPTION);
        }

        public static Object displayMessage(int command) {
            return displayMessage(command, null);
        }

        public static Object displayMessage(int command, File optional) {
            if (command == INVALID_NAME) {
                return JOptionPane.showInputDialog(null, "The file name is not valid. Please enter a valid name.",
                        "File Name Error", JOptionPane.ERROR_MESSAGE);
            } else if (command == OVERRIDE_WARNING) {
                return JOptionPane.showConfirmDialog(null, optional.getName() + " already exists, "
                        + "are you sure you want to overwrite this file?",
                        "Override Warning", JOptionPane.YES_NO_OPTION);

            } else if (command == IO_WRITER_ERROR) {
                JOptionPane.showMessageDialog(null, "There was a file operation error in writing "
                        + optional.getName(), "File Error", JOptionPane.ERROR_MESSAGE);
            }

            return -1;
        }

        public static String validateName(String name) {
            while (!isValidFileName(name)) {
                Object o = displayMessage(INVALID_NAME);

                if (o instanceof String) {
                    name = (String) o;
                }
            }

            return name;
        }
    }
}
