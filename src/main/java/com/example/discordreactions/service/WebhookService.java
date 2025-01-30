package com.example.discordreactions.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WebhookService {

    @Value("${discord.webhook.url}")
    private String webhookUrl;

    // Almacenamos el último ID del mensaje enviado por este webhook
    private String lastMessageId;

    /**
     * Actualiza la cuenta de reacciones, borrando el mensaje anterior y enviando uno nuevo.
     */
    public void updateReactionCount(String team, boolean isIncrement) {
        String action = isIncrement ? "incrementado" : "decrementado";
        String message = "📢 ¡El equipo **" + team + "** ha sido **" + action + "**!";

        // 1. Borramos el mensaje anterior si existe
        deleteLastWebhookMessage();

        // 2. Enviamos el nuevo mensaje con ?wait=true para que Discord nos devuelva el message_id
        sendWebhookMessageWait(message);
    }

    /**
     * Envía un mensaje usando el webhook con la query param ?wait=true
     * para obtener en la respuesta el "message_id" que acabamos de crear.
     */
    private void sendWebhookMessageWait(String content) {
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            System.out.println("❌ No se configuró la URL del webhook.");
            return;
        }

        // Enviamos el mensaje en JSON
        Map<String, String> payload = Map.of("content", content);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        // Agregamos "?wait=true" al final de la URL
        String urlWithWait = webhookUrl + "?wait=true";

        RestTemplate restTemplate = new RestTemplate();
        try {
            // postForEntity(...) nos devuelve la respuesta con el JSON del mensaje
            ResponseEntity<Map> response = restTemplate.postForEntity(urlWithWait, request, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // Obtenemos el ID del mensaje recién publicado
                Map body = response.getBody();
                lastMessageId = (String) body.get("id");
                System.out.println("Mensaje enviado. ID=" + lastMessageId);
            } else {
                System.err.println("Error al enviar mensaje. Código HTTP=" + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Excepción al enviar mensaje con webhook: " + e.getMessage());
        }
    }

    /**
     * Si tenemos un lastMessageId, intentamos borrarlo:
     * DELETE /webhooks/{webhook.id}/{webhook.token}/messages/{message.id}
     */
    private void deleteLastWebhookMessage() {
        if (lastMessageId == null) {
            return; // No hay mensaje previo que borrar
        }
        try {
            RestTemplate restTemplate = new RestTemplate();
            // La URL de borrado es <webhookUrl>/messages/<messageId>
            // Ejemplo: https://discord.com/api/webhooks/1234/abcde/messages/9876
            String deleteUrl = webhookUrl + "/messages/" + lastMessageId;
            restTemplate.delete(deleteUrl);

            System.out.println("Mensaje anterior con ID=" + lastMessageId + " eliminado");
        } catch (Exception e) {
            System.err.println("Error al eliminar mensaje con ID=" + lastMessageId + ": " + e.getMessage());
        } finally {
            lastMessageId = null; // Ya no hay mensaje anterior
        }
    }
}
