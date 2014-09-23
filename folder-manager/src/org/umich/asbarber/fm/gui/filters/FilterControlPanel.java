package org.umich.asbarber.fm.gui.filters;

import org.umich.asbarber.fm.gui.tree.JFileTree;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileFilter;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.umich.asbarber.fm.file.filters.CustomFilter;
import org.umich.asbarber.fm.file.filters.DirectoryFilter;
import org.umich.asbarber.fm.file.filters.OrFilter;

/**
 *
 * @author Aaron
 */
public class FilterControlPanel extends JPanel {

    private JFileTree treePane;

    private JButton applyFilter, unapplyFilter,
            addFilter, removeFilter;
    private JFilterList filterList;
    private JButton create, delete, view;
    private JLabel title;

    public FilterControlPanel() {
        super();
        initPanel();
        initComponents();
        addComponents();
    }

    private void initPanel() {
        this.setLayout(new BorderLayout());
    }

    private void initComponents() {
        //Instantiate Components
        applyFilter = new JButton("Apply Filter Set");
        unapplyFilter = new JButton("Unapply Filter Set");

        addFilter = new JButton("Add To Set");
        removeFilter = new JButton("Remove From Set");

        create = new JButton("Create");
        delete = new JButton("Delete");
        view = new JButton("View");

        title = new JLabel("Filter Operations");
        title.setFont(title.getFont().deriveFont((float) (title.getFont().getSize() * 1.5)));

        filterList = new JFilterList();

        //Listeners
        ActionListener listener = new Listener();
        applyFilter.addActionListener(listener);
        unapplyFilter.addActionListener(listener);
        addFilter.addActionListener(listener);
        removeFilter.addActionListener(listener);
        create.addActionListener(listener);
        delete.addActionListener(listener);
        view.addActionListener(listener);
    }

    private void addComponents() {
        JPanel north = new JPanel();
        north.setLayout(new GridLayout(0, 1));
        north.add(title);
        north.add(applyFilter);

        JScrollPane center = new JScrollPane(filterList);

        JPanel west = new JPanel();
        west.setLayout(new BoxLayout(west, BoxLayout.Y_AXIS));
        west.add(addFilter);
        west.add(removeFilter);

        JPanel east = new JPanel();
        east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
        east.add(create);
        east.add(delete);
        east.add(view);

        this.add(north, BorderLayout.NORTH);
        this.add(center, BorderLayout.CENTER);
        this.add(unapplyFilter, BorderLayout.SOUTH);
        this.add(west, BorderLayout.WEST);
        this.add(east, BorderLayout.EAST);
    }

    public void setTree(JFileTree treePane) {
        this.treePane = treePane;
    }

    public FileFilter makeFilter() {
        List<CustomFilter> myFilters = filterList.getAppliedItems();
        if (myFilters.isEmpty()) {
            return null;
        }

        FileFilter customFilter = myFilters.get(0);

        for (int i = 1; i < myFilters.size(); i++) {
            customFilter = new OrFilter(customFilter, myFilters.get(i));
        }

        return new OrFilter(customFilter, new DirectoryFilter());
    }

    private class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getActionCommand().equals("Add To Set")) {
                filterList.addApply();
            } else if (ae.getActionCommand().equals("Remove From Set")) {
                filterList.removeApply();
            } else if (ae.getActionCommand().equals("Create")) {
                CustomFilter addedFilter = FilterOperations.createFilter();

                if (addedFilter != null) {
                    filterList.addItem(addedFilter);
                }
            } else if (ae.getActionCommand().equals("Delete")) {
                CustomFilter deleteFilter = filterList.getSelectedItem();
                if (deleteFilter != null) {
                    FilterOperations.deleteFilter(deleteFilter);
                    filterList.removeItem(deleteFilter);
                }
            } else if (ae.getActionCommand().equals("View")) {
                if (filterList.getSelectedItem() != null) {
                    FilterOperations.viewFilter(filterList.getSelectedItem());
                }
            } else if (ae.getActionCommand().equals("Apply Filter Set")) {
                if (treePane != null) {
                    treePane.filter(makeFilter());
                }
            } else if (ae.getActionCommand().equals("Unapply Filter Set")) {
                if (treePane != null) {
                    treePane.filter(null);
                }
            }
        }
    }
}
