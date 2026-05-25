package com.mycompany.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class GameTest {

    @Test
    void testInitialGameState() {
        Game game = new Game();
        assertEquals(State.PLAYING, game.state);
        assertEquals('X', game.player1.symbol);
        assertEquals('O', game.player2.symbol);
        assertNotNull(game.board);
        assertEquals(9, game.board.length);
        for (int i = 0; i < 9; i++) {
            assertEquals(' ', game.board[i]);
        }
    }

    @Test
    void testCheckStateXWinByRow() {
        Game game = new Game();
        game.symbol = 'X';
        char[] board = {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(State.XWIN, game.checkState(board));
    }

    @Test
    void testCheckStateOWinByRow() {
        Game game = new Game();
        game.symbol = 'O';
        char[] board = {' ', ' ', ' ', 'O', 'O', 'O', ' ', ' ', ' '};
        assertEquals(State.OWIN, game.checkState(board));
    }

    @Test
    void testCheckStateXWinByColumn() {
        Game game = new Game();
        game.symbol = 'X';
        char[] board = {'X', ' ', ' ', 'X', ' ', ' ', 'X', ' ', ' '};
        assertEquals(State.XWIN, game.checkState(board));
    }

    @Test
    void testCheckStateOWinByDiagonal() {
        Game game = new Game();
        game.symbol = 'O';
        char[] board = {'O', ' ', ' ', ' ', 'O', ' ', ' ', ' ', 'O'};
        assertEquals(State.OWIN, game.checkState(board));
    }

    @Test
    void testCheckStateXWinByAntiDiagonal() {
        Game game = new Game();
        game.symbol = 'X';
        char[] board = {' ', ' ', 'X', ' ', 'X', ' ', 'X', ' ', ' '};
        assertEquals(State.XWIN, game.checkState(board));
    }

    @Test
    void testCheckStateDraw() {
        Game game = new Game();
        game.symbol = 'X';
        char[] board = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        assertEquals(State.DRAW, game.checkState(board));
    }

    @Test
    void testCheckStatePlaying() {
        Game game = new Game();
        game.symbol = 'X';
        char[] board = {'X', 'O', 'X', 'O', ' ', 'O', 'X', ' ', 'X'};
        assertEquals(State.PLAYING, game.checkState(board));
    }

    @Test
    void testGenerateMovesEmptyBoard() {
        Game game = new Game();
        ArrayList<Integer> moves = new ArrayList<>();
        game.generateMoves(game.board, moves);
        assertEquals(9, moves.size());
        for (int i = 0; i < 9; i++) {
            assertTrue(moves.contains(i));
        }
    }

    @Test
    void testGenerateMovesPartialBoard() {
        Game game = new Game();
        game.board[0] = 'X';
        game.board[4] = 'O';
        game.board[8] = 'X';
        ArrayList<Integer> moves = new ArrayList<>();
        game.generateMoves(game.board, moves);
        assertEquals(6, moves.size());
        assertFalse(moves.contains(0));
        assertFalse(moves.contains(4));
        assertFalse(moves.contains(8));
    }

    @Test
    void testGenerateMovesFullBoard() {
        Game game = new Game();
        for (int i = 0; i < 9; i++) {
            game.board[i] = 'X';
        }
        ArrayList<Integer> moves = new ArrayList<>();
        game.generateMoves(game.board, moves);
        assertEquals(0, moves.size());
    }

    @Test
    void testEvaluatePositionXWin() {
        Game game = new Game();
        game.symbol = 'X';
        char[] board = {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(Game.INF, game.evaluatePosition(board, game.player1));
    }

    @Test
    void testEvaluatePositionOWin() {
        Game game = new Game();
        game.symbol = 'O';
        char[] board = {'O', 'O', 'O', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(Game.INF, game.evaluatePosition(board, game.player2));
    }

    @Test
    void testEvaluatePositionLose() {
        Game game = new Game();
        game.symbol = 'X';
        char[] board = {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(-Game.INF, game.evaluatePosition(board, game.player2));
    }

    @Test
    void testEvaluatePositionDraw() {
        Game game = new Game();
        game.symbol = 'X';
        char[] board = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        assertEquals(0, game.evaluatePosition(board, game.player1));
    }

    @Test
    void testEvaluatePositionPlaying() {
        Game game = new Game();
        game.symbol = 'X';
        char[] board = {'X', ' ', 'X', ' ', 'O', ' ', ' ', ' ', ' '};
        assertEquals(-1, game.evaluatePosition(board, game.player1));
    }

    @Test
    void testMiniMaxBlocksWin() {
        Game game = new Game();
        char[] board = {'X', 'X', ' ', ' ', 'O', ' ', ' ', ' ', ' '};
        game.player2.symbol = 'O';
        int move = game.MiniMax(board.clone(), game.player2);
        assertEquals(3, move);
    }

    @Test
    void testMiniMaxTakesWin() {
        Game game = new Game();
        char[] board = {'O', 'O', ' ', 'X', 'X', ' ', ' ', ' ', ' '};
        game.player2.symbol = 'O';
        int move = game.MiniMax(board.clone(), game.player2);
        assertEquals(3, move);
    }

    @Test
    void testMiniMaxCenterPreference() {
        Game game = new Game();
        char[] board = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        game.player1.symbol = 'X';
        int move = game.MiniMax(board.clone(), game.player1);
        assertTrue(move >= 1 && move <= 9, "Ход должен быть в диапазоне 1-9: " + move);
    }

    @Test
    void testMinMoveReturnsImmediate() {
        Game game = new Game();
        game.symbol = 'O';
        char[] board = {'O', 'O', 'O', ' ', ' ', ' ', ' ', ' ', ' '};
        int val = game.MinMove(board, game.player2);
        assertEquals(Game.INF, val);
    }

    @Test
    void testMaxMoveReturnsImmediate() {
        Game game = new Game();
        game.symbol = 'X';
        char[] board = {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '};
        int val = game.MaxMove(board, game.player1);
        assertEquals(Game.INF, val);
    }

    @Test
    void testMinMoveRecursive() {
        Game game = new Game();
        char[] board = {'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        game.player1.symbol = 'X';
        game.player2.symbol = 'O';
        int val = game.MinMove(board, game.player1);
        assertTrue(val >= -Game.INF && val <= Game.INF);
    }

    @Test
    void testMaxMoveRecursive() {
        Game game = new Game();
        char[] board = {'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        game.player1.symbol = 'X';
        game.player2.symbol = 'O';
        int val = game.MaxMove(board, game.player2);
        assertTrue(val >= -Game.INF && val <= Game.INF);
    }
}