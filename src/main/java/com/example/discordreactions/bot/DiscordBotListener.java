package com.example.discordreactions.bot;

import com.example.discordreactions.service.ReactionService;
import com.example.discordreactions.service.WebhookService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
// IMPORTANTE: Eliminamos el import de 'MessageReactionRemoveEvent' si no vamos a usarlo
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class DiscordBotListener extends ListenerAdapter {

    private final WebhookService webhookService;
    private final ReactionService reactionService;
    private final String channelId;

    // Mensaje "principal" con los 3 emojis
    private String mainMessageId;

    // Mensaje de anuncio del ganador actual (lo borraremos al actualizar)
    private String winningAnnouncementMessageId;

    public DiscordBotListener(WebhookService webhookService,
                              ReactionService reactionService,
                              String channelId) {
        this.webhookService = webhookService;
        this.reactionService = reactionService;
        this.channelId = channelId;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        // Cuando el bot esté listo, publica el mensaje con las 3 reacciones
        JDA jda = event.getJDA();
        GuildMessageChannel channel = jda.getChannelById(GuildMessageChannel.class, channelId);

        if (channel != null) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("🔥 ¡Juego de Reacciones! 🔥")
                    .setDescription("Reacciona con uno de los siguientes emojis para sumar puntos a tu equipo:\n\n" +
                            "1️⃣ - **Equipo 1**\n" +
                            "2️⃣ - **Equipo 2**\n" +
                            "3️⃣ - **Equipo 3**\n\n" +
                            "¡Quien tenga más puntos gana! 🎯")
                    .setColor(Color.ORANGE);

            channel.sendMessageEmbeds(embed.build()).queue(message -> {
                // Guardamos el ID del mensaje principal para filtrar reacciones
                this.mainMessageId = message.getId();

                // Añadimos las 3 reacciones
                message.addReaction(Emoji.fromUnicode("1️⃣")).queue();
                message.addReaction(Emoji.fromUnicode("2️⃣")).queue();
                message.addReaction(Emoji.fromUnicode("3️⃣")).queue();
            });

        } else {
            System.err.println("No se encontró el canal con ID: " + channelId);
        }
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (event.getUser() == null || event.getUser().isBot()) return;
        // Solo nos importa si la reacción es en el mensaje principal
        if (!event.getMessageId().equals(mainMessageId)) return;

        String emojiName = event.getReaction().getEmoji().getName();
        String equipo = determinarEquipoPorEmoji(emojiName);

        if (equipo != null) {
            // Solo incrementamos: no decrecemos aunque quiten reacción
            reactionService.incrementReaction(equipo);

            // OPCIONAL: avisar al webhook que sumó un punto
            webhookService.updateReactionCount(equipo, true);

            // Publicar/actualizar el mensaje de "¡Equipo X va ganando!"
            announceWinner(event);
        }
    }

    // Eliminamos por completo onMessageReactionRemove si no queremos que decrezca
    // @Override
    // public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
    //     // NO HACEMOS NADA
    // }

    private String determinarEquipoPorEmoji(String emojiName) {
        switch (emojiName) {
            case "1️⃣": return "Equipo1";
            case "2️⃣": return "Equipo2";
            case "3️⃣": return "Equipo3";
            default:    return null;
        }
    }

    /**
     * Averigua quién es el ganador y publica un mensaje.
     * Antes de publicar, borra el anuncio previo si existe.
     */
    private void announceWinner(MessageReactionAddEvent event) {
        String winner = reactionService.findWinner(); // Equipo con más puntos
        int points = reactionService.getReactionCounts().get(winner);

        GuildMessageChannel channel = (GuildMessageChannel) event.getChannel();

        // Borrar el antiguo mensaje de anuncio, si lo tenemos guardado
        if (winningAnnouncementMessageId != null) {
            channel.deleteMessageById(winningAnnouncementMessageId).queue(
                    success -> System.out.println("Mensaje anterior de ganador eliminado"),
                    error -> System.err.println("No se pudo eliminar: " + error.getMessage())
            );
        }

        // Enviar un nuevo mensaje anunciando al ganador
        channel.sendMessage("🏆 **" + winner + "** va ganando con **" + points + "** clics!").queue(msg -> {
            // Guardamos el ID del nuevo mensaje
            winningAnnouncementMessageId = msg.getId();
        });
    }
}
