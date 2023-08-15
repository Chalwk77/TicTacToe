/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.commands;

import com.chalwk.game.NewGame;
import com.chalwk.listeners.CommandInterface;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

import static com.chalwk.game.Globals.*;
import static com.chalwk.game.PrivateMessage.privateMessage;

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

        for (int i = 0; i < board_layout.length; i++) {
            String size = board_layout[i].length + "x" + board_layout[i].length;
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

        OptionMapping option = event.getOption("opponent");
        OptionMapping size = event.getOption("board");

        if (option.getAsUser().isBot()) {
            privateMessage(event, event.getMember(), "You cannot invite a bot to play TicTacToe.");
        } else {

            String challengerID = event.getUser().getId();
            String opponentID = option.getAsUser().getId();

            invitePlayer(event, size, challengerID, opponentID);
        }
    }

    private void invitePlayer(SlashCommandInteractionEvent event, OptionMapping size, String challengerID, String opponentID) {

        int gameCount = getGameCount();
        NewGame new_game = new NewGame(size, event, challengerID, opponentID);
        if (gameCount == 0) {
            concurrentGames = new NewGame[1];
            concurrentGames[0] = new_game;
        } else {
            NewGame[] newConcurrentGames = new NewGame[gameCount + 1];
            System.arraycopy(concurrentGames, 0, newConcurrentGames, 0, gameCount);
            newConcurrentGames[gameCount] = new_game;
            concurrentGames = newConcurrentGames;
        }
        for (NewGame game : concurrentGames) {
            System.out.println("NEW GAME INVITE SENT:");
            System.out.println(game.challengerName + " vs " + game.opponentName);
            System.out.println("Game Count: " + gameCount);
        }
        concurrentGames[gameCount].showSubmission(event);
    }
}
