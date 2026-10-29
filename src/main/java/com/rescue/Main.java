package com.rescue;

import com.rescue.view.ControlPanelView;
import com.rescue.controller.ControlPanelController;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControlPanelView view = new ControlPanelView();
            new ControlPanelController(view);
            view.setVisible(true);
        });
    }
}