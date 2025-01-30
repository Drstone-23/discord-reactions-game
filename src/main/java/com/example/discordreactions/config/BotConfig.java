package com.example.discordreactions.config;

import com.example.discordreactions.bot.DiscordBotListener;
import com.example.discordreactions.service.ReactionService;
import com.example.discordreactions.service.WebhookService;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;

@Component
public class BotConfig {

    @Value("${discord.bot.token}")
    private String botToken;

    @Value("${discord.channel.id}")
    private String channelId;

    @Autowired
    private WebhookService webhookService;

    @Autowired
    private ReactionService reactionService;

    @EventListener(ApplicationReadyEvent.class)
    public void startBot() throws LoginException, InterruptedException {
        JDABuilder builder = JDABuilder.createDefault(botToken);
        builder.setActivity(Activity.playing("Reacciones!"));

        // Registramos el listener pasando los 3 parámetros
        builder.addEventListeners(
                new DiscordBotListener(webhookService, reactionService, channelId)
        );

        builder.build().awaitReady();
        System.out.println("Bot conectado y listo.");
    }
}
