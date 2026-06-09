package com.induce;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

enum State { PLAYING, OWIN, XWIN, DRAW }

class Player {
    public char mark;
    public int move;
    public boolean isPicked;
    public boolean victory;
}

class Game {
    public static final int INF = 100;
    private static final int[][] WIN_LINES = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
        {0, 4, 8}, {2, 4, 6}
    };

    public State state;
    public Player player1;
    public Player player2;
    public Player currentPlayer;
    public int nmove;
    public char mark;
    public int q;
    public char[] grid;

    public Game() {
        player1 = new Player();
        player2 = new Player();
        player1.mark = 'X';
        player2.mark = 'O';
        currentPlayer = player1;
        state = State.PLAYING;
        grid = new char[9];
        Arrays.fill(grid, ' ');
    }

    public State determineStatus(char[] position) {
        for (int[] line : WIN_LINES) {
            char cellMark = position[line[0]];
            if (cellMark != ' ' && cellMark == position[line[1]]
                    && cellMark == position[line[2]]) {
                return cellMark == 'X' ? State.XWIN : State.OWIN;
            }
        }

        return hasFreeSpace(position) ? State.PLAYING : State.DRAW;
    }

    void generateMoves(char[] position, ArrayList<Integer> moveList) {
        moveList.clear();
        for (int idx = 0; idx < position.length; idx++) {
            if (position[idx] == ' ') {
                moveList.add(idx);
            }
        }
    }

    int evaluatePosition(char[] position, Player player) {
        State outcome = determineStatus(position);
        if (outcome == State.DRAW) {
            return 0;
        }
        if (outcome == State.PLAYING) {
            return -1;
        }

        char winningMark = outcome == State.XWIN ? 'X' : 'O';
        return winningMark == player.mark ? INF : -INF;
    }

    int MiniMax(char[] position, Player player) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        generateMoves(position, possibleMoves);
        if (possibleMoves.isEmpty()) {
            return 0;
        }

        int optimalMove = possibleMoves.get(0);
        int optimalScore = -INF * 10;
        q = 0;

        for (int move : possibleMoves) {
            position[move] = player.mark;
            int score = minimax(position, getOpposite(player.mark), player.mark, 1);
            position[move] = ' ';

            if (score > optimalScore) {
                optimalScore = score;
                optimalMove = move;
            }
        }

        return optimalMove + 1;
    }

    int MinMove(char[] position, Player player) {
        return minimax(position, getOpposite(player.mark), player.mark, 0);
    }

    int MaxMove(char[] position, Player player) {
        return minimax(position, player.mark, player.mark, 0);
    }

    private int minimax(char[] position, char currentTurn, char maximizingPlayer, int depth) {
        q++;
        State outcome = determineStatus(position);
        if (outcome != State.PLAYING) {
            return getTerminalScore(outcome, maximizingPlayer, depth);
        }

        boolean isMaximizing = currentTurn == maximizingPlayer;
        int best = isMaximizing ? -INF * 10 : INF * 10;

        for (int idx = 0; idx < position.length; idx++) {
            if (position[idx] != ' ') {
                continue;
            }

            position[idx] = currentTurn;
            int score = minimax(position, getOpposite(currentTurn), maximizingPlayer, depth + 1);
            position[idx] = ' ';
            best = isMaximizing ? Math.max(best, score) : Math.min(best, score);
        }

        return best;
    }

    private int getTerminalScore(State outcome, char maximizingPlayer, int depth) {
        if (outcome == State.DRAW) {
            return 0;
        }

        char winningMark = outcome == State.XWIN ? 'X' : 'O';
        return winningMark == maximizingPlayer ? INF - depth : depth - INF;
    }

    private boolean hasFreeSpace(char[] position) {
        for (char cell : position) {
            if (cell == ' ') {
                return true;
            }
        }
        return false;
    }

    private char getOpposite(char marker) {
        return marker == 'X' ? 'O' : 'X';
    }
}

public class Program {
    public static void main(String[] args) {
        JFrame window = new JFrame("Crosses and Zeros");
        window.add(new TicTacToePanel(new GridLayout(3, 3)));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(5, 5, 500, 500);
        window.setVisible(true);
    }
}

class TicTacToeCell extends JButton {
    private final int index;
    private final int rowPos;
    private final int colPos;
    private char symbol;

    TicTacToeCell(int num, int x, int y) {
        this.index = num;
        this.rowPos = y;
        this.colPos = x;
        this.symbol = ' ';
        setText(Character.toString(symbol));
        setFont(new Font("Arial", Font.PLAIN, 40));
    }

    public void setMarker(String markerText) {
        symbol = markerText.charAt(0);
        setText(markerText);
        setEnabled(false);
    }

    public char getMarker() {
        return symbol;
    }

    public int getRow() {
        return rowPos;
    }

    public int getCol() {
        return colPos;
    }

    public int getNum() {
        return index;
    }
}

class Utility {
    public static void print(char[] data) {
        displayLine(convertToObjects(data));
    }

    public static void print(int[] data) {
        displayLine(convertToObjects(data));
    }

    public static void print(ArrayList<Integer> moves) {
        displayLine(moves.toArray());
    }

    private static Object[] convertToObjects(char[] values) {
        Object[] result = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i];
        }
        return result;
    }

    private static Object[] convertToObjects(int[] values) {
        Object[] result = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i];
        }
        return result;
    }

    private static void displayLine(Object[] items) {
        System.out.println();
        for (Object item : items) {
            System.out.print(item + "-");
        }
        System.out.println();
    }
}

class TicTacToePanel extends JPanel implements ActionListener {
    private Game gameLogic;
    private TicTacToeCell[] boardCells = new TicTacToeCell[9];

    TicTacToePanel(GridLayout layout) {
        super(layout);
        buildBoard();
        gameLogic = new Game();
    }

    private void buildBoard() {
        for (int i = 0; i < boardCells.length; i++) {
            buildCell(i, i % 3, i / 3);
        }
    }

    private void buildCell(int idx, int col, int row) {
        boardCells[idx] = new TicTacToeCell(idx, col, row);
        boardCells[idx].addActionListener(this);
        add(boardCells[idx]);
    }

    public void actionPerformed(ActionEvent event) {
        TicTacToeCell clicked = (TicTacToeCell) event.getSource();
        if (clicked.getMarker() != ' ' || gameLogic.state != State.PLAYING) {
            return;
        }

        placeMark(clicked, gameLogic.player1.mark);
        gameLogic.state = gameLogic.determineStatus(gameLogic.grid);

        if (gameLogic.state == State.PLAYING) {
            int aiChoice = gameLogic.MiniMax(gameLogic.grid, gameLogic.player2);
            if (aiChoice > 0) {
                gameLogic.nmove = aiChoice;
                placeMark(boardCells[aiChoice - 1], gameLogic.player2.mark);
                gameLogic.state = gameLogic.determineStatus(gameLogic.grid);
            }
        }

        if (gameLogic.state != State.PLAYING) {
            showGameResult(gameLogic.state);
        }
    }

    protected void showGameResult(State finalState) {
        String resultMessage;
        switch (finalState) {
            case XWIN:
                resultMessage = "X wins!";
                break;
            case OWIN:
                resultMessage = "O wins!";
                break;
            case DRAW:
                resultMessage = "It's a tie!";
                break;
            default:
                resultMessage = "Игра продолжается";
                break;
        }
        JOptionPane.showMessageDialog(null, resultMessage, "Результат",
                JOptionPane.WARNING_MESSAGE);
    }

    private void placeMark(TicTacToeCell cell, char markSymbol) {
        cell.setMarker(Character.toString(markSymbol));
        gameLogic.grid[cell.getNum()] = markSymbol;
    }
}
