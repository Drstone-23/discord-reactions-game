package com.example.discordreactions.model;

public class TeamReaction {
    private String teamName;
    private int reactionCount;

    public TeamReaction(String teamName, int reactionCount) {
        this.teamName = teamName;
        this.reactionCount = reactionCount;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getReactionCount() {
        return reactionCount;
    }

    public void setReactionCount(int reactionCount) {
        this.reactionCount = reactionCount;
    }
}
