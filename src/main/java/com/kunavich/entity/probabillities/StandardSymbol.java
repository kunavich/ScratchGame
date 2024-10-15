package com.kunavich.entity.probabillities;

import java.util.Map;

public class StandardSymbol {
    private int column;
    private int row;
    private Map<String, Integer> symbols;

    private int probabilitySum = -1;

    // Getters and Setters
    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

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
