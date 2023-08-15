/* Copyright (c) 2023, TicTacToe-JDA. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

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
}
