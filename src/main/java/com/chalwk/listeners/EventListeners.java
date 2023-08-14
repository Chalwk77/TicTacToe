/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.chalwk.game.Game.*;
import static com.chalwk.game.Moves.placeMove;
import static com.chalwk.game.board.getBoard;

public class EventListeners extends ListenerAdapter {

    private static String whoStarts(String inviteeName, String opponentName) {
        Random random = new Random();
        int randomNum = random.nextInt(2);
        return (randomNum == 0) ? inviteeName : opponentName;
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        System.out.println("""
                ________________________________________________
                Copyright (c) 2023, Tic Tac Toe, Jericho Crosby
                ___    __     ___       __     ___  __   ___
                 |  | /  `     |   /\\  /  `     |  /  \\ |__
                 |  | \\__,     |  /~~\\ \\__,     |  \\__/ |___
                ________________________________________________""");
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        Guild guild = event.getGuild();
        String guildID = guild.getId();

        Member member = event.getMember();
        String memberID = member.getId();

        if (games.containsKey(guildID)) {

            String buttonID = event.getComponentId();
            String buttonLabel = event.getComponent().getLabel();

            String[] game = games.get(guildID);

            String inviteeID = game[0];
            String opponentID = game[1];

            boolean gameStarted = game[2].equals("true");
            if (!gameStarted) {
                startGame(event, guild, inviteeID, opponentID, buttonID, game);
            } else {

                getPlayers player = getPlayerNames(guild, inviteeID, opponentID);

                String inviteeName = player.inviteeName();
                String opponentName = player.opponentName();

                char symbol = (memberID.equals(inviteeID)) ? player2 : player1;
                String whosTurn = (memberID.equals(inviteeID)) ? opponentName : inviteeName;

                placeMove(board, buttonLabel, symbol, whosTurn, inviteeName, opponentName, event, game);
            }
        }
    }

    private void startGame(ButtonInteractionEvent event, Guild guild, String inviteeID, String opponentID, String buttonID, String[] game) {

        getPlayers player = getPlayerNames(guild, inviteeID, opponentID);
        if (buttonID.equalsIgnoreCase("accept")) {
            event.getMessage().delete().queue();

            String whoStarts = whoStarts(player.inviteeName, player.opponentName);

            EmbedBuilder currentBoard = getBoard(board, whoStarts, player.inviteeName(), player.opponentName(), event);

            setupButtons(event, currentBoard);
            game[2] = "true"; // game started

        } else {

            event.getMessage().delete().queue();
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle("⭕.❌ Tic-Tac-Toe ❌.⭕ | " + player.inviteeName() + " vs " + player.opponentName())
                    .setDescription("Game Declined.")
                    .build()).queue();

            event.getGuild().retrieveMemberById(inviteeID).queue(invitee -> {
                invitee.getUser().openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage("Your game invite to " + player.opponentName() + " was declined.").queue();
                });
            });
        }
    }

    private void setupButtons(ButtonInteractionEvent event, EmbedBuilder currentBoard) {

        List<Button> buttons = new ArrayList<>();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                int id = (row * board.length) + col;
                buttons.add(Button.secondary(String.valueOf(id), letters[row] + (col + 1)));
            }
        }

        switch (board.length) {
            case 3 -> {
                List<Button> row1 = buttons.subList(0, 3);
                List<Button> row2 = buttons.subList(3, 6);
                List<Button> row3 = buttons.subList(6, 9);
                event.replyEmbeds(currentBoard.build())
                        .addActionRow(row1)
                        .addActionRow(row2)
                        .addActionRow(row3).queue();
            }
            case 4 -> {
                List<Button> row1 = buttons.subList(0, 4);
                List<Button> row2 = buttons.subList(4, 8);
                List<Button> row3 = buttons.subList(8, 12);
                List<Button> row4 = buttons.subList(12, 16);
                event.replyEmbeds(currentBoard.build())
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
                event.replyEmbeds(currentBoard.build())
                        .addActionRow(row1)
                        .addActionRow(row2)
                        .addActionRow(row3)
                        .addActionRow(row4)
                        .addActionRow(row5).queue();
            }
        }
    }

    @NotNull
    private getPlayers getPlayerNames(Guild guild, String inviteeID, String opponentID) {

        Member inviteeMember = guild.getMemberById(inviteeID);
        Member opponentMember = guild.getMemberById(opponentID);

        assert inviteeMember != null;
        String inviteeName = inviteeMember.getEffectiveName();

        assert opponentMember != null;
        String opponentName = opponentMember.getEffectiveName();

        return new getPlayers(inviteeName, opponentName);
    }

    private boolean allowClick(ButtonInteractionEvent event, String memberID, String inviteeID, String opponentID) {
        if (!memberID.equals(inviteeID) && !memberID.equals(opponentID)) {
            Objects.requireNonNull(event.getGuild()).retrieveMemberById(memberID).queue(user -> {
                user.getUser().openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage("You are not part of this game!").queue();
                });
            });
            return false;
        }
        return true;
    }

    private record getPlayers(String inviteeName, String opponentName) {
    }
}