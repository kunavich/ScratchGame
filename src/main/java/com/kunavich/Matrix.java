package com.kunavich;

import com.kunavich.entity.Answer;
import com.kunavich.entity.Config;
import com.kunavich.entity.probabillities.StandardSymbol;

import java.util.*;

public class Matrix {

    private String[][] matrix;
    private Answer answer;
    private Config config;

    public void generateMatrix(Config config) {
        matrix = new String[config.getColumns()][config.getRows()];
        this.config=config;

        List<StandardSymbol> cells = config.getProbabilities().getStandard_symbols();
        for (StandardSymbol cell:cells) {
            String symbol;
            if(isBonus(cell.getProbabilitySum())){
                symbol = generateBonusCell();
            } else {
                symbol = generateStandardCell(cell.getProbabilitySum(),cell.getSymbols());
            }
            matrix[cell.getRow()][cell.getColumn()]=symbol;
        }
        answer = new Answer(matrix);
    }

    private boolean isBonus(int standardProbability){
        int bonusProbability = config.getProbabilities().getBonus_symbols().getProbabilitySum();
        int probabilitySum = standardProbability + bonusProbability;

        Random random = new Random();
        double randomValue = random.nextDouble();

        return !(randomValue >= (double) standardProbability / probabilitySum);
    }

    private String generateBonusCell() {
        int bonusProbabilitySum = config.getProbabilities().getBonus_symbols().getProbabilitySum();
        Map<String, Integer> probabilities = config.getProbabilities().getBonus_symbols().getSymbols();

        Random random = new Random();
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;

        for (Map.Entry<String, Integer> entry : probabilities.entrySet()) {
            cumulativeProbability += ((double) entry.getValue()/bonusProbabilitySum);
            if (randomValue <= cumulativeProbability) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("Probabilities do not sum to 1.0");
    }

    private String generateStandardCell( int standardProbabilitySum, Map<String, Integer> probabilities ) {
        Random random = new Random();
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;

        for (Map.Entry<String, Integer> entry : probabilities.entrySet()) {
            cumulativeProbability += ((double) entry.getValue()/standardProbabilitySum);
            if (randomValue <= cumulativeProbability) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("Probabilities do not sum to 1.0");
    }

}
