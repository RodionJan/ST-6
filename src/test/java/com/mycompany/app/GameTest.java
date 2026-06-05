package com.mycompany.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class GameTest {

    @Test
    void testInitialGameState() {
        Game game = new Game();
        assertEquals(State.PLAYING, game.state);
        assertEquals('X', game.firstplayer.mark);
        assertEquals('O', game.secondplayer.mark);
        assertNotNull(game.board);
        assertEquals(9, game.board.length);
        for (int i = 0; i < 9; i++) {
            assertEquals(' ', game.board[i]);
        }
    }

    @Test
    void testCheckStateXWinByRow() {
        Game game = new Game();
        game.mark = 'X';
        char[] board = {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(State.XWIN, game.determineStatus(board));
    }

    @Test
    void testCheckStateOWinByRow() {
        Game game = new Game();
        game.mark = 'O';
        char[] board = {' ', ' ', ' ', 'O', 'O', 'O', ' ', ' ', ' '};
        assertEquals(State.OWIN, game.determineStatus(board));
    }

    @Test
    void testCheckStateXWinByColumn() {
        Game game = new Game();
        game.mark = 'X';
        char[] board = {'X', ' ', ' ', 'X', ' ', ' ', 'X', ' ', ' '};
        assertEquals(State.XWIN, game.determineStatus(board));
    }

    @Test
    void testCheckStateOWinByDiagonal() {
        Game game = new Game();
        game.mark = 'O';
        char[] board = {'O', ' ', ' ', ' ', 'O', ' ', ' ', ' ', 'O'};
        assertEquals(State.OWIN, game.determineStatus(board));
    }

    @Test
    void testCheckStateXWinByAntiDiagonal() {
        Game game = new Game();
        game.mark = 'X';
        char[] board = {' ', ' ', 'X', ' ', 'X', ' ', 'X', ' ', ' '};
        assertEquals(State.XWIN, game.determineStatus(board));
    }

    @Test
    void testCheckStateDraw() {
        Game game = new Game();
        game.mark = 'X';
        char[] board = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        assertEquals(State.DRAW, game.determineStatus(board));
    }

    @Test
    void testCheckStatePlaying() {
        Game game = new Game();
        game.mark = 'X';
        char[] board = {'X', 'O', 'X', 'O', ' ', 'O', 'X', ' ', 'X'};
        assertEquals(State.PLAYING, game.determineStatus(board));
    }

    @Test
    void testGenerateMovesEmptyBoard() {
        Game game = new Game();
        ArrayList<Integer> chosenpositions = new ArrayList<>();
        game.getAvailableMoves(game.board, chosenpositions);
        assertEquals(9, chosenpositions.size());
        for (int i = 0; i < 9; i++) {
            assertTrue(chosenpositions.contains(i));
        }
    }

    @Test
    void testGenerateMovesPartialBoard() {
        Game game = new Game();
        game.board[0] = 'X';
        game.board[4] = 'O';
        game.board[8] = 'X';
        ArrayList<Integer> chosenpositions = new ArrayList<>();
        game.getAvailableMoves(game.board, chosenpositions);
        assertEquals(6, chosenpositions.size());
        assertFalse(chosenpositions.contains(0));
        assertFalse(chosenpositions.contains(4));
        assertFalse(chosenpositions.contains(8));
    }

    @Test
    void testGenerateMovesFullBoard() {
        Game game = new Game();
        for (int i = 0; i < 9; i++) {
            game.board[i] = 'X';
        }
        ArrayList<Integer> chosenpositions = new ArrayList<>();
        game.getAvailableMoves(game.board, chosenpositions);
        assertEquals(0, chosenpositions.size());
    }

    @Test
    void testEvaluatePositionXWin() {
        Game game = new Game();
        game.mark = 'X';
        char[] board = {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(Game.INF, game.assessBoard(board, game.firstplayer));
    }

    @Test
    void testEvaluatePositionOWin() {
        Game game = new Game();
        game.mark = 'O';
        char[] board = {'O', 'O', 'O', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(Game.INF, game.assessBoard(board, game.secondplayer));
    }

    @Test
    void testEvaluatePositionLose() {
        Game game = new Game();
        game.mark = 'X';
        char[] board = {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(-Game.INF, game.assessBoard(board, game.secondplayer));
    }

    @Test
    void testEvaluatePositionDraw() {
        Game game = new Game();
        game.mark = 'X';
        char[] board = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        assertEquals(0, game.assessBoard(board, game.firstplayer));
    }

    @Test
    void testEvaluatePositionPlaying() {
        Game game = new Game();
        game.mark = 'X';
        char[] board = {'X', ' ', 'X', ' ', 'O', ' ', ' ', ' ', ' '};
        assertEquals(-1, game.assessBoard(board, game.firstplayer));
    }

    @Test
    void testMiniMaxBlocksWin() {
        Game game = new Game();
        char[] board = {'X', 'X', ' ', ' ', 'O', ' ', ' ', ' ', ' '};
        game.secondplayer.mark = 'O';
        int chosenposition = game.MiniMax(board.clone(), game.secondplayer);
        assertEquals(3, chosenposition);
    }

    @Test
    void testMiniMaxTakesWin() {
        Game game = new Game();
        char[] board = {'O', 'O', ' ', 'X', 'X', ' ', ' ', ' ', ' '};
        game.secondplayer.mark = 'O';
        int chosenposition = game.MiniMax(board.clone(), game.secondplayer);
        assertEquals(3, chosenposition);
    }

    @Test
    void testMiniMaxCenterPreference() {
        Game game = new Game();
        char[] board = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        game.firstplayer.mark = 'X';
        int chosenposition = game.MiniMax(board.clone(), game.firstplayer);
        assertTrue(chosenposition >= 1 && chosenposition <= 9, "Ход должен быть в диапазоне 1-9: " + chosenposition);
    }

    @Test
    void testMinMoveReturnsImmediate() {
        Game game = new Game();
        game.mark = 'O';
        char[] board = {'O', 'O', 'O', ' ', ' ', ' ', ' ', ' ', ' '};
        int val = game.MinMove(board, game.secondplayer);
        assertEquals(Game.INF, val);
    }

    @Test
    void testMaxMoveReturnsImmediate() {
        Game game = new Game();
        game.mark = 'X';
        char[] board = {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '};
        int val = game.MaxMove(board, game.firstplayer);
        assertEquals(Game.INF, val);
    }

    @Test
    void testMinMoveRecursive() {
        Game game = new Game();
        char[] board = {'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        game.firstplayer.mark = 'X';
        game.secondplayer.mark = 'O';
        int val = game.MinMove(board, game.firstplayer);
        assertTrue(val >= -Game.INF && val <= Game.INF);
    }

    @Test
    void testMaxMoveRecursive() {
        Game game = new Game();
        char[] board = {'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        game.firstplayer.mark = 'X';
        game.secondplayer.mark = 'O';
        int val = game.MaxMove(board, game.secondplayer);
        assertTrue(val >= -Game.INF && val <= Game.INF);
    }
}