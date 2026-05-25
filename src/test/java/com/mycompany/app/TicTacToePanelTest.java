package com.mycompany.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

class TicTacToePanelTest {

    @Test
    void testTicTacToePanelCreation() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));

        assertNotNull(panel);
    }

    @Test
    void testActionPerformedPlayer1Move() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        java.awt.Component[] components = panel.getComponents();
        assertTrue(components.length >= 1);
        TicTacToeCell firstCell = (TicTacToeCell) components[0];

        ActionEvent event = new ActionEvent(firstCell, ActionEvent.ACTION_PERFORMED, "");

        try {
            panel.actionPerformed(event);
        } catch (SecurityException e) {
        }

        assertEquals('X', firstCell.getMarker());
    }
}