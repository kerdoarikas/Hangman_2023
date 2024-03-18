package views.panels;

import helpers.TextFieldLimit;
import models.Model;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * This class makes the GameBoard panel (top panel) (extends JPanel)
 */
public class GameBoard extends JPanel {
    private final Model model;
    /**
     * This panel is on top of the JFrame and the buttons, labels and text field on the top side are placed on top of
     * this panel
     */
    private final JPanel pnlComponents = new JPanel(new GridBagLayout());
    private GameImages gameImages;
    /**
     * GridBagLayout settings
     */
    private final GridBagConstraints gbc = new GridBagConstraints();
    private JLabel lblTime;
    private JLabel lblError;
    private JTextField txtChar;
    private JButton btnNew;
    private JButton btnScore;
    private JButton btnCancel;
    private JButton btnSend;
    private JComboBox<String> cmbCategory;

    /**
     * Class constructor. Always do this part when creating an object. An object is created only once.
     * @param model A previously made model
     */
    public GameBoard(Model model) {
        this.model = model;

        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setBackground(new Color(255, 255, 160));

        pnlComponents.setBorder(new EmptyBorder(5,5,5,5));

        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2,5,2,5);
        gbc.fill = GridBagConstraints.BOTH;

        populatePanel();

        gameImages = new GameImages(model);

        this.add(pnlComponents);
        this.add(gameImages);
    }

    /**
     * All objects placed on the panel (JLabel, JButton, JTextField, JComboBox)
     */
    private void populatePanel(){
        // First Line
        lblTime = new JLabel("Here comes the right time or game time", JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        pnlComponents.add(lblTime, gbc);

        // Second Line
        JLabel lblCategory = new JLabel("Category");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        pnlComponents.add(lblCategory, gbc);

        cmbCategory = new JComboBox<>(model.getCmbNames());
        gbc.gridx = 1;
        gbc.gridy = 1;
        pnlComponents.add(cmbCategory, gbc);

        btnNew = new JButton("New Game");
        gbc.gridx = 2;
        gbc.gridy = 1;
        pnlComponents.add(btnNew, gbc);

        // Third line
        JLabel lblChar = new JLabel("Input character");
        gbc.gridx = 0;
        gbc.gridy = 2;
        pnlComponents.add(lblChar, gbc);

        /*
         * TextField all time focus
         * https://stackoverflow.com/questions/4640138/setting-the-focus-to-a-text-field
         */
        txtChar = new JTextField("", 14) {
            @Override
            public void addNotify() {
                super.addNotify();
                requestFocus();
            }
        };
        txtChar.setEnabled(false);
        txtChar.setHorizontalAlignment(JTextField.CENTER);
        txtChar.setDocument(new TextFieldLimit(1));
        gbc.gridx = 1;
        gbc.gridy = 2;
        pnlComponents.add(txtChar, gbc);

        btnSend = new JButton("Send");
        btnSend.setEnabled(false);
        gbc.gridx = 2;
        gbc.gridy = 2;
        pnlComponents.add(btnSend, gbc);

        lblError = new JLabel("Wrong 0 letter(s)");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        pnlComponents.add(lblError, gbc);

        // Fifth line
        btnCancel = new JButton("Cancel the game");
        btnCancel.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        pnlComponents.add(btnCancel, gbc);

        btnScore = new JButton("Leaderboard");
        gbc.gridx = 1;
        gbc.gridy = 4;
        pnlComponents.add(btnScore, gbc);
    }

    /**
     * Let's take a time label.
     * @return a real-time or game-time label to view
     */
    public JLabel getLblTime() {
        return lblTime;
    }
    public JLabel getLblError(){ return lblError;}

    /**
     * Let's take a button leaderboard
     * @return button
     */
    public JButton getBtnScore() {
        return btnScore;
    }

    /**
     * Let's take a letter in the input box
     * @return textfield
     */
    public JTextField getTxtChar() {
        return txtChar;
    }

    /**
     * Let's take a button new game
     * @return nutton
     */
    public JButton getBtnNew() {
        return btnNew;
    }

    /**
     * Let's take a button cancel game
     * @return button
     */
    public JButton getBtnCancel() {
        return btnCancel;
    }

    /**
     * Let's take a button letter send
     * @return button
     */
    public JButton getBtnSend() {
        return btnSend;
    }

    /**
     * Let's take a ComboBox for categories
     * @return ComboBox
     */
    public JComboBox<String> getCmbCategory() {
        return cmbCategory;
    }

    /**
     * Return the GameImage panel
     * @return GameImage
     */
    public GameImages getGameImages() {
        return gameImages;
    }

}