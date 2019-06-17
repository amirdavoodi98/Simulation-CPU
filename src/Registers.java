//package com.company;

public class Registers {

    public static void readRegisters() {

        String   readReg1Adress = MainController.instructionCode.substring(9,14);
        String   readReg2Adress = MainController.instructionCode.substring(15,20);

        MainController.readReg1 =  MainController.registers[(int)Utilities.getDecimal(readReg1Adress)];
        MainController.readReg2 =  MainController.registers[(int)Utilities.getDecimal(readReg2Adress)];

    }

    public static void writeBack(){
        int address = getWrtiteAdress();
        if(MainController.RegWrite){
            if(MainController.MemtoReg) {
                MainController.registers[address] = MainController.readMemory;
            }else {
                MainController.registers[address] = MainController.ALUResult;
            }
            if(MainController.Jalr){
                MainController.registers[address] = MainController.pc+1;
            }
            if(MainController.Lui) {
                MainController.registers[address] = (int) Utilities.getDecimal(MainController.instructionCode.substring(16,32));
            }
        }

    }
    private static int getWrtiteAdress(){

        int  writeRegAdress = 0;

        if (MainController.RegDst) {
            writeRegAdress = (int) Utilities.getDecimal(MainController.instructionCode.substring(20,25));
        }else{
            writeRegAdress = (int) Utilities.getDecimal(MainController.instructionCode.substring(15,20));
        }

        if (MainController.Jalr) {
            writeRegAdress = 31;
        }
        if(MainController.Lui){
            writeRegAdress = (int) Utilities.getDecimal(MainController.instructionCode.substring(14,19));

        }


        return writeRegAdress;
    }

}
