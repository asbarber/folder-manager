package org.umich.asbarber.fm.gui.tree;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author Aaron
 */
public class RootBrowser {

    public static File browseForRoot(File init) {
        JFileChooser browser = new JFileChooser(init);
        browser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = browser.showDialog(new JFrame(), "Select As Root");

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return browser.getSelectedFile();
        } else {
            return null;
        }
    }
}
