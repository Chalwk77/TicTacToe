/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import static com.chalwk.game.Globals.WINNING_COMBINATIONS;

public class GameOver {

    private static void endGame(Game game, int state, ButtonInteractionEvent event) {
        Member member = event.getMember();
        assert member != null;

        EmbedBuilder currentBoard = game.getBoardEmbed();
        if (state == 0) {
            currentBoard.setDescription("Game Over! It's a draw!");
        } else {

            String name = member.getEffectiveName();
            currentBoard.setDescription("Game Over! " + name + " has won the game!");
        }
        event.getHook().deleteOriginal().queue();
        event.getHook().sendMessageEmbeds(currentBoard.build()).queue();
    }

    public static boolean gameOver(Game game, ButtonInteractionEvent event) {

        char[][] board = game.getBoard();

        int count = 0;
        for (char[] chars : board) {
            for (char aChar : chars) {
                if (aChar != Game.filler) {
                    count++;
                }
            }
        }

        if (count == board.length * board.length) {
            endGame(game, 0, event);
            return true;
        } else if (checkWinner(board, game.player1) || checkWinner(board, game.player2)) {
            endGame(game, 1, event);
            return true;
        }

        return false;
    }

    private static boolean checkWinner(char[][] board, char symbol) {
        int len = board.length;
        int[][][] winningCombinations = new int[][][]{WINNING_COMBINATIONS[len - 3]};
        for (int[][] winningCombination : winningCombinations) {
            for (int[] combination : winningCombination) {
                for (int i = 0; i < combination.length; i++) {
                    int x = combination[i];
                    if (board[x / len][x % len] != symbol) {
                        break;
                    }
                    if (i == combination.length - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
