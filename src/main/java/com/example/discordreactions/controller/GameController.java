package com.example.discordreactions.controller;

import com.example.discordreactions.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class GameController {

    @Autowired
    private ReactionService reactionService;

    // Página principal con la gráfica
    @GetMapping("/")
    public String showGamePage(Model model) {
        // Podríamos inyectar contadores iniciales al modelo
        Map<String, Integer> counts = reactionService.getReactionCounts();
        model.addAttribute("reactionCounts", counts);
        return "game"; // Buscará "game.html" en /templates
    }

    // Endpoint para datos en JSON consumidos por la gráfica (Chart.js)
    @GetMapping("/api/reactions")
    @ResponseBody
    public Map<String, Integer> getReactions() {
        return reactionService.getReactionCounts();
    }
}
