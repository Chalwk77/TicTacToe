/* Copyright (c) 2023, TicTacToe. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk;

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

public class Main {

    private final ShardManager shardManager;

    public Main() throws LoginException, IOException {
        shardManager = buildBot();
        shardManager.addEventListener(loadCommands());
    }

    public static void main(String[] args) {
        try {
            new Main();
        } catch (LoginException | IOException e) {
            System.out.println("Failed to start bot: " + e.getMessage());
        }
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
        final ShardManager shardManager;
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

        return shardManager;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }
}
