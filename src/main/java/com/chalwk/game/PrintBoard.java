/* Copyright (c) 2023, TicTacToe-JDA. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;

import static com.chalwk.Main.getBotAvatar;
import static com.chalwk.Main.getBotName;

public class PrintBoard extends NewGame {

    public PrintBoard(OptionMapping boardSize, SlashCommandInteractionEvent event, String challengerID, String opponentID) {
        super(boardSize, event, challengerID, opponentID);
    }

    public EmbedBuilder getBoardEmbed(String whosTurn) {

        String botName = getBotName();
        String botAvatar = getBotAvatar();

        EmbedBuilder embed = new EmbedBuilder();
//        embed.setTitle("⭕.❌ Tic-Tac-Toe ❌.⭕\n\n" + this.challengerName + "  vs  " + this.opponentName);
//        embed.addField("Board:", "```" + printBoard() + "```", false);
//        embed.addField("It's now " + whosTurn + "'s turn.", "", false);
//
//        embed.setFooter(botName + " - Copyright (c) 2023. Jericho Crosby", botAvatar);
        return embed;
    }

    public void setupButtons(EmbedBuilder boardEmbed, ButtonInteractionEvent event) {

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
