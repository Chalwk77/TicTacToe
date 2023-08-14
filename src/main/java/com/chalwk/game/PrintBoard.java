/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

public class PrintBoard {

    /***
     * Board printer.
     */
    public static String printBoard(char[][] board) {
        int len = Game.board.length;
        switch (len) {
            case 3 -> {
                return board1(board);
            }
            case 4 -> {
                return board2(board);
            }
            case 5 -> {
                return board3(board);
            }
            default -> throw new IllegalStateException("Board size not supported: (" + len + "x" + len + ")");
        }
    }

    private static String board1(char[][] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                sb.append("    A   B   C\n");
            }
            sb.append(i + 1).append(" | ");
            for (int j = 0; j < 3; j++) {
                sb.append(b[i][j]).append(" | ");
            }
            sb.append("\n");
            if (i != 2) {
                sb.append("  |---+---+---|\n");
            }
        }
        return sb.toString();
    }

    private static String board2(char[][] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                sb.append("    A   B   C   D\n");
            }
            sb.append(i + 1).append(" | ");
            for (int j = 0; j < 4; j++) {
                sb.append(b[i][j]).append(" | ");
            }
            sb.append("\n");
            if (i != 3) {
                sb.append("  |---+---+---+---|\n");
            }
        }
        return sb.toString();
    }

    private static String board3(char[][] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                sb.append("    A   B   C   D   E\n");
            }
            sb.append(i + 1).append(" | ");
            for (int j = 0; j < 5; j++) {
                sb.append(b[i][j]).append(" | ");
            }
            sb.append("\n");
            if (i != 4) {
                sb.append("  |---+---+---+---+---|\n");
            }
        }
        return sb.toString();
    }
}
