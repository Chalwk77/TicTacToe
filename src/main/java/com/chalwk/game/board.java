/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.chalwk.Main.getBotAvatar;
import static com.chalwk.Main.getBotName;
import static com.chalwk.game.Globals.*;

public class board {

    /***
     * Create a new board.
     * @param boardSize The size of the board (3x3, 4x4, 5x5).
     * @return The new board.
     */
    public static char[][] newBoard(OptionMapping boardSize) {
        char[][] board = boards[boardSize.getAsInt()];
        String[] alphabet = Arrays.copyOfRange(letters, 0, board.length);
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                board[row][col] = filler;
                cell_indicators.put(alphabet[row] + (col + 1), new int[]{col, row});
            }
        }
        return board;
    }

    /***
     * Get the board embed object.
     * @param board The board to print.
     * @param whosTurn The name of the current player's turn.
     * @param challengerName The name of the challenger.
     * @param opponentName The name of the opponent.
     * @param event The event object that triggered the board (button click).
     * @return The board embed object.
     */
    public static EmbedBuilder getBoardEmbed(char[][] board, String whosTurn, String challengerName, String opponentName, ButtonInteractionEvent event) {

        String botName = getBotName();
        String botAvatar = getBotAvatar();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("⭕.❌ Tic-Tac-Toe ❌.⭕\n\n" + challengerName + "  vs  " + opponentName);
        embed.addField("Board:", "```" + printBoard(board) + "```", false);
        embed.addField("It's now " + whosTurn + "'s turn.", "", false);

        embed.setFooter(botName + " - Copyright (c) 2023. Jericho Crosby", botAvatar);
        return embed;
    }

    /***
     * Print the board.
     * @param board The board to print.
     * @return Neatly formatted board.
     */
    public static String printBoard(char[][] board) {
        int len = board.length;
        if (len < 3 || len > 5) throw new IllegalStateException("Board size not supported: (" + len + "x" + len + ")");
        return buildBoard(board);
    }

    /***
     *
     * @param board The board to print.
     * @return Neatly formatted board.
     */
    private static String buildBoard(char[][] board) {

        StringBuilder sb = new StringBuilder();

        int len = board.length;
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
                sb.append(board[i][j]).append(" | ");
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

    /***
     * Set up the buttons for the board.
     * @param board The board to set up.
     * @param boardEmbed The board embed object.
     * @param event The event object that triggered the board (button click).
     */
    public static void setupButtons(char[][] board, EmbedBuilder boardEmbed, ButtonInteractionEvent event) {

        List<Button> buttons = new ArrayList<>();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                int id = (row * board.length) + col;
                buttons.add(Button.secondary(String.valueOf(id), letters[row] + (col + 1)));
            }
        }

        switch (board.length) {
            case 3 -> {
                List<Button> row1 = buttons.subList(0, 3);
                List<Button> row2 = buttons.subList(3, 6);
                List<Button> row3 = buttons.subList(6, 9);
                event.replyEmbeds(boardEmbed.build())
                        .addActionRow(row1)
                        .addActionRow(row2)
                        .addActionRow(row3).queue();
            }
            case 4 -> {
                List<Button> row1 = buttons.subList(0, 4);
                List<Button> row2 = buttons.subList(4, 8);
                List<Button> row3 = buttons.subList(8, 12);
                List<Button> row4 = buttons.subList(12, 16);
                event.replyEmbeds(boardEmbed.build())
                        .addActionRow(row1)
                        .addActionRow(row2)
                        .addActionRow(row3)
                        .addActionRow(row4).queue();
            }
            case 5 -> {
                List<Button> row1 = buttons.subList(0, 5);
                List<Button> row2 = buttons.subList(5, 10);
                List<Button> row3 = buttons.subList(10, 15);
                List<Button> row4 = buttons.subList(15, 20);
                List<Button> row5 = buttons.subList(20, 25);
                event.replyEmbeds(boardEmbed.build())
                        .addActionRow(row1)
                        .addActionRow(row2)
                        .addActionRow(row3)
                        .addActionRow(row4)
                        .addActionRow(row5).queue();
            }
        }
    }
}
