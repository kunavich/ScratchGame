package com.kunavich.entity.probabillities;

import java.util.Map;

public class BonusSymbols {
    private Map<String, Integer> symbols;

    private int probabilitySum = -1;

    // Getters and Setters
    public Map<String, Integer> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, Integer> symbols) {
        this.symbols = symbols;
    }

    public int getProbabilitySum() {
        if(probabilitySum == -1) {
            int sum = 0;
            for (int i : symbols.values()) {
                sum += i;
            }
            probabilitySum=sum;
            return sum;
        }
        return probabilitySum;
    }
}
