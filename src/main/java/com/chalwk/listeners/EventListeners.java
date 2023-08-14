/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.listeners;

import com.chalwk.game.ButtonClick;
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
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        ButtonClick.buttonClick(event);
    }
}