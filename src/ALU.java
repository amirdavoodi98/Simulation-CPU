//package com.company;

public class ALU {

    public static void ALUExecution(){

        int ALUSource[] = ALUSource();
        int arg1 = ALUSource[0];
        int arg2 = ALUSource[1];

        switch (MainController.AlUOp){
            case "000": //add
                MainController.ALUResult = arg1 + arg2;
                break;

            case "001":  //sub
                MainController.ALUResult = arg1 - arg2;
                break;

            case "010": //or
                MainController.ALUResult = arg1 | arg2;
                break;

            case "110": //and
                MainController.ALUResult = arg1 & arg2;
                break;

            case "111": //slt
                if(arg1<arg2)
                    MainController.ALUResult = 1;
                else
                    MainController.ALUResult = 0;
                break;
        }

    }
    private static  int[] ALUSource(){

        int result[] = new int[2];

        result[0] = MainController.readReg1;
        if(!MainController.ALUSrc){
            result[1] = MainController.readReg2;
        }else {
            result[1] = (int) Utilities.getDecimal(MainController.instructionCode.substring(16,32));
        }
        return result;
    }
}
