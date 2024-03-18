package controllers.listeners;

import models.Database;
import models.Model;
import views.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ButtonSend implements ActionListener {

    private Model model;
    private View view;

    public ButtonSend(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Runs when send button is clicked
     * @param EventBtnSendPressed the event to be processed
     */
    public void actionPerformed(ActionEvent EventBtnSendPressed) {
        String userChar = view.getTxtChar().getText().toUpperCase();

        if (!userChar.trim().isEmpty()) {
            String[] wordReadyToShow = model.getWordToShow(userChar);
            if (model.isWordQuessed()) {
                view.showNewButton();
                view.getGameTime().stopTimer();
                view.getGameTime().setRunning(false);
                view.getRealDateTime().start();
                model.setWordQuessed(false);
                String n = JOptionPane.showInputDialog("Sisesta nimi");
                if(n == null || n.isEmpty()){n = "Tundmatu"; }
                model.setPlayerName(n);
                String strTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                model.setGameTime(strTime);
                model.setTimeSeconds(view.getGameTime().getPlayedTimeInSeconds());
                new Database(model).insertScores();
            }

            // CHECK IF USER WAS WRONG
            if (model.isQuessPassed()) {
                model.setImageId(model.getImageId()+1);
                view.setNewImage(model.getImageId());
                view.getLblError().setForeground(Color.RED);
                view.getLblError().setText(model.getQuessedWrongChars());
                model.setQuessPassed(false);
            }

            // CHECK IF GAME IS OVER
            if(model.getImageId() == 11) {
                view.showNewButton();
                view.getGameTime().stopTimer();
                view.getGameTime().setRunning(false);
                view.getRealDateTime().start();
                model.setWordQuessed(false);
                view.getLblResult().setText("L E T ' S  P L A Y");
                resetGame();
            }

            // VIEW
            if(model.getImageId() == 11) {
                view.getLblResult().setText("L E T ' S  P L A Y");
            } else {
                String wordReadyToShowSpacesAdded = String.join(" ",wordReadyToShow);
                view.getLblResult().setText(wordReadyToShowSpacesAdded);
            }
        }
        view.getTxtChar().setText("");
        view.getTxtChar().requestFocus();
    }

    /**
     * Resets all variables for a new game
     */
    private void resetGame(){
        view.getCmbCategory().setSelectedIndex(0);
        view.getLblError().setText("Wrong 0 letter(s):");
        model.resetGame();
    }
}
