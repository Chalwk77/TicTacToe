/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.game;

public class Globals {

    public static Game[] concurrentGames = new Game[0];
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
    public static char[][][] board_layout = {

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

    public static int getGameCount() {
        return concurrentGames.length;
    }
}
