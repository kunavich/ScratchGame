package com.kunavich;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kunavich.entity.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

    private final double betAmount =100;
    private final String filePrefix= "src/test/resources/";
    private final String[][] noWinsMatrix =new String[][]{{"A", "D", "F"}, {"B", "E", "E"}, {"C", "F", "D"}};
    private final String[][] missOnlyMatrix =new String[][]{{"MISS", "MISS", "MISS"},
            {"MISS", "MISS", "MISS"}, {"MISS", "MISS", "MISS"}};

    private Matrix matrix;
    private ObjectMapper objectMapper;
    private String fileName;

    @BeforeEach
    void setUp() {
        matrix= new Matrix();
        objectMapper = new ObjectMapper();
    }

    @Test
    void generateMatrixCanGenerate3x3Matrix() {
        fileName="config.json";
        Config config;
        try {
            config = objectMapper.readValue(new File(filePrefix+fileName), Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        matrix.generateMatrix(config);
        assertFalse(areMatricesEqual(matrix.getAnswer().getMatrix(),missOnlyMatrix), "Matrices should not be equal");
    }
    @Test
    void generateMatrixCanGenerate4x4Matrix() {
        fileName="configTest4x4.json";
        Config config;
        try {
            config = objectMapper.readValue(new File(filePrefix+fileName), Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        matrix.generateMatrix(config);
        assertFalse(areMatricesEqual(matrix.getAnswer().getMatrix(),missOnlyMatrix), "Matrices should not be equal");
    }

    @Test
    void generateMatrixWithNoWins() {
        fileName="configTestNoReward.json";
        Config config;
        try {
            config = objectMapper.readValue(new File(filePrefix+fileName), Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        matrix.generateMatrix(config);
        assertTrue(areMatricesEqual(matrix.getAnswer().getMatrix(),noWinsMatrix), "Matrices should be equal");
    }

    @Test
    void generateMatrixWithMissOnly() {
        fileName="configTestMissOnly.json";
        Config config;
        try {
            config = objectMapper.readValue(new File(filePrefix+fileName), Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        matrix.generateMatrix(config);
        assertTrue(areMatricesEqual(matrix.getAnswer().getMatrix(),missOnlyMatrix), "Matrices should be equal");
    }


    @Test
    void generateMatrixWithNoConfigGiven() {
        assertThrows (IOException.class, () ->{
            fileName="configBlaBla.json";
            Config config = objectMapper.readValue(new File(filePrefix+fileName), Config.class);
            matrix.generateMatrix(config);
        });
    }

    @Test
    void calculateWinWithNoWin() {
        fileName="configTestNoReward.json";
        Config config;
        try {
            config = objectMapper.readValue(new File(filePrefix+fileName), Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        matrix.generateMatrix(config);
        matrix.calculateWin(betAmount);
        assertEquals(0.0,matrix.getAnswer().getReward());
    }

    @Test
    void calculateWinWithOnlyMiss() {
        fileName="configTestMissOnly.json";
        Config config;
        try {
            config = objectMapper.readValue(new File(filePrefix+fileName), Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        matrix.generateMatrix(config);
        matrix.calculateWin(betAmount);
        assertEquals(0.0,matrix.getAnswer().getReward());
    }
    @Test
    void calculateWinWithGuarantiedLinear() {
        fileName="configTestGuarantiedLinear.json";
        Config config;
        try {
            config = objectMapper.readValue(new File(filePrefix+fileName), Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        matrix.generateMatrix(config);
        matrix.calculateWin(betAmount);
        String[] expectedWinCombinations = new String[]{"same_symbol_3_times","same_symbols_horizontally" };
        assertEquals(500000.0,matrix.getAnswer().getReward());
        assertTrue(areArraysEqual(expectedWinCombinations,matrix.getAnswer().getApplied_winning_combinations().get("A").toArray(new String[2])));
    }




    private boolean areMatricesEqual(String[][] matrix1, String[][] matrix2) {
        if (matrix1.length != matrix2.length) {
            return false;
        }
        for (int i = 0; i < matrix1.length; i++) {
            if (matrix1[i].length != matrix2[i].length) {
                return false;
            }
            for (int j = 0; j < matrix1[i].length; j++) {
                if (!matrix1[i][j].equals(matrix2[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean areArraysEqual(String[] array1, String[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (!array1[i].equals(array2[i])) {
                return false;
            }
        }
        return true;
    }
}