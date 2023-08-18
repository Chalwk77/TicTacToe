/* Copyright (c) 2023, TicTacToe-JDA. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.*;

import static com.chalwk.Main.getBotAvatar;
import static com.chalwk.Main.getBotName;
import static com.chalwk.game.GameOver.gameOver;
import static com.chalwk.game.Globals.board_layout;
import static com.chalwk.game.Globals.concurrentGames;
import static com.chalwk.game.PrivateMessage.privateMessage;

public class Game {

    public static final char filler = '-';
    public static final String[] letters = {
            "A", "B", "C", "D",
            "E", "F", "G", "H",
            "I", "J", "K", "L",
            "M", "N", "O", "P",
            "Q", "R", "S", "T",
            "U", "V", "W", "X",
            "Y", "Z"
    };
    public final char player1 = 'X';
    public final char player2 = 'O';
    public String whos_turn;
    public String challengerID;
    public String opponentID;
    public String challengerName;
    public String opponentName;
    public boolean started = false;
    public char symbol;
    public int gameID;
    private Map<String, int[]> cell_indicators = new HashMap<>();
    private char[][] board;
    private Guild guild;

    public Game(OptionMapping boardSize, SlashCommandInteractionEvent event, String challengerID, String opponentID) {
        setGuild(event);
        setCompetitors(challengerID, opponentID);
        createBoard(boardSize);
    }

    private void setGuild(SlashCommandInteractionEvent event) {
        this.guild = event.getGuild();
    }

    private void setCompetitors(String challengerID, String opponentID) {
        this.challengerID = challengerID;
        this.opponentID = opponentID;
        this.challengerName = guild.getMemberById(challengerID).getEffectiveName();
        this.opponentName = guild.getMemberById(opponentID).getEffectiveName();
    }

    private void createBoard(OptionMapping boardSize) {
        this.cell_indicators = new HashMap<>();
        this.board = board_layout[boardSize.getAsInt()];
        String[] alphabet = Arrays.copyOfRange(letters, 0, board.length);
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                this.board[row][col] = filler;
                this.cell_indicators.put(alphabet[row] + (col + 1), new int[]{col, row});
            }
        }
    }

    public char[][] getBoard() {
        return board;
    }

    private String[] getLetters() {
        return letters;
    }

    private String buildBoard() {

        StringBuilder sb = new StringBuilder();

        int len = this.board.length;
        String[] alphabet = Arrays.copyOfRange(getLetters(), 0, len);

        sb.append("```");
        for (int i = 0; i < len; i++) {
            if (i == 0) {
                sb.append("    ");
                for (int j = 0; j < len; j++) {
                    sb.append(alphabet[j]).append("   ");
                }
            }
            sb.append("\n");
            sb.append(i + 1).append(" | ");
            for (int j = 0; j < len; j++) {
                sb.append(this.board[i][j]).append(" | ");
            }
            sb.append("\n");
            if (i != len - 1) {
                sb.append("  |");
                sb.append("---|".repeat(len));
            }
        }
        sb.append("```");

        return sb.toString();
    }

    private String printBoard() {
        int len = this.board.length;
        String err = "Board size not supported: (" + len + "x" + len + ")";
        if (len < 3 || len > 5) throw new IllegalStateException(err);
        return buildBoard();
    }

    public void showSubmission(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = getEmbedBuilder();
        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.success("accept", "\uD83D\uDFE2 Accept"));
        buttons.add(Button.danger("decline", "\uD83D\uDD34 Decline"));
        buttons.add(Button.secondary("cancel", "\uD83D\uDEAB Cancel"));
        event.replyEmbeds(embed.build()).addActionRow(buttons).queue();
    }

    private EmbedBuilder getEmbedBuilder() {

        String botName = getBotName();
        String botAvatar = getBotAvatar();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("⭕.❌ Tic-Tac-Toe ❌.⭕");
        embed.setDescription("You have been invited to play TicTacToe.");
        embed.addField("Challenger:", "<@" + this.challengerID + ">", true);
        embed.addField("Opponent:", "<@" + this.opponentID + ">", true);
        embed.addField("Board: (" + this.board.length + "x" + this.board.length + ")", printBoard(), false);
        embed.setFooter(botName + " - Copyright (c) 2023. Jericho Crosby", botAvatar);
        return embed;
    }

    public boolean moveAllowed(String buttonLabel) {
        int[] cells = this.cell_indicators.get(buttonLabel.toUpperCase());
        return cells != null && isCellEmpty(cells);
    }

    private boolean isCellEmpty(int[] cells) {
        return this.board[cells[0]][cells[1]] == filler;
    }

    public void placeMove(ButtonInteractionEvent event, String input) {

        Member member = event.getMember();
        String name = member.getEffectiveName();

        this.setSymbol(member.getId()); // set the symbol for the player (X or O)

        int[] cells = this.cell_indicators.get(input.toUpperCase());
        int row = cells[0];
        int col = cells[1];

        this.setTurn();
        this.board[row][col] = this.symbol;

        EmbedBuilder embed = getEmbed();
        embed.addField(name + " selected " + input.toUpperCase(), "\n\n", true);
        embed.setDescription("It is now " + this.whos_turn + "'s turn.");
        event.editMessageEmbeds(embed.build()).queue();

        gameOver(this, event);
    }

    public EmbedBuilder getEmbed() {

        String botName = getBotName();
        String botAvatar = getBotAvatar();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("⭕.❌ Tic-Tac-Toe ❌.⭕");
        embed.addField("Challenger:", "<@" + this.challengerID + ">", true);
        embed.addField("Opponent:", "<@" + this.opponentID + ">", true);
        embed.addField("Board: (" + this.board.length + "x" + this.board.length + ")", printBoard(), false);
        embed.setFooter(botName + " - Copyright (c) 2023. Jericho Crosby", botAvatar);
        return embed;
    }

    private String whoStarts() {
        Random random = new Random();
        int randomNum = random.nextInt(2);
        return (randomNum == 0) ? this.challengerName : this.opponentName;
    }

    public void setTurn() {
        this.whos_turn = (this.whos_turn.equals(this.challengerName)) ? this.opponentName : this.challengerName;
    }

    public void setSymbol(String playerID) {
        this.symbol = (playerID.equals(challengerID)) ? this.player2 : this.player1;
    }

    private void initializeGame(ButtonInteractionEvent event) {

        int boardLength = this.board.length;
        this.started = true;
        this.whos_turn = whoStarts();

        EmbedBuilder embed = getEmbedBuilder();
        embed.setDescription("The game has started. " + this.whos_turn + " goes first.");

        List<Button> buttons = new ArrayList<>();
        for (int row = 0; row < boardLength; row++) {
            for (int col = 0; col < boardLength; col++) {
                String letter = letters[col];
                String buttonLabel = letter + (row + 1);
                buttons.add(Button.primary(buttonLabel, buttonLabel));
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

    void acceptInvitation(ButtonInteractionEvent event) {
        event.getMessage().delete().queue();
        initializeGame(event);
    }

    void declineInvitation(ButtonInteractionEvent event, Member member) {
        privateMessage(event, member, "Your game invite to " + this.opponentName + " was declined.");
        event.getMessage().delete().queue();
        concurrentGames[this.gameID] = null;
    }

    void cancelInvitation(ButtonInteractionEvent event, Member member) {
        privateMessage(event, member, "Your game invite to " + this.opponentName + " was cancelled.");
        event.getMessage().delete().queue();
        concurrentGames[this.gameID] = null;
    }

    public int getGameID() {
        return this.gameID;
    }
}
