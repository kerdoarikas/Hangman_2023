package controllers.listeners;

import models.Model;
import views.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonCancel implements ActionListener {
    private final Model model;
    private final View view;
    public ButtonCancel(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Runs when the Cancel Game button is clicked
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        view.showNewButton();

        view.getGameTime().stopTimer();
        view.getGameTime().setRunning(false);

        view.getRealDateTime().start();
        view.getLblResult().setText("L E T ' S  P L A Y");

    }
}
