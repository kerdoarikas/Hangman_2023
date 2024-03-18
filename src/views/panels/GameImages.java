package views.panels;

import models.Model;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that creates an image panel and at the same time reads the images in the image folder into an array
 */
public class GameImages extends JPanel {
    private final Model model;
    private JLabel lblImage; // A label that contains an image

    /**
     * A class constructor that is executed only at the time of order creation
     * @param model Model
     */
    public GameImages(Model model) {
        this.model = model;
        this.setBackground(new Color(195, 220, 255));
        this.setPreferredSize(new Dimension(130,130));
        readImagesFolder();

        lblImage = new JLabel();
        ImageIcon imageIcon = new ImageIcon(model.getImageFiles().get(model.getImageFiles().size()-1));

        lblImage.setIcon(imageIcon);

        add(lblImage);
    }

    /**
     * Reads the image files (all files!) into an array and the content is also added to the model.
     * There might be a better solution for this
     */
    private void readImagesFolder() {
        File folder = new File(model.getImagesFolder());
        File[] files = folder.listFiles();
        List<String> imageFiles = new ArrayList<>();
        for(File file: files) {
            imageFiles.add(file.getAbsolutePath());
        }
        model.setImageFiles(imageFiles);
    }

    /**
     * Returns the label of the image
     * @return label of the image
     */
    public JLabel getLblImage() {
        return lblImage;
    }
}
