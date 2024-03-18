package controllers.listeners;

import models.Database;
import models.Model;
import views.View;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Button Leaderboard functionality (implements ActionListener)
 */
public class ButtonScores implements ActionListener {
    private final Model model;
    private final View view;
    private final String[] header = new String[] {"Date", "Name", "Word", "Letters", "Game time"};
    private final DefaultTableModel dtm = new DefaultTableModel(header,0);
    private final JTable table = new JTable(dtm);
    private JDialog dialogScore;

    public ButtonScores(Model model, View view) {
        this.model = model;
        this.view = view;
        model.setDtmScores(dtm);
        createDialog();
    }

    private void createDialog() {
        dialogScore = new JDialog();
        dialogScore.setPreferredSize(new Dimension(530,180));

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        // Table not editable https://stackoverflow.com/questions/1990817/how-to-make-a-jtable-non-editable
        table.setDefaultEditor(Object.class, null);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(4).setCellRenderer(center);

        dialogScore.add(new JScrollPane(table));
        dialogScore.setTitle("Leaderboard");
        dialogScore.pack();
        dialogScore.setLocationRelativeTo(null);
        dialogScore.setModal(true);
    }

    /**
     * Button Leaderboard clicked
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        new Database(model).selectScores();
        if(model.getDataScores().size() > 0) {
            view.updateScoresTable();
            dialogScore.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(view, "Play first! The leaderboard is empty!");
        }
    }
}
