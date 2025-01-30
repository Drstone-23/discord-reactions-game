package com.example.discordreactions.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReactionService {

    private final Map<String, Integer> reactionCounts;

    public ReactionService() {
        reactionCounts = new HashMap<>();
        reactionCounts.put("Equipo1", 0);
        reactionCounts.put("Equipo2", 0);
        reactionCounts.put("Equipo3", 0);
    }

    public void incrementReaction(String team) {
        reactionCounts.put(team, reactionCounts.getOrDefault(team, 0) + 1);
    }

    // Antes decrecía, pero ahora lo quitamos o lo dejamos en blanco:
    public void decrementReaction(String team) {
        // No hacemos nada (o lo comentamos si deseas)
        // reactionCounts.put(team, Math.max(0, reactionCounts.getOrDefault(team, 0) - 1));
    }

    // Devuelve el mapa de conteos
    public Map<String, Integer> getReactionCounts() {
        return reactionCounts;
    }

    // Encuentra el equipo con más reacciones.
    // Si hay empate, devolvemos el primero que encuentre con el valor mayor.
    public String findWinner() {
        return reactionCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Ningún equipo");
    }
}
