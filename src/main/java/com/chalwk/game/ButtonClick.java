/* Copyright (c) 2023, TicTacToe-JDA. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.game;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import static com.chalwk.game.Globals.concurrentGames;

public class ButtonClick {
    public static void buttonClick(ButtonInteractionEvent event) {

        Member member = event.getMember();
        String memberID = member.getId();

        for (Game game : concurrentGames) {

            String challengerID = game.challengerID;
            String opponentID = game.opponentID;
            String challengerName = game.challengerName;
            String opponentName = game.opponentName;

            boolean started = game.started;

            if (memberID.equals(challengerID) || memberID.equals(opponentID)) {
                Button button = event.getComponent();
                String buttonID = button.getId();
                if (!started) {
                    game.startGame(event, buttonID);
                } else {

                    String buttonLabel = button.getLabel();
                    game.whos_turn = (memberID.equals(challengerID)) ? opponentName : challengerName;

                    if (!game.moveAllowed(event, buttonLabel)) return;

                    game.symbol = (memberID.equals(challengerID)) ? game.player2 : game.player1;
                    game.placeMove(event, buttonLabel, game); // magic happens here.
                }
                break;
            }
        }
    }
}
