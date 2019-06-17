//package com.company;

public class Memory {
    
 private static int getMemUsed() { // TODO: Iterate over memory to find out how many cells of that have been used?
      MainController.mem_cells_used++;
        return 0;
    }
    public static void memoryManager(){
        if(MainController.MemRead){
            getMemUsed();
            MainController.readMemory = MainController.memory[MainController.ALUResult];
        }
        if(MainController.MemWrite){
            getMemUsed();
            MainController.memory[MainController.ALUResult] = MainController.readReg2;

        }

    }
}
