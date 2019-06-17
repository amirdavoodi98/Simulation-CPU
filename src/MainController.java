
import javafx.stage.Stage;

public class MainController {

    public static boolean RegDst = false, RegWrite = false, ALUSrc = false, MemRead = false,
            MemWrite = false, MemtoReg = false, Branch = false , Jump = false, Jalr = false,Lui= false;

    public static int pc = 0;
    public  static int memory[] = new int[65536];
    public  static String instructionCode = "";
    public static String AlUOp = "";
    public static int readMemory = 0;
    public static int readReg1 = 0 ;
    public static int readReg2 = 0 ;
    public static int ALUResult = 0 ;

    public static int registers[] = new int[32];

    private static int times_used_register[] = new int[32];
    private static int mem_cells_used = 0;
    static Stage myStage;

    static void execute() {

        setupRegisters();//setup registers

        Assembler.main();
        ThreadHelper threadHelper = new ThreadHelper();
        threadHelper.run();
    }


    private static void checkJump(){

        if(ALUResult == 0 && Branch){
            pc += (int) Utilities.getDecimal(instructionCode.substring(16,32));
        }
        if(Jump){
            pc = (int) Utilities.getDecimal(instructionCode.substring(16,32));
        }

        if(Jalr){
            pc = (int) Utilities.getDecimal(instructionCode.substring(16,32));
        }
    }


    private static int getMemUsed() { // TODO: Iterate over memory to find out how many cells of that have been used?

        return 0;
    }
    private static void setTimesOfUsing(int register_id) {        // TODO: calculate how many times one register has been used

    }



    private static void setupRegisters(){

        for(int i = 0;i<registers.length;i++){

            registers[i] = 0;

        }
    }



    private static void changeImageViews() {
        // TODO: set each imageView whether is enable or disable
    }

    public static class ThreadHelper implements Runnable{
        @Override
        public void run() {

            for (pc = 0; pc < Assembler.limit; pc++) {

                instructionCode = Utilities.getBinaryWithDigits(Integer.parseInt(Assembler.getMachineCode(pc)),32);
                System.out.println(instructionCode);
                ControlUnit.setSignals();
                Registers.readRegisters();
                ALU.ALUExecution();
                checkJump();
                Memory.memoryManager();
                Registers.writeBack();
                changeImageViews();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mem_cells_used = getMemUsed();
        }
    }
}
