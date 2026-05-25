package com.mycompany.app;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;

class UtilityTest {

    @Test
    void testPrintCharArray() {
        char[] board = {'X', 'O', 'X', ' ', ' ', ' ', 'O', ' ', 'X'};
        Utility.print(board);
    }

    @Test
    void testPrintIntArray() {
        int[] board = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Utility.print(board);
    }

    @Test
    void testPrintArrayList() {
        ArrayList<Integer> moves = new ArrayList<>(Arrays.asList(0, 4, 8));
        Utility.print(moves);
    }

    @Test
    void testPrintEmptyArrayList() {
        ArrayList<Integer> moves = new ArrayList<>();
        Utility.print(moves);
    }
}