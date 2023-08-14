/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import static com.chalwk.Main.getBotAvatar;
import static com.chalwk.Main.getBotName;
import static com.chalwk.game.PrintBoard.printBoard;

public class board {

    public static EmbedBuilder getBoard(char[][] board, String whosTurn, String challengerName, String opponentName, ButtonInteractionEvent event) {

        String botName = getBotName();
        String botAvatar = getBotAvatar();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("⭕.❌ Tic-Tac-Toe ❌.⭕\n\n" + challengerName + "  vs  " + opponentName);
        embed.addField("Board:", "```" + printBoard(board) + "```", false);
        embed.addField("It's now " + whosTurn + "'s turn.", "", false);

        embed.setFooter(botName + " - Copyright (c) 2023. Jericho Crosby", botAvatar);
        return embed;
    }
}
