package org.umich.asbarber.fm.gui.filters;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.umich.asbarber.fm.file.filters.CustomFilter;

/**
 *
 * @author Aaron
 */
public class JFilterList extends JList {

    public JFilterList() {
        initModel();
        super.addListSelectionListener(new Selector());
        super.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        super.setCellRenderer(new Renderer());
    }

    private void initModel() {
        DefaultListModel model = new DefaultListModel();

        for (CustomFilter filter : FilterOperations.getSavedFilters()) {
            model.addElement(new ListItem(filter));
        }
        super.setModel(model);
    }

    public void addItem(CustomFilter item) {
        ((DefaultListModel) super.getModel()).addElement(new ListItem(item));
    }

    public void removeItem(CustomFilter item) {
        ((DefaultListModel) super.getModel()).removeElement(new ListItem(item));
    }

    public CustomFilter getSelectedItem() {
        if (this.getSelectedValue() == null) {
            return null;
        } else {
            return ((ListItem) this.getSelectedValue()).filter;
        }
    }

    public List<CustomFilter> getAppliedItems() {
        List<CustomFilter> list = new ArrayList<CustomFilter>();
        Enumeration<ListItem> enume = (Enumeration<ListItem>) ((DefaultListModel) super.getModel()).elements();

        while (enume.hasMoreElements()) {
            ListItem item = enume.nextElement();
            if (item.isApplied()) {
                list.add(item.filter);
            }
        }
        return list;
    }

    public void addApply() {
        for (Object o : super.getSelectedValues()) {
            ((ListItem) o).addApply();
        }
        removeSelection();
        this.clearSelection();
    }

    public void removeApply() {
        for (Object o : super.getSelectedValues()) {
            ((ListItem) o).removeApply();
        }

        this.clearSelection();
    }

    public void removeSelection() {
        for (int i = 0; i < super.getModel().getSize(); i++) {
            ((ListItem) super.getModel().getElementAt(i)).removeSelection();
        }
    }

    private class ListItem {

        public final static int NOT_SELECTED = 0,
                SELECTED = 1,
                APPLIED = 2,
                APPLIED_SELECTED = 3;

        private CustomFilter filter;
        private int selectionType = 0;

        public ListItem(CustomFilter filter) {
            this.filter = filter;
        }

        public void setSelectionType(int i) {
            selectionType = i;
        }

        public int getSelectionType() {
            return selectionType;
        }

        public boolean isApplied() {
            return (selectionType == APPLIED || selectionType == APPLIED_SELECTED);
        }

        public void addSelection() {
            switch (selectionType) {
                case NOT_SELECTED:
                    selectionType = SELECTED;
                    break;
                case APPLIED:
                    selectionType = APPLIED_SELECTED;
                    break;
            }
        }

        public void addApply() {
            switch (selectionType) {
                case NOT_SELECTED:
                    selectionType = APPLIED;
                    break;
                case SELECTED:
                    selectionType = APPLIED_SELECTED;
                    break;
            }
        }

        public void removeSelection() {
            switch (selectionType) {
                case SELECTED:
                    selectionType = NOT_SELECTED;
                    break;
                case APPLIED_SELECTED:
                    selectionType = APPLIED;
                    break;
            }
        }

        public void removeApply() {
            switch (selectionType) {
                case APPLIED:
                    selectionType = NOT_SELECTED;
                    break;
                case APPLIED_SELECTED:
                    selectionType = SELECTED;
                    break;
            }
        }

        @Override
        public String toString() {
            return filter.getName();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ListItem) {
                ListItem otherItem = (ListItem) o;

                return this.toString().equals(otherItem.toString());
            }

            return false;
        }
    }

    private class Selector implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent lse) {
            if (!lse.getValueIsAdjusting()) {
                return;
            }

            ListItem element = (ListItem) ((JList) lse.getSource()).getSelectedValue();

            removeSelection();

            element.addSelection();
        }
    }

    private class Renderer extends DefaultListCellRenderer {

        private final Color UNSELECTED_COLOR = JFilterList.this.getBackground(),
                SELECTED_COLOR = JFilterList.this.getSelectionBackground(),
                APPLIED_COLOR = Color.YELLOW,
                APPLIED_SELECTED_COLOR = Color.ORANGE;

        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            //Selection Type
            int type = ((ListItem) value).selectionType;
            Color type_Color;

            switch (type) {
                case ListItem.NOT_SELECTED:
                    type_Color = UNSELECTED_COLOR;
                    break;
                case ListItem.SELECTED:
                    type_Color = SELECTED_COLOR;
                    break;
                case ListItem.APPLIED:
                    type_Color = APPLIED_COLOR;
                    break;
                case ListItem.APPLIED_SELECTED:
                    type_Color = APPLIED_SELECTED_COLOR;
                    break;
                default:
                    type_Color = list.getBackground();
            }

            setBackground(type_Color);
            setForeground(list.getForeground());
            setText(value.toString());
            return this;
        }
    }
}
