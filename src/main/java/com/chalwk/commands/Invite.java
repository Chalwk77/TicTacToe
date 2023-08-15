/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.commands;

import com.chalwk.listeners.CommandInterface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;

import static com.chalwk.game.Globals.boards;
import static com.chalwk.game.Globals.concurrentGames;
import static com.chalwk.game.PrivateMessage.privateMessage;
import static com.chalwk.game.board.newBoard;
import static com.chalwk.game.board.printBoard;

public class Invite implements CommandInterface {

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getDescription() {
        return "Invite someone to play TicTacToe with you.";
    }

    @Override
    public List<OptionData> getOptions() {

        List<OptionData> options = new ArrayList<>();
        OptionData user = new OptionData(OptionType.USER, "opponent", "The user you want to invite.");
        user.setRequired(true);

        OptionData option = new OptionData(OptionType.INTEGER, "board", "The board size you want to play on.");

        for (int i = 0; i < boards.length; i++) {
            String size = boards[i].length + "x" + boards[i].length;
            option.addChoice(size, i);
        }
        option.setRequired(true);

        options.add(user);
        options.add(option);

        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        Guild guild = event.getGuild();
        String guildID = guild.getId();

        OptionMapping option = event.getOption("opponent");
        OptionMapping size = event.getOption("board");

        if (option.getAsUser().isBot()) {
            privateMessage(event, event.getMember(), "You cannot invite a bot to play TicTacToe.");
        } else {

            String challengerID = event.getUser().getId();
            String opponentID = option.getAsUser().getId();

            char[][] board = newBoard(size);
            showSubmission(board, event, challengerID, opponentID);

            concurrentGames.put(guildID, new String[]{challengerID, opponentID, "false"});
        }
    }

    private void showSubmission(char[][] board, SlashCommandInteractionEvent event, String challengerID, String opponentID) {
        String botName = event.getJDA().getSelfUser().getName() + " - Copyright (c) 2023. Jericho Crosby";

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("⭕.❌ Tic-Tac-Toe ❌.⭕");
        embed.setDescription("You have been invited to play TicTacToe.");
        embed.addField("Challenger:", "<@" + challengerID + ">", true);
        embed.addField("Opponent:", "<@" + opponentID + ">", true);
        embed.addField("A random player will be selected to go first.", "", false);
        embed.addField("Board: (" + board.length + "x" + board.length + ")", "```" + printBoard(board) + "```", false);
        embed.setFooter(botName, event.getJDA().getSelfUser().getAvatarUrl());

        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.success("accept", "\uD83D\uDFE2 Accept"));
        buttons.add(Button.danger("decline", "\uD83D\uDD34 Decline"));
        buttons.add(Button.secondary("cancel", "\uD83D\uDEAB Cancel"));

        event.replyEmbeds(embed.build()).addActionRow(buttons).queue();
    }
}
