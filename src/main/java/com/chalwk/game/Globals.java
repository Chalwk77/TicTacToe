/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.game;

import java.util.HashMap;
import java.util.Map;

public class Globals {

    public static Map<String, String[]> games = new HashMap<>();
    public static char player1 = 'X';
    public static char player2 = 'O';
    public static char filler = '-';
    public static String[] letters = {
            "A", "B", "C", "D",
            "E", "F", "G", "H",
            "I", "J", "K", "L",
            "M", "N", "O", "P",
            "Q", "R", "S", "T",
            "U", "V", "W", "X",
            "Y", "Z"
    };
    public static Map<String, int[]> cell_indicators = new HashMap<>();
    public static int[][][] WINNING_COMBINATIONS = {

            // 3x3:
            {
                    {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // rows

                    {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // columns

                    {0, 4, 8}, {2, 4, 6} // diagonals
            },

            //4x4:
            {
                    {0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}, // rows

                    {0, 4, 8, 12}, {1, 5, 9, 13}, {2, 6, 10, 14}, {3, 7, 11, 15}, // columns

                    {0, 5, 10, 15}, {3, 6, 9, 12} // diagonals
            },

            //5x5:
            {
                    {0, 1, 2, 3, 4}, {5, 6, 7, 8, 9}, {10, 11, 12, 13, 14}, {15, 16, 17, 18, 19}, {20, 21, 22, 23, 24}, // rows

                    {0, 5, 10, 15, 20}, {1, 6, 11, 16, 21}, {2, 7, 12, 17, 22}, {3, 8, 13, 18, 23}, {4, 9, 14, 19, 24}, // columns

                    {0, 6, 12, 18, 24}, {4, 8, 12, 16, 20} // diagonals
            }
    };
    public static char[][][] boards = {

            // 3x3
            {
                    {' ', ' ', ' '},
                    {' ', ' ', ' '},
                    {' ', ' ', ' '}
            },

            // 4x4
            {
                    {' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' '}
            },

            // 5x5
            {
                    {' ', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' ', ' '}
            }
    };
}
