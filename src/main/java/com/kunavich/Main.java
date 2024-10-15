package com.kunavich;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kunavich.entity.Config;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {

        String fileName="config.json";
        double betAmount = 1;
        if (args.length > 0) {
            System.out.println("Arguments passed to the program:");
            for (int i = 0; i < args.length; i+=2) {
                if(args[i].equals("--config")){
                    fileName=args[i+1];
                }
                if(args[i].equals("--betting-amount")){
                    betAmount=Double.parseDouble(args[i+1]);
                }
            }
        } else {
            System.out.println( "No arguments were passed.");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Matrix matrix= new Matrix();
        Config config = objectMapper.readValue(new File("config.json"), Config.class);
    }

}