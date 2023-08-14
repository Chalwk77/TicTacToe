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
        return buildBoard(board);
    }

    private static String buildBoard(char[][] b) {

        StringBuilder sb = new StringBuilder();

        int len = b.length;
        String[] alphabet = Arrays.copyOfRange(letters, 0, len);

        for (int i = 0; i < len; i++) {

            // Print the letters at the top of the board.
            if (i == 0) {
                sb.append("    ");
                for (int j = 0; j < len; j++) {
                    sb.append(alphabet[j]).append("   ");
                }
            }
            sb.append("\n");

            // Print the middle of the board and numbers on the left side.
            sb.append(i + 1).append(" | ");
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
