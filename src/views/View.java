package views;

import helpers.GameTimer;
import helpers.RealDateTime;
import models.Model;
import models.datastructures.DataScores;
import views.panels.AskName;
import views.panels.GameBoard;
import views.panels.GameResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.time.format.DateTimeFormatter;

/**
 * This class creates a main window (extends JFrame)
 */
public class View extends JFrame {
    private final Model model;
    private GameBoard gameBoard;
    private GameResult gameResult;
    private final RealDateTime realDateTime;
    private final GameTimer gameTime;
    private AskName askName;

    /**
     * Main window JFrame
     * @param model The already created model
     */
    public View(Model model) {
        this.model = model;

        setupFrame();
        setupPanels();


        realDateTime = new RealDateTime(this);
        realDateTime.start();

        gameTime = new GameTimer(this);
    }

    /**
     * Let's set the JFrame properties
     */
    private void setupFrame() {
        this.setTitle("Hangman 2023");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(590,250));
    }

    /**
     * Creates panels and the objects needed for them on the main window
     */
    private void setupPanels() {
        gameBoard = new GameBoard(model);
        gameResult = new GameResult();

        this.add(gameBoard, BorderLayout.NORTH);
        this.add(gameResult, BorderLayout.CENTER);
    }
    // All methods register* in file Controller.java
    /**
     * Take the leaderboard button from gameBoard and add an actionListener to the button
     * @param al actionListener
     */
    public void registerButtonScores(ActionListener al) {
        gameBoard.getBtnScore().addActionListener(al);
    }

    /**
     * Take the New Game button from the game board and add an actionListener to the button
     * @param al actionListener
     */
    public void registerButtonNew(ActionListener al) {
        gameBoard.getBtnNew().addActionListener(al);
    }

    /**
     * Meetod lisab ActionListener numule Send
     * @param al
     */
    public void registerButtonSend(ActionListener al){
        gameBoard.getBtnSend().addActionListener(al);
    }

    /**
     * Take the game pause button from the game board and add an actionListener to the button
     * @param al actionListener
     */
    public void registerButtonCancel(ActionListener al) {
        gameBoard.getBtnCancel().addActionListener(al);
    }

    /**
     * Take a ComboBox from the game board and add an itemListener
     * @param il itemListener
     */
    public void registerComboBoxChange(ItemListener il) {
        gameBoard.getCmbCategory().addItemListener(il);
    }

    /**
     * Update the leaderboard table (DefaultTableModel)
     */
    public void updateScoresTable() {
        model.getDtmScores().setRowCount(0);
        for(DataScores ds : model.getDataScores()) {
            String gameTime = ds.gameTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            String name = ds.playerName();
            String word = ds.word();
            String chars = ds.missedLetters();
            int timeSeconds = ds.timeSeconds();
            String humanTime = convertSecToMMSS(timeSeconds);
            model.getDtmScores().addRow(new Object[] {gameTime, name, word, chars, humanTime});
        }
    }

    /**
     * Converts full seconds to minutes and seconds. 100 sec => 01:30 (1 min 30 sec)
     * @param seconds full seconds
     * @return 00:00 in format
     */
    private String convertSecToMMSS(int seconds) {
        int min = (seconds / 60);
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }

    /**
     * Show new game button and other button changes. There is no game
     */
    public void showNewButton() {
        gameBoard.getBtnNew().setEnabled(true);
        gameBoard.getCmbCategory().setEnabled(true);
        gameBoard.getBtnSend().setEnabled(false);
        gameBoard.getBtnCancel().setEnabled(false);
        gameBoard.getTxtChar().setEnabled(false);
        this.setNewImage(model.getImageFiles().size()-1);
    }

    /**
     * Hide new game button and other button changes. The game is on.
     */
    public void hideNewButtons() {
        gameBoard.getBtnNew().setEnabled(false);
        gameBoard.getCmbCategory().setEnabled(false);
        gameBoard.getBtnSend().setEnabled(true);
        gameBoard.getBtnCancel().setEnabled(true);
        gameBoard.getTxtChar().setEnabled(true);
    }
    /**
     * Return the time label located on the gameBoard (top panel)
     * @return label
     */
    public JLabel getLblTime() {
        return gameBoard.getLblTime();
    }

    /**
     * Return the guessed word label located in the gameResult panel (bottom panel)
     * @return label
     */
    public JLabel getLblResult() {
        return gameResult.getLblResult();
    }

    /**
     * Returns the GameBoard panel (top panel)
     * @return gameBoard
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Returns the real time
     * @return RealDateTime
     */
    public RealDateTime getRealDateTime() {
        return realDateTime;
    }

    /**
     * Reaturns the game time
     * @return GameTimer
     */
    public GameTimer getGameTime() {
        return gameTime;
    }

    /**
     * Set a new image according to the image number
     * @param id image id (0..11)
     */
    public void setNewImage(int id) {
        ImageIcon imageIcon = new ImageIcon(model.getImageFiles().get(id));
        gameBoard.getGameImages().getLblImage().setIcon(imageIcon);
    }

    /**
     * Returns an input box
     * @return JTextField
     */
    public JTextField getTxtChar() {
        return gameBoard.getTxtChar();
    }

    /**
     * Access ERROR Label
     * @return
     */
    public JLabel getLblError() {
        return gameBoard.getLblError();
    }
    public JComboBox getCmbCategory() {
        return gameBoard.getCmbCategory();
    }

    /**
     * Window to ask users name
     */
    public void askName() {
        this.askName = new AskName(model);
        askName.setVisible(true);
    }

    /**
     * Reset category and faults info
     */
    public void resetGame() {
        this.getCmbCategory().setSelectedIndex(0);
        this.getLblError().setText("Wrong 0 letter(s):");
    }
}
