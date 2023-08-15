/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.listeners;

import com.chalwk.game.ButtonClick;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

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

        //--------------------------------\\
        // Initialize maps for this guild: \\
        //----------------------------------\\
//        boards.put(guildID, new HashMap<>());
//        boards.get(guildID).put("boards", new HashMap<>());
//        board_indicators.put(guildID, new HashMap<>());
//        board_indicators.get(guildID).put("indicators", new HashMap<>());
//        players.put(guildID, new String[][]{});

        // Add to map_players with:  players.put(guildID, new String[][]{{challenger, opponent}});
        // String challengerName = map_players.get(guildID)[len][0];
        // String opponentName = map_players.get(guildID)[len][1];
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        ButtonClick.buttonClick(event);
    }
}