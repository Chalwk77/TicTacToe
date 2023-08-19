/* Copyright (c) 2023, TicTacToe-JDA. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import static com.chalwk.Main.games;
import static com.chalwk.game.PrivateMessage.privateMessage;

public class ButtonClick {
    public static void buttonClick(ButtonInteractionEvent event) {

        buttonData data = getButtonData(event);

        for (Game game : games) {
            if (game == null) continue;

            String buttonLabel = data.button.getLabel();
            String challengerID = game.challengerID;
            String opponentID = game.opponentID;

            if (data.memberID().equals(challengerID) || data.memberID().equals(opponentID)) {
                if (!game.started) {
                    if (data.buttonID.equalsIgnoreCase("accept")) {
                        if (canClick(data, opponentID, event, "You are not the opponent. Unable to accept."))
                            continue;
                        game.acceptInvitation(event);
                    } else if (data.buttonID.equalsIgnoreCase("decline")) {
                        if (canClick(data, opponentID, event, "You are not the opponent. Unable to decline."))
                            continue;
                        game.declineInvitation(event, data.member);
                    } else if (data.buttonID.equalsIgnoreCase("cancel")) {
                        if (canClick(data, challengerID, event, "You are not the challenger. Unable to cancel."))
                            continue;
                        game.cancelInvitation(event, data.member);
                    }
                } else if (!game.moveAllowed(buttonLabel) || (!yourTurn(game, data.member))) {
                    return;
                } else {
                    game.placeMove(event, buttonLabel);
                }
            }
        }
    }

    private static boolean canClick(buttonData button, String playerID, ButtonInteractionEvent event, String message) {
        if (!button.memberID.equals(playerID)) {
            privateMessage(event, button.member, message);
            return true;
        }
        return false;
    }

    private static boolean yourTurn(Game game, Member member) {
        return member.getEffectiveName().equals(game.whos_turn);
    }

    @NotNull
    private static buttonData getButtonData(ButtonInteractionEvent event) {
        Member member = event.getMember();
        String memberID = member.getId();
        Button button = event.getComponent();
        String buttonLabel = button.getLabel();
        String buttonID = button.getId();
        return new buttonData(member, memberID, button, buttonID, buttonLabel);
    }

    private record buttonData(Member member, String memberID, Button button, String buttonID, String buttonLabel) {

    }
}
