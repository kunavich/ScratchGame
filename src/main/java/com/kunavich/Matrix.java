package com.kunavich;

import com.kunavich.entity.Answer;
import com.kunavich.entity.Config;
import com.kunavich.entity.WinCombination;
import com.kunavich.entity.probabillities.StandardSymbol;

import java.util.*;

public class Matrix {

    private String[][] matrix;
    private List<String> bonuses;
    private Map<String,Integer> symbolAmounts;
    private Answer answer;
    private Config config;

    public void generateMatrix(Config config) {
        matrix = new String[config.getColumns()][config.getRows()];
        symbolAmounts = new HashMap<>();
        bonuses = new ArrayList<>();
        this.config=config;

        List<StandardSymbol> cells = config.getProbabilities().getStandard_symbols();
        for (StandardSymbol cell:cells) {
            String symbol;
            boolean bonusFlag =false;
            if(isBonus(cell.getProbabilitySum())){
                symbol = generateBonusCell();
                bonusFlag = true;
            } else {
                symbol = generateStandardCell(cell.getProbabilitySum(),cell.getSymbols());
            }
            if(!bonusFlag) {
                if (symbolAmounts.containsKey(symbol)) {
                    symbolAmounts.put(symbol, symbolAmounts.get(symbol) + 1);
                } else {
                    symbolAmounts.put(symbol, 1);
                }
            }
            matrix[cell.getRow()][cell.getColumn()]=symbol;
        }
        answer = new Answer(matrix);
    }

    public Answer getAnswer(){
        return answer;
    }

    public void calculateWin(double betAmount) {
        double reward=0;
        Map<String, WinCombination> wins = config.getWin_combinations();
        for (Map.Entry<String, Integer> entry : symbolAmounts.entrySet()) {
            double symbolReward=-1;
            double symbolMultiplier= config.getSymbols().get(entry.getKey()).getReward_multiplier();
            for (Map.Entry<String, WinCombination> win: wins.entrySet()) {
                if(win.getValue().getWhen().equals("same_symbols") && win.getValue().getCount()==entry.getValue()){
                    if(symbolReward==-1){
                        symbolReward=1;
                    }
                    symbolReward*=betAmount*win.getValue().getReward_multiplier()*symbolMultiplier;
                    addWin(entry.getKey(),win.getKey());
                }
                if(win.getValue().getWhen().equals("linear_symbols") ){
                    List<List<String>> covered_areas = win.getValue().getCovered_areas();
                    for (List<String> list : covered_areas) {
                        boolean winingFlag = true;
                        for (String position : list) {
                            String[] split = position.split(":");
                            int col = Integer.parseInt(split[0]);
                            int row = Integer.parseInt(split[1]);
                            if (!matrix[row][col].equals(entry.getKey())) {
                                winingFlag = false;
                                break;
                            }
                        }
                        if (winingFlag) {
                            if(symbolReward==-1){
                                symbolReward=1;
                            }
                            addWin(entry.getKey(),win.getKey());
                            symbolReward *= betAmount * win.getValue().getReward_multiplier() * symbolMultiplier;
                        }
                    }
                }
            }
            if(symbolReward!=-1) {
                reward += symbolReward;
            }
        }
        if(reward!=0) {
            for (String bonus : bonuses) {
                if (bonus.equals("MISS")) {
                    continue;
                }
                answer.getApplied_bonus_symbol().add(bonus);
                if (reward != 0 && config.getSymbols().get(bonus).getImpact().equals("multiply_reward")) {
                    reward *= config.getSymbols().get(bonus).getReward_multiplier();
                }
                if (reward != 0 && config.getSymbols().get(bonus).getImpact().equals("extra_bonus")) {
                    reward += config.getSymbols().get(bonus).getExtra();
                }
            }
        }
        answer.setReward(reward);
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
                bonuses.add(entry.getKey());
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

    private void addWin(String entryKey,String winKey ){
        List<String> list;
        if(answer.getApplied_winning_combinations().containsKey(entryKey)) {
            list=(answer.getApplied_winning_combinations().get(entryKey));
        } else {
            list = new ArrayList<>();
        }
        list.add(winKey);
        answer.getApplied_winning_combinations().put(entryKey,list);
    }
}
