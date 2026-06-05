package com.mycompany.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TicTacToeCellTest {

    @Test
    void testCellCreation() {
        TicTacToeCell cell = new TicTacToeCell(0, 0, 0);
        assertNotNull(cell);
        assertEquals(0, cell.getNum());
        assertEquals(0, cell.getRow());
        assertEquals(0, cell.getCol());
        assertEquals(' ', cell.getSymbol());
    }

    @Test
    void testCellSetMarker() {
        TicTacToeCell cell = new TicTacToeCell(4, 1, 1);
        cell.setSymbol("X");
        assertEquals('X', cell.getSymbol());
        assertFalse(cell.isEnabled());
    }

    @Test
    void testCellGetMethods() {
        TicTacToeCell cell = new TicTacToeCell(8, 2, 2);
        assertEquals(8, cell.getNum());
        assertEquals(2, cell.getRow());
        assertEquals(2, cell.getCol());
    }

    @Test
    void testMultipleCells() {
        TicTacToeCell cell1 = new TicTacToeCell(0, 0, 0);
        TicTacToeCell cell2 = new TicTacToeCell(4, 1, 1);
        TicTacToeCell cell3 = new TicTacToeCell(8, 2, 2);

        cell1.setSymbol("X");
        cell2.setSymbol("O");
        cell3.setSymbol("X");

        assertEquals('X', cell1.getSymbol());
        assertEquals('O', cell2.getSymbol());
        assertEquals('X', cell3.getSymbol());
    }
}