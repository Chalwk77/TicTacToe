/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.listeners;

import com.chalwk.game.ButtonClick;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EventListeners extends ListenerAdapter {

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        System.out.println("""
                ________________________________________________
                Copyright (c) 2023, Tic Tac Toe, Jericho Crosby
                ___    __     ___       __     ___  __   ___
                 |  | /  `     |   /\\  /  `     |  /  \\ |__
                 |  | \\__,     |  /~~\\ \\__,     |  \\__/ |___
                ________________________________________________""");

        Guild guild = event.getGuild();
        String guildID = guild.getId();

        String challenger = "Chalwk";
        String opponent = "ErratumKing";


        //=================================================================================//
        Map<String, Map<String, Map<String, char[][]>>> gameData = new HashMap<>();
        gameData.put(guildID, new HashMap<>());
        gameData.get(guildID).put("boards", new HashMap<>());
        gameData.get(guildID).put("players", new HashMap<>());

        int len = gameData.get(guildID).get("boards").size();
        for (int game_id = 0; game_id < len; game_id++) {
            char[][] BOARD = {{'X', 'O', 'X'}, {'X', 'O', 'O'}, {'O', 'X', 'O'}};
            gameData.get(guildID).get("boards").put(String.valueOf(game_id), BOARD);
        }

        Map<String, String[][]> players = new HashMap<>();
        players.put(guildID, new String[][]{{challenger, opponent}});

        String challengerName = players.get(guildID)[len][0];
        String opponentName = players.get(guildID)[len][1];

        System.out.println("Challenger: " + challengerName);
        System.out.println("Opponent: " + opponentName);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        ButtonClick.buttonClick(event);
    }
}