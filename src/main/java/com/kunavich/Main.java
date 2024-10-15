package com.kunavich;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kunavich.entity.Config;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Matrix matrix= new Matrix();
        Config config = objectMapper.readValue(new File("config.json"), Config.class);
    }

}