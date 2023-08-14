/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import static com.chalwk.game.PrintBoard.printBoard;

public class board {

    public static EmbedBuilder getBoard(char[][] board, String whosTurn, String inviteeName, String opponentName, ButtonInteractionEvent event) {

        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("⭕.❌ Tic-Tac-Toe ❌.⭕\n\n " + inviteeName + " vs " + opponentName);
        embed.addField("Board:", "```" + printBoard(board) + "```", false);
        embed.addField("It's now " + whosTurn + "'s turn.", "", false);

        String botName = event.getJDA().getSelfUser().getName();
        botName += " - Copyright (c) 2023. Jericho Crosby";

        embed.setFooter(botName, event.getJDA().getSelfUser().getAvatarUrl());
        return embed;
    }
}
