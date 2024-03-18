package views.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel for guess word. Contains one JLabel
 */
public class GameResult extends JPanel {
    private final JLabel lblResult = new JLabel("L E T ' S  P L A Y");

    /**
     * Constructor for the GameResult panel. This is always done when the object is created. It will be done once!
     */
    public GameResult() {
        setBackground(new Color(230,250,220));
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(5,5,5,5));

        // Font style for JLabel
        Font fontBold = new Font("Verdana", Font.BOLD, 24);
        lblResult.setFont(fontBold);
        lblResult.setHorizontalAlignment(JLabel.CENTER);

        add(lblResult);
    }

    /**
     * Only the label needs to be picked up from this panel
     * @return JLabel
     */
    public JLabel getLblResult() {
        return lblResult;
    }
}
