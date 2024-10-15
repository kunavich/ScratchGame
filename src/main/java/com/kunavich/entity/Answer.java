package com.kunavich.entity;

import java.util.*;

public class Answer {

    private final String[][] matrix ;
    private double reward;
    private Map<String, List<String>> applied_winning_combinations;
    private List<String> applied_bonus_symbol;

    public Answer(String[][] matrix) {
        this.matrix=matrix;
        reward=0;
        applied_winning_combinations= new HashMap<>();
        applied_bonus_symbol =new ArrayList<>();
    }

    public String[][] getMatrix() {
        return matrix;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public Map<String, List<String>> getApplied_winning_combinations() {
        return applied_winning_combinations;
    }

    public List<String> getApplied_bonus_symbol() {
        return applied_bonus_symbol;
    }

}
