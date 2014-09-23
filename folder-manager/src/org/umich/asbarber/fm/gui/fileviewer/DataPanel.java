package org.umich.asbarber.fm.gui.fileviewer;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.umich.asbarber.fm.file.PartsFile;

/**
 *
 * @author Aaron
 */
public class DataPanel extends JPanel {

    private JLabel iName, iExt, iPath,
            name, ext, path;

    private PartsFile data;

    public DataPanel(PartsFile file) {
        this.data = file;

        initLayout();
        initComponents();
        addComponents();
    }

    private void initLayout() {
        this.setLayout(new GridLayout(0, 2));
    }

    private void initComponents() {
        iName = new JLabel("Name: ");
        iExt = new JLabel("Extension: ");
        iPath = new JLabel("Path: ");

        name = new JLabel(data.getName());
        ext = new JLabel(data.getExtension());
        path = new JLabel(data.getPath());
    }

    private void addComponents() {
        this.add(iName);
        this.add(name);

        this.add(iPath);
        this.add(path);

        this.add(iExt);
        this.add(ext);
    }
}
