package com.example.discordreactions.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/discord-webhook")
public class DiscordWebhookController {

    private final ConcurrentHashMap<String, Integer> teamScores = new ConcurrentHashMap<>();

    @PostMapping
    public void updateScores(@RequestBody Map<String, Integer> scores) {
        teamScores.putAll(scores);
    }

    @GetMapping
    public Map<String, Integer> getScores() {
        return teamScores;
    }
}
