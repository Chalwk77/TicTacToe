/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk;

import com.chalwk.game.TEST;
import com.chalwk.listeners.CommandInterface;
import com.chalwk.listeners.CommandManager;
import com.chalwk.listeners.EventListeners;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

import static com.chalwk.util.Authentication.getToken;
import static org.reflections.Reflections.log;

// fixme: Players can place a move on a button when it's not their turn.
// fixme: Throws NullPointerException (Cannot load from int array because "cells" is null) when you start another game.
// todo: Prevent players from starting a game with themselves.
// todo: Implement invitation expiration timer.

public class Main {

    public static String botName;
    public static String botAvatar;
    private ShardManager shardManager;

    public Main() throws LoginException, IOException {
        shardManager = buildBot();
        shardManager.addEventListener(loadCommands());

        //
        // TEST CODE WORKING:
        //
        TEST[] concurrent = new TEST[0];
        int length = concurrent.length;
        for (int i = 0; i < 10; i++) {
            TEST[] temp = new TEST[length + 1];
            System.arraycopy(concurrent, 0, temp, 0, length);
            concurrent = temp;
            concurrent[length] = new TEST();
            concurrent[length].setGameID(i);
            concurrent[length].setChallengerName("nothing");
            length = concurrent.length;
        }
        concurrent[5].setChallengerName("Jericho");
        for (TEST test : concurrent) {
            int gameID = test.getGameID();
            String challengerName = test.getChallengerName();
            System.out.println("Game ID: " + gameID + " Challenger Name: " + challengerName);
        }
    }

    public static void main(String[] args) {
        try {
            new Main();
        } catch (LoginException | IOException e) {
            System.out.println("Failed to start bot: " + e.getMessage());
        }
    }

    public static String getBotName() {
        return botName;
    }

    public static String getBotAvatar() {
        return botAvatar;
    }

    @NotNull
    private CommandManager loadCommands() {
        CommandManager commands = new CommandManager();
        Reflections reflections = new Reflections("com.chalwk.commands");
        for (Class<?> commandClass : reflections.getSubTypesOf(CommandInterface.class)) {
            try {
                commands.add((CommandInterface) commandClass.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                log.warn("Failed to load command: " + commandClass.getName(), e, Level.WARNING);
            }
        }
        return commands;
    }

    @NotNull
    private ShardManager buildBot() throws IOException {
        String token = getToken();
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("TicTacToe"));
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.MESSAGE_CONTENT);

        shardManager = builder.build();
        shardManager.addEventListener(new EventListeners());
        botName = shardManager.getShards().get(0).getSelfUser().getName();
        botAvatar = shardManager.getShards().get(0).getSelfUser().getAvatarUrl();

        return shardManager;
    }
}
