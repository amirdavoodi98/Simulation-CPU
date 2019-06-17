//package com.company;

public class Memory {

    public static void memoryManager(){
        if(MainController.MemRead){
            MainController.readMemory = MainController.memory[MainController.ALUResult];
        }
        if(MainController.MemWrite){

            MainController.memory[MainController.ALUResult] = MainController.readReg2;

        }

    }
}
