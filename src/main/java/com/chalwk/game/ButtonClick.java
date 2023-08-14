/* Copyright (c) 2023, TicTacToe-JDA. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import static com.chalwk.game.Game.board;
import static com.chalwk.game.Game.*;
import static com.chalwk.game.Moves.moveAllowed;
import static com.chalwk.game.Moves.placeMove;
import static com.chalwk.game.PrivateMessage.privateMessage;
import static com.chalwk.game.board.getBoard;

public class ButtonClick {
    private static String challengerName;
    private static String opponentName;

    private static void startGame(ButtonInteractionEvent event, String buttonID, String[] game) {

        if (buttonID.equalsIgnoreCase("accept")) {
            event.getMessage().delete().queue();

            String starter = whoStarts(challengerName, opponentName);
            EmbedBuilder currentBoard = getBoard(board, starter, challengerName, opponentName, event);

            setupButtons(event, currentBoard);
            game[2] = "true";

        } else {

            event.getMessage().delete().queue();
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle("⭕.❌ Tic-Tac-Toe ❌.⭕ | " + challengerName + " vs " + opponentName)
                    .setDescription("Game Declined.")
                    .build()).queue();

            Guild guild = event.getGuild();
            Member challenger = guild.getMemberById(game[0]);

            privateMessage(event, challenger, "Your game invite to " + opponentName + " was declined.");
        }
    }

    public static void buttonClick(ButtonInteractionEvent event) {

        Guild guild = event.getGuild();
        assert guild != null;
        String guildID = guild.getId();

        Member member = event.getMember();
        assert member != null;
        String memberID = member.getId();

        if (games.containsKey(guildID)) {

            String buttonID = event.getComponentId();
            String buttonLabel = event.getComponent().getLabel();

            String[] game = games.get(guildID);

            String challengerID = game[0];
            String opponentID = game[1];

            if (!allowClick(event, member, challengerID, opponentID)) return;

            Member challenger = guild.getMemberById(challengerID);
            Member opponent = guild.getMemberById(opponentID);

            assert challenger != null;
            assert opponent != null;

            challengerName = challenger.getEffectiveName();
            opponentName = opponent.getEffectiveName();

            boolean gameStarted = game[2].equals("true");
            if (!gameStarted) {
                startGame(event, buttonID, game);
            } else {

                char symbol = (memberID.equals(challengerID)) ? player2 : player1;
                String whosTurn = (memberID.equals(challengerID)) ? opponentName : challengerName;

                if (!moveAllowed(board, event, buttonLabel, whosTurn, challengerName, opponentName)) return;

                placeMove(board, buttonLabel, symbol, whosTurn, challengerName, opponentName, event, game);
            }
        }
    }

    private static boolean allowClick(ButtonInteractionEvent event, Member member, String challenger, String opponent) {
        if (!member.getId().equals(challenger) && !member.getId().equals(opponent)) {
            privateMessage(event, member, "You are not part of this game.");
            return false;
        }
        return true;
    }
}
