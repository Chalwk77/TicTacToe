/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Map;

import static com.chalwk.game.Game.cell_indicators;
import static com.chalwk.game.Game.filler;
import static com.chalwk.game.board.getBoard;

public class Moves {

    /***
     * Checks if the move is allowed.
     * @param board The board being played.
     * @param event The event that triggered the move.
     * @param input The input from the event.
     * @return true if the move is allowed, false if not.
     */
    public static boolean moveAllowed(char[][] board, MessageReceivedEvent event, String input) {

        Member member = event.getMember();
        assert member != null;

        int[] cells = cell_indicators.get(input.toUpperCase());
        if (cells == null) {
            event.getChannel().sendMessage(member.getAsMention() + " " + input.toUpperCase() + " is an invalid move.").queue();
            return false;
        }

        int row = cells[0];
        int col = cells[1];
        if (board[row][col] == filler) {
            return true;
        }
        event.getChannel().sendMessage(member.getAsMention() + " Slot taken by [" + board[row][col] + "]. Please try again.").queue();

        return false;
    }

    /***
     * Places the move on the board.
     * @param board The current board
     * @param input The user input
     * @param symbol The symbol to place
     */
    public static void placeMove(char[][] board, String input, char symbol, String whosTurn, String inviteeName, String opponentName, MessageReceivedEvent event, String[] game) {

        int[] cells = cell_indicators.get(input.toUpperCase());
        int row = cells[0];
        int col = cells[1];

        board[row][col] = symbol;
        EmbedBuilder currentBoard = getBoard(board, whosTurn, inviteeName, opponentName);

        String embedID = game[2];

        currentBoard.setFooter("Tic-Tac-Toe", event.getJDA().getSelfUser().getAvatarUrl());
        event.getChannel().editMessageEmbedsById(embedID, currentBoard.build()).queue();
    }

    public static String showValidMoves(char[][] board) {

        StringBuilder moves = new StringBuilder();
        int count = 0;

        for (Map.Entry<String, int[]> entry : cell_indicators.entrySet()) {
            String pos = entry.getKey();

            int[] rowCol = entry.getValue();
            int row = rowCol[0];
            int col = rowCol[1];

            count++;
            if (count % 5 == 0) {
                moves.append("\n");
            } else if (board[row][col] == filler) {
                moves.append(pos).append(", ");
            }
        }

        return moves.toString();
    }
}
