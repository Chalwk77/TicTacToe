/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import java.util.Arrays;

import static com.chalwk.game.Game.letters;

public class PrintBoard {

    /***
     * Print the board.
     * @param board The board to print.
     * @return Neatly formatted board.
     */
    public static String printBoard(char[][] board) {
        int len = Game.board.length;
        if (len < 3 || len > 5) throw new IllegalStateException("Board size not supported: (" + len + "x" + len + ")");
        return getBoard(board);
    }

    private static String getBoard(char[][] b) {

        StringBuilder sb = new StringBuilder();

        int len = b.length;
        letters = Arrays.copyOfRange(new String[]{
                "A", "B", "C", "D",
                "E", "F", "G", "H",
                "I", "J", "K", "L",
                "M", "N", "O", "P",
                "Q", "R", "S", "T",
                "U", "V", "W", "X",
                "Y", "Z"
        }, 0, len);

        for (int i = 0; i < len; i++) {

            // Print the letters at the top of the board.
            if (i == 0) {
                sb.append("    ");
                for (int j = 0; j < len; j++) {
                    sb.append(letters[j]).append("   ");
                }
            }
            sb.append("\n");

            // Print the numbers on the left side of the board.
            sb.append(i + 1).append(" | ");

            // Print the board.
            for (int j = 0; j < len; j++) {
                sb.append(b[i][j]).append(" | ");
            }
            sb.append("\n");

            // Print the bottom of the board.
            if (i != len - 1) {
                sb.append("  |");
                sb.append("---|".repeat(len));
            }
        }

        return sb.toString();
    }
}
