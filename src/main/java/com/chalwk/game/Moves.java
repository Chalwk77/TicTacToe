/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import static com.chalwk.game.GameOver.gameOver;
import static com.chalwk.game.Globals.cell_indicators;
import static com.chalwk.game.Globals.filler;
import static com.chalwk.game.board.getBoardEmbed;

public class Moves {

    /***
     * Checks if the move is allowed.
     * @param board The board being played.
     * @param event The event that triggered the move.
     * @param input The input from the event.
     * @return true if the move is allowed, false if not.
     */
    public static boolean moveAllowed(char[][] board, ButtonInteractionEvent event, String input, String whosTurn, String challengerName, String opponentName) {

        Member member = event.getMember();
        assert member != null;

        int[] cells = cell_indicators.get(input.toUpperCase());
        if (board[cells[0]][cells[1]] == filler) {
            return true;
        }

        EmbedBuilder currentBoard = getBoardEmbed(board, whosTurn, challengerName, opponentName, event);
        currentBoard.addField(whosTurn + ", that cell is already taken. Please select another cell.", "", true);
        event.editMessageEmbeds(currentBoard.build()).queue();

        return false;
    }

    /***
     * Places the move on the board.
     * @param board The current board
     * @param input The user input
     * @param symbol The symbol to place
     */
    public static void placeMove(char[][] board, String input, char symbol, String whosTurn, String challengerName, String opponentName, ButtonInteractionEvent event, String[] game) {

        int[] cells = cell_indicators.get(input.toUpperCase());
        int row = cells[0];
        int col = cells[1];

        board[row][col] = symbol;
        EmbedBuilder currentBoard = getBoardEmbed(board, whosTurn, challengerName, opponentName, event);
        currentBoard.addField(whosTurn + " selected " + input.toUpperCase(), "\n\n", true);

        event.editMessageEmbeds(currentBoard.build()).queue();

        gameOver(board, event, whosTurn, challengerName, opponentName);
    }
}
