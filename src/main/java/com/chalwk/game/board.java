/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import static com.chalwk.game.PrintBoard.printBoard;

public class board {

    public static EmbedBuilder getBoard(char[][] board, String whosTurn, String inviteeName, String opponentName, ButtonInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("⭕.❌ Tic-Tac-Toe ❌.⭕\n\n" + inviteeName + " vs " + opponentName);
        embed.setDescription("It's " + whosTurn + "'s turn.");
        embed.addField("Board", "```" + printBoard(board) + "```", false);
        embed.setFooter("Tic-Tac-Toe", event.getJDA().getSelfUser().getAvatarUrl());
        return embed;
    }
}
