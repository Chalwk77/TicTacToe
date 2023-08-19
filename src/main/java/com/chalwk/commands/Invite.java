/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.commands;

import com.chalwk.game.Game;
import com.chalwk.listeners.CommandInterface;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

import static com.chalwk.Main.addGame;
import static com.chalwk.Main.games;
import static com.chalwk.game.Globals.board_layout;
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

        OptionMapping size = event.getOption("board");
        OptionMapping opponent = event.getOption("opponent");

        Member member = event.getMember();
        assert member != null;
        String memberID = member.getId();

        assert opponent != null;
        String opponentID = opponent.getAsUser().getId();

        if (opponent.getAsUser().isBot()) {
            privateMessage(event, member, "You cannot invite a bot to play Tic-Tac-Toe.");
        } else if (memberID.equals(opponentID)) {
            privateMessage(event, member, "You cannot invite yourself to play Tic-Tac-Toe.");
        } else {
            invitePlayer(event, size, memberID, opponentID);
        }
    }

    private void invitePlayer(SlashCommandInteractionEvent event, OptionMapping size, String challengerID, String opponentID) {
        Game game = new Game(size, event, challengerID, opponentID);
        game.showSubmission(event);
        games = addGame(games, game);
    }
}
