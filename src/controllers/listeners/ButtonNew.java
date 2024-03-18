package controllers.listeners;

import models.Database;
import models.Model;
import views.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ButtonNew implements ActionListener {
    private final Model model;
    private final View view;

    /**
     * New Game button constructor.
     *
     * @param model Model
     * @param view View
     */
    public ButtonNew(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Runs when the New Game button is pressed
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        view.hideNewButtons();
        view.getRealDateTime().stop();
        if (!view.getGameTime().isRunning()) {
            view.getGameTime().setSeconds(0);
            view.getGameTime().setMinutes(0);
            view.getGameTime().setRunning(true);
            view.getGameTime().startTimer();
        } else {
            view.getGameTime().stopTimer();
            view.getGameTime().setRunning(false);
        }
        view.setNewImage(0);
        model.setImageId(0);
        view.getTxtChar().requestFocus();
        view.getLblError().setForeground(Color.BLACK);
        view.getLblError().setText("Wrong 0 letter(s):");
        model.resetGame();

        new Database(model).selectRandomWord(model.getSelectedCategory());

        List<String> lettersReplaced = Arrays.asList(model.getRandomWordArr());
        Collections.fill(lettersReplaced, "_");
        String lettersReplacedSpacesAdded = String.join(" ", lettersReplaced);
        view.getLblResult().setText(lettersReplacedSpacesAdded);

    }
}


