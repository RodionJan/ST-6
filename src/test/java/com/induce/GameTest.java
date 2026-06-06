package com.induce;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class GameTest {
    private Game game;

    @Before
    public void setUp() {
        System.setProperty("java.awt.headless", "true");
        game = new Game();
    }

    @Test
    public void constructorCreatesEmptyPlayingBoard() {
        Assert.assertEquals(State.PLAYING, game.state);
        Assert.assertEquals('X', game.player1.mark);
        Assert.assertEquals('O', game.player2.mark);
        for (char cell : game.grid) {
            Assert.assertEquals(' ', cell);
        }
    }

    @Test
    public void checkStateFindsTopRowXWin() {
        char[] grid = {'X', 'X', 'X', ' ', 'O', ' ', ' ', ' ', 'O'};
        Assert.assertEquals(State.XWIN, game.determineStatus(grid));
    }

    @Test
    public void checkStateFindsMiddleRowOWin() {
        char[] grid = {'X', ' ', 'X', 'O', 'O', 'O', ' ', ' ', ' '};
        Assert.assertEquals(State.OWIN, game.determineStatus(grid));
    }

    @Test
    public void checkStateFindsColumnWin() {
        char[] grid = {'O', 'X', ' ', 'O', 'X', ' ', 'O', ' ', 'X'};
        Assert.assertEquals(State.OWIN, game.determineStatus(grid));
    }

    @Test
    public void checkStateFindsMainDiagonalWin() {
        char[] grid = {'X', 'O', ' ', ' ', 'X', 'O', ' ', ' ', 'X'};
        Assert.assertEquals(State.XWIN, game.determineStatus(grid));
    }

    @Test
    public void checkStateFindsAntiDiagonalWin() {
        char[] grid = {'X', ' ', 'O', 'X', 'O', ' ', 'O', ' ', 'X'};
        Assert.assertEquals(State.OWIN, game.determineStatus(grid));
    }

    @Test
    public void checkStateReportsDrawForFullBoardWithoutWinner() {
        char[] grid = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        Assert.assertEquals(State.DRAW, game.determineStatus(grid));
    }

    @Test
    public void checkStateReportsPlayingWhenMovesRemain() {
        char[] grid = {'X', 'O', 'X', ' ', 'O', ' ', ' ', 'X', ' '};
        Assert.assertEquals(State.PLAYING, game.determineStatus(grid));
    }

    @Test
    public void generateMovesReturnsEmptyCellsInOrder() {
        char[] grid = {'X', ' ', 'O', ' ', 'X', ' ', 'O', ' ', 'X'};
        ArrayList<Integer> moves = new ArrayList<>();

        game.generateMoves(grid, moves);

        Assert.assertEquals(Arrays.asList(1, 3, 5, 7), moves);
    }

    @Test
    public void generateMovesClearsPreviousContent() {
        ArrayList<Integer> moves = new ArrayList<>();
        moves.add(99);

        game.generateMoves(new char[] {'X', 'O', 'X', 'O', 'X', 'O', 'X', 'O', 'X'}, moves);

        Assert.assertTrue(moves.isEmpty());
    }

    @Test
    public void evaluatePositionRewardsOwnWin() {
        Player player = new Player();
        player.mark = 'X';
        char[] grid = {'X', 'X', 'X', ' ', 'O', ' ', ' ', ' ', 'O'};

        Assert.assertEquals(Game.INF, game.evaluatePosition(grid, player));
    }

    @Test
    public void evaluatePositionPenalizesOpponentWin() {
        Player player = new Player();
        player.mark = 'O';
        char[] grid = {'X', 'X', 'X', ' ', 'O', ' ', ' ', ' ', 'O'};

        Assert.assertEquals(-Game.INF, game.evaluatePosition(grid, player));
    }

    @Test
    public void evaluatePositionReturnsZeroForDraw() {
        Player player = new Player();
        player.mark = 'O';
        char[] grid = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};

        Assert.assertEquals(0, game.evaluatePosition(grid, player));
    }

    @Test
    public void evaluatePositionReturnsMinusOneForUnfinishedGame() {
        Player player = new Player();
        player.mark = 'X';
        char[] grid = {'X', ' ', ' ', ' ', 'O', ' ', ' ', ' ', ' '};

        Assert.assertEquals(-1, game.evaluatePosition(grid, player));
    }

    @Test
    public void minimaxTakesImmediateWinningMove() {
        char[] grid = {'O', 'O', ' ', 'X', 'X', ' ', ' ', ' ', ' '};
        int move = game.MiniMax(grid, game.player2);

        Assert.assertEquals(3, move);
    }

    @Test
    public void minimaxBlocksOpponentImmediateWin() {
        char[] grid = {'X', 'X', ' ', ' ', 'O', ' ', ' ', ' ', ' '};
        int move = game.MiniMax(grid, game.player2);

        Assert.assertEquals(3, move);
    }

    @Test
    public void minimaxUsesLastAvailableCell() {
        char[] grid = {'X', 'O', 'X', 'O', 'X', ' ', 'O', 'X', 'O'};
        int move = game.MiniMax(grid, game.player2);

        Assert.assertEquals(6, move);
    }

    @Test
    public void minimaxReturnsZeroForFullBoard() {
        char[] grid = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        Assert.assertEquals(0, game.MiniMax(grid, game.player2));
    }

    @Test
    public void minMoveSeesOpponentWinAsBad() {
        char[] grid = {'X', 'X', 'X', ' ', 'O', ' ', ' ', ' ', 'O'};
        Assert.assertTrue(game.MinMove(grid, game.player2) < 0);
    }

    @Test
    public void maxMoveSeesOwnWinAsGood() {
        char[] grid = {'O', 'O', 'O', 'X', 'X', ' ', ' ', ' ', ' '};
        Assert.assertTrue(game.MaxMove(grid, game.player2) > 0);
    }

    @Test
    public void playerDefaultFlagsAreFalse() {
        Player player = new Player();
        Assert.assertFalse(player.isPicked);
        Assert.assertFalse(player.victory);
    }
}
