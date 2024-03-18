package controllers;

import controllers.listeners.*;
import models.Database;
import models.Model;
import views.View;

public class Controller {
    // private final Model model;
    // private final View view;

    public Controller(Model model, View view) {
        // this.model = model;
        // this.view = view;

        view.registerButtonScores(new ButtonScores(model, view));
        view.registerButtonNew(new ButtonNew(model, view));
        view.registerButtonCancel(new ButtonCancel(model, view));
        view.registerComboBoxChange(new CategoryItemChange(model));
        view.registerButtonSend(new ButtonSend(model, view));
    }
}
