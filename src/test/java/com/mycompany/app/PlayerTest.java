package com.mycompany.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testPlayerInitialState() {
        Player player = new Player();
        assertEquals(0, player.symbol);
        assertEquals(0, player.move);
        assertFalse(player.selected);
        assertFalse(player.win);
    }

    @Test
    void testPlayerWithSymbol() {
        Player player = new Player();
        player.symbol = 'X';
        player.move = 5;
        player.selected = true;
        player.win = true;

        assertEquals('X', player.symbol);
        assertEquals(5, player.move);
        assertTrue(player.selected);
        assertTrue(player.win);
    }
}