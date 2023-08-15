/* Copyright (c) 2023, TicTacToe-JDA. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Random;

import static com.chalwk.game.PrivateMessage.privateMessage;

public class ButtonClick {
    private static String challengerName;
    private static String opponentName;

    private static String whoStarts(String challenger, String opponent) {
        Random random = new Random();
        int randomNum = random.nextInt(2);
        return (randomNum == 0) ? challenger : opponent;
    }

    // board, event, buttonID, game
    private static void startGame(char[][] board, String[] game, String buttonID, ButtonInteractionEvent event) {

//        Member member = event.getMember();
//        String userID = member.getId();
//
//        if (buttonID.equalsIgnoreCase("accept")) {
//            event.getMessage().delete().queue();
//
//            String starter = whoStarts(challengerName, opponentName);
//            EmbedBuilder currentBoard = getBoardEmbed(board, starter, challengerName, opponentName);
//
//            setupButtons(board, currentBoard, event);
//            game[2] = "true";
//
//        } else if (buttonID.equalsIgnoreCase("decline")) {
//            if (userID.equals(game[0])) {
//                privateMessage(event, member, "You cannot decline your own game. Click cancel.");
//                return;
//            }
//
//            event.getMessage().delete().queue();
//            event.replyEmbeds(new EmbedBuilder()
//                    .setTitle("⭕.❌ Tic-Tac-Toe ❌.⭕ | " + challengerName + " vs " + opponentName)
//                    .setDescription("Game Declined.")
//                    .build()).queue();
//
//            Guild guild = event.getGuild();
//            Member challenger = guild.getMemberById(game[0]);
//
//            privateMessage(event, challenger, "Your game invite to " + opponentName + " was declined.");
//        } else if (buttonID.equalsIgnoreCase("cancel")) {
//            if (!userID.equals(game[0])) {
//                privateMessage(event, member, "You are not the challenger. Unable to cancel.");
//                return;
//            }
//
//            String size = board.length + "x" + board.length;
//            privateMessage(event, member, "The (" + size + ") game invite to " + opponentName + " was cancelled.");
//
//            event.getMessage().delete().queue();
//        }
    }

    public static void buttonClick(ButtonInteractionEvent event) {

//        Guild guild = event.getGuild();
//        assert guild != null;
//        String guildID = guild.getId();
//
//        Member member = event.getMember();
//        assert member != null;
//        String memberID = member.getId();
//
//        if (concurrentGames.containsKey(guildID)) {
//
//            String buttonID = event.getComponentId();
//            String buttonLabel = event.getComponent().getLabel();
//
//            String[] game = concurrentGames.get(guildID);
//
//            String challengerID = game[0];
//            String opponentID = game[1];
//
//            if (!allowClick(event, member, challengerID, opponentID)) return;
//
//            Member challenger = guild.getMemberById(challengerID);
//            Member opponent = guild.getMemberById(opponentID);
//
//            assert challenger != null;
//            assert opponent != null;
//
//            challengerName = challenger.getEffectiveName();
//            opponentName = opponent.getEffectiveName();
//
//            boolean gameStarted = game[2].equals("true");
//            if (!gameStarted) {
//                startGame(board, game, buttonID, event);
//            } else {
//
//                char symbol = (memberID.equals(challengerID)) ? player2 : player1;
//                String whosTurn = (memberID.equals(challengerID)) ? opponentName : challengerName;
//
//                if (!moveAllowed(board, event, buttonLabel, whosTurn, challengerName, opponentName)) return;
//
//                placeMove(board, buttonLabel, symbol, whosTurn, challengerName, opponentName, event, game);
//            }
//        }
    }

    private static boolean allowClick(ButtonInteractionEvent event, Member member, String challenger, String opponent) {
        if (!member.getId().equals(challenger) && !member.getId().equals(opponent)) {
            privateMessage(event, member, "You are not part of this game.");
            return false;
        }
        return true;
    }
}
