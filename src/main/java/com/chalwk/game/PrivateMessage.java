/* Copyright (c) 2023, TicTacToe-JDA. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class PrivateMessage {

    static void privateMessage(ButtonInteractionEvent event, Member member, String Message) {

        Guild guild = event.getGuild();
        assert guild != null;
        assert member != null;
        guild.retrieveMemberById(member.getId()).queue(user -> user.getUser()
                .openPrivateChannel()
                .queue(privateChannel -> privateChannel.sendMessage(Message).queue()));
    }
}
