package org.umich.asbarber.fm.gui.fileviewer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.umich.asbarber.fm.file.PartsFile;
import org.umich.asbarber.fm.file.filters.PhotoFilter;

/**
 *
 * @author Aaron
 */
public class ViewPanel extends JPanel {

    private DataPanel dataPanel;
    private JLabel title;

    public ViewPanel() {
        initPanel();
        initComponents();
        open(null);
    }

    private void initPanel() {
        this.setLayout(new BorderLayout());
    }

    private void initComponents() {
        title = new JLabel("File Viewer");
        title.setFont(title.getFont().deriveFont((float) (title.getFont().getSize() * 1.5)));
    }

    public void open(PartsFile file) {
        //Clears
        super.removeAll();
        dataPanel = null;

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        //Info about file
        if (file != null) {
            dataPanel = new DataPanel(file);
            content.add(dataPanel);
        }

        //Text or image opening
        if (file == null) {
            //open nothing
        } else if (file.getExtension().equals(".txt")) {
            content.add(textReader(file));
        } else if ((new PhotoFilter()).accept(file.getFile())) {
            content.add(imageReader(file));
        } else {
            content.add(new JLabel("This file type is not supported in the 'File Viewer'."));
        }

        this.add(title, BorderLayout.NORTH);
        this.add(content, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();
    }

    private Component textReader(PartsFile file) {
        JTextArea comp = new JTextArea();
        comp.setEditable(false);

        try {
            FileReader reader = new FileReader(file.getFile());

            int c;
            while ((c = reader.read()) != -1) {
                comp.append(Character.toString((char) c));
            }

            reader.close();
        } catch (IOException ex) {
            comp.setText("Error in reading the file.");
        }

        return comp;
    }

    private Component imageReader(PartsFile file) {
        ImageIcon img = new ImageIcon(file.getFile().getPath());

        //Initial values
        int widthInitial = img.getIconWidth();
        int heightInitial = img.getIconHeight();
        int heightFinal = super.getHeight() - dataPanel.getHeight();

        //Scale size
        double scaleFactor = (double) heightFinal / heightInitial;
        int widthFinal = (int) (widthInitial * scaleFactor);

        //Scale action
        if (widthFinal > 0 && heightFinal > 0) {
            img = scaleImageIcon(img, widthFinal, heightFinal);
        }

        return new JScrollPane(new JLabel(img));
    }

    private ImageIcon scaleImageIcon(ImageIcon sourceIcon, int w, int h) {
        Image sourceImage = sourceIcon.getImage();
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(sourceImage, 0, 0, w, h, null);
        g2.dispose();

        return new ImageIcon(resizedImg);
    }
}
