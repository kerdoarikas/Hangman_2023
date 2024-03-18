package main;

import controllers.Controller;
import models.Database;
import models.Model;
import views.View;

import javax.swing.*;

/**
 * This is the class that is invoked
 */
public class AppMain {

    /**
     * A method that creates the necessary objects for playing. This means model, GUI, etc.
     */
    private static void start() {
        Model model = new Model();
        View view = new View(model);
        new Controller(model, view);

        view.pack();
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }

    /**
     * Main method
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppMain::start);
    }
}
