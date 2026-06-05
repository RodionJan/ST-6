package com.mycompany.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testPlayerInitialState() {
        Player player = new Player();
        assertEquals(0, player.mark);
        assertEquals(0, player.chosenposition);
        assertFalse(player.ischosen);
        assertFalse(player.haswon);
    }

    @Test
    void testPlayerWithmark() {
        Player player = new Player();
        player.mark = 'X';
        player.chosenposition = 5;
        player.ischosen = true;
        player.haswon = true;

        assertEquals('X', player.mark);
        assertEquals(5, player.chosenposition);
        assertTrue(player.ischosen);
        assertTrue(player.haswon);
    }
}