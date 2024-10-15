package com.kunavich.entity;

public class Symbol {
    private double reward_multiplier;
    private String type;
    private String impact;
    private double extra;// Some symbols have an "impact" field

    // Getters and Setters
    public double getReward_multiplier() {
        return reward_multiplier;
    }

    public void setReward_multiplier(double reward_multiplier) {
        this.reward_multiplier = reward_multiplier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public double getExtra() {
        return extra;
    }

    public void setExtra(double extra) {
        this.extra = extra;
    }
}
