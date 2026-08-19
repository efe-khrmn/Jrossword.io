package com.texteditor.jrosswordio;

import java.util.ArrayList;
import java.util.List;

public final class TestPuzzle {

    public static final int SIZE = 5;

    private static final String[] SOLUTION = {
            "CRANE",
            "HOUSE",
            "ARENA",
            "IDEAL",
            "RESET"
    };

    private static final int[][] NUMBERS = {
            {1, 2, 3, 4, 5},
            {6, 0, 0, 0, 0},
            {7, 0, 0, 0, 0},
            {8, 0, 0, 0, 0},
            {9, 0, 0, 0, 0}
    };

    public static final class Clue {
        public final int number;
        public final String text;

        public Clue(int number, String text) {
            this.number = number;
            this.text = text;
        }

        @Override
        public String toString() {
            return number + ". " + text;
        }
    }

    private TestPuzzle() {
    }

    public static char solutionAt(int row, int col) {
        return SOLUTION[row].charAt(col);
    }

    public static boolean isBlocked(int row, int col) {
        return solutionAt(row, col) == '#';
    }

    public static int numberAt(int row, int col) {
        return NUMBERS[row][col];
    }

    public static List<Clue> acrossClues() {
        List<Clue> clues = new ArrayList<>();
        clues.add(new Clue(1, "Long-necked bird"));
        clues.add(new Clue(6, "Where you live"));
        clues.add(new Clue(7, "Sports venue"));
        clues.add(new Clue(8, "Perfect"));
        clues.add(new Clue(9, "Start over"));
        return clues;
    }

    public static List<Clue> downClues() {
        List<Clue> clues = new ArrayList<>();
        clues.add(new Clue(1, "Seat for a monarch's guest? Sit on this"));
        clues.add(new Clue(2, "Random"));
        clues.add(new Clue(3, "Random"));
        clues.add(new Clue(4, "Random"));
        clues.add(new Clue(5, "Random"));
        return clues;
    }
}
