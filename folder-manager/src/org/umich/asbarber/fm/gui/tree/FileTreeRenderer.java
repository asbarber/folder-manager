package org.umich.asbarber.fm.gui.tree;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Aaron
 */
public class FileTreeRenderer extends DefaultTreeCellRenderer {

    private final static String dirPath = "resources/icon_directory_1.png";

    private final static int ICON_HEIGHT = 24, ICON_WIDTH = 24;
    private ImageIcon directoryIcon;

    public FileTreeRenderer() {
        directoryIcon = new ImageIcon(dirPath);
        if (directoryIcon != null) {
            directoryIcon = resizeImageIcon(directoryIcon);
        }
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        FileNode node = (FileNode) value;

        if (node.isDirectory() && directoryIcon != null) {
            setIcon(directoryIcon);
        }

        return this;
    }

    private static ImageIcon resizeImageIcon(ImageIcon icon) {
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }
}
