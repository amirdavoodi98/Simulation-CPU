
public class Registers {

    public static void setupRegisters(){

        for(int i = 0;i<MainController.registers.length;i++){

            MainController.registers[i] = 0;

        }
    }
    public static void setTimesOfUsing(int register_id) {        // TODO: calculate how many times one register has been used

        MainController.times_used_register[register_id] +=1;
    }


    public static void readRegisters() {

        String   readReg1Adress = MainController.instructionCode.substring(8,12);
        String   readReg2Adress = MainController.instructionCode.substring(12,16);

        MainController.readReg1 =  MainController.registers[(int)Utilities.getDecimal(readReg1Adress)];
        MainController.readReg2 =  MainController.registers[(int)Utilities.getDecimal(readReg2Adress)];
        setTimesOfUsing((int)Utilities.getDecimal(readReg1Adress));
        setTimesOfUsing((int)Utilities.getDecimal(readReg2Adress));

    }

    public static void writeBack(){
        int address = getWrtiteAdress();
        if(MainController.RegWrite){
            setTimesOfUsing(address);

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
            writeRegAdress = (int) Utilities.getDecimal(MainController.instructionCode.substring(16,20));
        }else{
            writeRegAdress = (int) Utilities.getDecimal(MainController.instructionCode.substring(12,16));
        }

        if (MainController.Jalr) {
            writeRegAdress = 15;
        }
        if(MainController.Lui){
            writeRegAdress = (int) Utilities.getDecimal(MainController.instructionCode.substring(12,16));

        }


        return writeRegAdress;
    }

}
