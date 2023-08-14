/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;
import java.util.Timer;

import static com.chalwk.game.Game.*;
import static com.chalwk.game.GameOver.gameOver;
import static com.chalwk.game.Moves.moveAllowed;
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
    public void onMessageReceived(MessageReceivedEvent event) {

        String input = event.getMessage().getContentRaw();

        if (event.getAuthor().isBot()) return;
        if (!moveAllowed(board, event, input)) return;

        Member member = event.getMember();
        String memberID = member.getId();
        String guildID = event.getGuild().getId();

        if (games.containsKey(guildID)) {

            String[] game = games.get(guildID);
            String inviteeID = game[0];
            String opponentID = game[1];

            if (!memberID.equals(inviteeID) && !memberID.equals(opponentID)) return;

            Member inviteeMember = event.getGuild().getMemberById(inviteeID);
            String inviteeName = inviteeMember.getEffectiveName();

            Member opponentMember = event.getGuild().getMemberById(opponentID);
            String opponentName = opponentMember.getEffectiveName();

            String whosTurn;
            char symbol = (memberID.equals(inviteeID)) ? player2 : player1;
            whosTurn = (memberID.equals(inviteeID)) ? opponentName : inviteeName;

            placeMove(board, input, symbol, whosTurn, inviteeName, opponentName, event, game);
            event.getMessage().delete().queue();

            gameOver(board, game, event);
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        Guild guild = event.getGuild();
        String guildID = guild.getId();

        Member member = event.getMember();
        String memberID = member.getId();

        String buttonID = event.getComponentId();

        if (games.containsKey(guildID)) {
            String[] game = games.get(guildID);

            String inviteeID = game[0];
            String opponentID = game[1];
            if (!allowClick(event, memberID, inviteeID, opponentID)) return;

            getPlayers player = getPlayerNames(guild, inviteeID, opponentID);

            if (buttonID.equalsIgnoreCase("accept")) {
                event.getMessage().delete().queue();

                String whoStarts = whoStarts(player.inviteeName, player.opponentName);

                EmbedBuilder currentBoard = getBoard(board, whoStarts, player.inviteeName(), player.opponentName());
                currentBoard.setFooter("Tic-Tac-Toe", event.getJDA().getSelfUser().getAvatarUrl());
                event.replyEmbeds(currentBoard.build()).queue();
                setEmbedID(event, game);

            } else {

                event.getMessage().delete().queue();
                event.replyEmbeds(
                        new EmbedBuilder()
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

    private void setEmbedID(ButtonInteractionEvent event, String[] game) {
        Timer timer = new Timer();
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                long messageID = event.getChannel().getLatestMessageIdLong();
                game[2] = String.valueOf(messageID);
            }
        }, 500);
    }

    private record getPlayers(String inviteeName, String opponentName) {
    }
}