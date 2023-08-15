/* Copyright (c) 2023, TicTacToe-JDA. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.chalwk.game.Globals.board_layout;

public class NewGame {

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
    public String challengerID;
    public String opponentID;
    public String challengerName;
    public String opponentName;
    public Map<String, int[]> cell_indicators;
    private Guild guild;
    private char[][] board;

    public NewGame(OptionMapping boardSize, SlashCommandInteractionEvent event, String challengerID, String opponentID) {
        setGuild(event);
        setPlayers(challengerID, opponentID);
        createBoard(boardSize);
    }

    private void setGuild(SlashCommandInteractionEvent event) {
        this.guild = event.getGuild();
    }

    private void setPlayers(String challengerID, String opponentID) {
        this.challengerID = challengerID;
        this.opponentID = opponentID;
        this.challengerName = guild.getMemberById(challengerID).getEffectiveName();
        this.opponentName = guild.getMemberById(opponentID).getEffectiveName();
    }

    private void setCellIndicators() {
        Map<String, int[]> cells = new HashMap<>();
        String[] alphabet = Arrays.copyOfRange(letters, 0, board.length);
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                cells.put(alphabet[row] + (col + 1), new int[]{col, row});
            }
        }
        this.cell_indicators = cells;
    }

    private void createBoard(OptionMapping boardSize) {
        this.board = board_layout[boardSize.getAsInt()];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                this.board[row][col] = filler;
            }
        }
        setCellIndicators();
    }

    public char[][] getBoard() {
        return this.board;
    }

    private String[] getLetters() {
        return letters;
    }

    private String buildBoard() {

        StringBuilder sb = new StringBuilder();

        int len = this.getBoard().length;
        String[] alphabet = Arrays.copyOfRange(getLetters(), 0, len);

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
                sb.append(this.getBoard()[i][j]).append(" | ");
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

    private String printBoard() {
        int len = this.getBoard().length;
        if (len < 3 || len > 5) throw new IllegalStateException("Board size not supported: (" + len + "x" + len + ")");
        return buildBoard();
    }

    public void showSubmission(SlashCommandInteractionEvent event) {

        int boardLength = this.getBoard().length;
        String botName = event.getJDA().getSelfUser().getName() + " - Copyright (c) 2023. Jericho Crosby";

        EmbedBuilder embed = getEmbedBuilder(event, boardLength, botName);

        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.success("accept", "\uD83D\uDFE2 Accept"));
        buttons.add(Button.danger("decline", "\uD83D\uDD34 Decline"));
        buttons.add(Button.secondary("cancel", "\uD83D\uDEAB Cancel"));

        event.replyEmbeds(embed.build()).addActionRow(buttons).queue();
    }

    @NotNull
    private EmbedBuilder getEmbedBuilder(SlashCommandInteractionEvent event, int boardLength, String botName) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("⭕.❌ Tic-Tac-Toe ❌.⭕");
        embed.setDescription("You have been invited to play TicTacToe.");
        embed.addField("Challenger:", "<@" + this.challengerID + ">", true);
        embed.addField("Opponent:", "<@" + this.opponentID + ">", true);
        embed.addField("A random player will be selected to go first.", "", false);
        embed.addField("Board: (" + boardLength + "x" + boardLength + ")", "```" + printBoard() + "```", false);
        embed.setFooter(botName, event.getJDA().getSelfUser().getAvatarUrl());
        return embed;
    }

    private void setupButtons(EmbedBuilder embed, ButtonInteractionEvent event) {

        int boardLength = this.getBoard().length;
        List<Button> buttons = new ArrayList<>();
        for (int row = 0; row < boardLength; row++) {
            for (int col = 0; col < boardLength; col++) {
                int id = (row * boardLength) + col;
                buttons.add(Button.secondary(String.valueOf(id), getLetters()[row] + (col + 1)));
            }
        }

        switch (boardLength) {
            case 3 -> {
                List<Button> row1 = buttons.subList(0, 3);
                List<Button> row2 = buttons.subList(3, 6);
                List<Button> row3 = buttons.subList(6, 9);
                event.replyEmbeds(embed.build())
                        .addActionRow(row1)
                        .addActionRow(row2)
                        .addActionRow(row3).queue();
            }
            case 4 -> {
                List<Button> row1 = buttons.subList(0, 4);
                List<Button> row2 = buttons.subList(4, 8);
                List<Button> row3 = buttons.subList(8, 12);
                List<Button> row4 = buttons.subList(12, 16);
                event.replyEmbeds(embed.build())
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
                event.replyEmbeds(embed.build())
                        .addActionRow(row1)
                        .addActionRow(row2)
                        .addActionRow(row3)
                        .addActionRow(row4)
                        .addActionRow(row5).queue();
            }
        }
    }
}
