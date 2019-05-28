import javafx.stage.Stage;

public class MainController {
    private boolean RegDst = false, RegWrite = false, ALUSrc = false, PCSrc = false, MemRead = false,
            MemWrite = false, MemtoReg = false, Branch = false;
    private int registers[] = new int[32];
    private int times_used_register[] = new int[32];
    private static int mem_cells_used = 0;
    static Stage myStage;

    static void execute() {
        Assembler.main();
        ThreadHelper threadHelper = new ThreadHelper();
        threadHelper.run();
    }

    private static int getMemUsed() {
        // TODO: Iterate over memory to find out how many cells of that have been used?
        return 0;
    }

    private static void setRegisters(String code) {
        // TODO: set each register's value
    }

    private static void setTimesOfUsing(int register_id) {
        // TODO: calculate how many times one register has been used
    }

    private static void setSignals(String code) {
        // TODO: set each signal whether is false or true
    }

    private static void changeImageViews() {
        // TODO: set each imageView whether is enable or disable
    }

    public static class ThreadHelper implements Runnable{
        @Override
        public void run() {

            for (int pc = 0; pc < 10; pc++) {
                if (pc > Assembler.limit) {
                    break;
                }
                String code = Assembler.getBinaryCode(pc);
                System.out.println(code);
                setSignals(code);
                changeImageViews();
                setRegisters(code);
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

