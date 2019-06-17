//package com.company;

public class ControlUnit {

    public static void setSignals() {        // TODO: set each signal whether is false or true

        String  opcode = MainController.instructionCode.substring(4,8);

        switch (opcode){

            case "0000": //add R
                setRTypeSignals();
                MainController.AlUOp = "000";
                break;

            case "0001": //sub
                setRTypeSignals();
                MainController.AlUOp = "001";
                break;

            case "0010": //slt
                setRTypeSignals();
                MainController.AlUOp = "111";
                break;

            case "0100": //nand
                setRTypeSignals();
                MainController.AlUOp = "110";
                break;

            case "0011": //or
                setRTypeSignals();
                MainController.AlUOp = "010";
                break;

            case "0101": //addi
                setITypeSignals();
                MainController.AlUOp = "000";
                break;

            case "0110": //slti
                setITypeSignals();
                MainController.AlUOp = "111";
                break;

            case "0111": //ori
                setITypeSignals();
                MainController.AlUOp = "010";
                break;

            case "1000": //lui
                setSignalsfalse();
                MainController.RegWrite = true;
                MainController.Lui = true;
                MainController.AlUOp = "xxx";
                break;

            case "1001": //lw
                setSignalsfalse();
                MainController.MemtoReg = true;
                MainController.ALUSrc = true;
                MainController.RegWrite = true;
                MainController.MemRead  = true;
                MainController.AlUOp = "000";
                break;

            case "1010": //sw
                setSignalsfalse();
                MainController.MemWrite = true;
                MainController.ALUSrc = true;
                MainController.AlUOp = "000";
                break;

            case "1011": //beq
                setSignalsfalse();
                MainController.Branch = true;
                MainController.AlUOp = "001";
                break;

            case "1101": //j
                setSignalsfalse();
                MainController.Jump = true;
                MainController.AlUOp = "xxx";
                break;

            case "1100": //jalr
                setSignalsfalse();
                MainController.Jalr = true;
                MainController.RegWrite = true;
                MainController.AlUOp = "xxx";
                break;

            case "1110": //halt
                System.out.println("Halt");
                MainController.flag = 1;
//                System.exit(0);
                break;

        }

    }

    private static void setSignalsfalse(){

        MainController.RegDst = false;
        MainController.RegWrite = false;
        MainController.ALUSrc = false;
        MainController.MemRead = false;
        MainController.MemWrite = false;
        MainController.MemtoReg = false;
        MainController.Branch = false;
        MainController.Jump = false;
        MainController.Jalr = false;
        MainController.Lui = false;

    }
    private static void setITypeSignals(){

        MainController.RegDst = false;
        MainController.RegWrite = true;
        MainController.ALUSrc = true;
        MainController.MemRead = false;
        MainController.MemWrite = false;
        MainController.MemtoReg = false;
        MainController.Branch = false;
        MainController.Jump = false;
        MainController.Jalr = false;
        MainController.Lui = false;
    }

    private static void setRTypeSignals(){

        MainController.RegDst = true;
        MainController.RegWrite = true;
        MainController.ALUSrc = false;
        MainController.MemRead = false;
        MainController.MemWrite = false;
        MainController.MemtoReg = false;
        MainController.Branch = false;
        MainController.Jump = false;
        MainController.Jalr = false;
        MainController.Lui = false;
    }
}
