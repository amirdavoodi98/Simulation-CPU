
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

public class MainController implements Initializable {

    static boolean RegDst = false, RegWrite = false, ALUSrc = false, MemRead = false,
            MemWrite = false, MemtoReg = false, Branch = false, Jump = false, Jalr = false, Lui = false;
    static int pc = 0;
    static int memory[] = new int[65536];

    static String instructionCode = "";
    static String AlUOp = "";
    static int readMemory = 0;
    public static int readReg1 = 0;
    static int readReg2 = 0;
    public static int ALUResult = 0;
    public static int registers[] = new int[32];
    static int times_used_register[] = new int[32];
    static int mem_cells_used = 0;
    static Stage myStage;
    public ImageView pc_image;
    public Label register1;
    public Label register2;
    public Label register3;
    public Label register4;
    public Label register5;
    public Label register6;
    public Label register7;
    public Label register8;
    public Label register9;
    public Label register10;
    public Label register11;
    public Label register12;
    public Label register13;
    public Label register14;
    public Label register15;
    Service<Void> service = new Service<Void>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    //Background work
                    final CountDownLatch latch = new CountDownLatch(1);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for (pc = 0; pc < Assembler.limit; pc++) {
                                    instructionCode = Utilities.getBinaryWithDigits(Integer.parseInt(Assembler.getMachineCode(pc)), 32);
                                    System.out.println("pc:" + pc + ", " + instructionCode);
                                    ControlUnit.setSignals();
                                    Registers.readRegisters();
                                    ALU.ALUExecution();
                                    checkJump();
                                    Memory.memoryManager();
                                    Registers.writeBack();
                                    changeImageViews();
                                    Random random = new Random(10);
                                    String string = String.valueOf(random.nextInt(100));
                                    register1.setText(string);
                                    System.out.println("random : " + string);
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                System.out.println("O_o");
                            } finally {
                                latch.countDown();
                            }
                        }
                    });
                    latch.await();
                    //Keep with the background work
                    return null;
                }
            };
        }
    };

    public void execute() {
        setupRegisters();//setup registers
        Assembler.main();
        Thread threadHelper = new Thread(new ThreadHelper());
        threadHelper.start();
    }


    private static void checkJump() {

        if (ALUResult == 0 && Branch) {
            pc += (int) Utilities.getDecimal(instructionCode.substring(16, 32));
        }
        if (Jump) {
            pc = (int) Utilities.getDecimal(instructionCode.substring(16, 32));
        }

        if (Jalr) {
            pc = (int) Utilities.getDecimal(instructionCode.substring(16, 32));
        }
    }

    private static void setupRegisters() {

        for (int i = 0; i < registers.length; i++) {

            registers[i] = 0;

        }
    }


    private void changeImageViews() {
        register1.setText(String.valueOf(registers[1]));
        register2.setText(String.valueOf(registers[2]));
        register3.setText(String.valueOf(registers[3]));
        register4.setText(String.valueOf(registers[4]));
        register5.setText(String.valueOf(registers[5]));
        register6.setText(String.valueOf(registers[6]));
        register7.setText(String.valueOf(registers[7]));
        register8.setText(String.valueOf(registers[8]));
        register9.setText(String.valueOf(registers[9]));
        register10.setText(String.valueOf(registers[10]));
        register11.setText(String.valueOf(registers[11]));
        register12.setText(String.valueOf(registers[12]));
        register13.setText(String.valueOf(registers[13]));
        register14.setText(String.valueOf(registers[14]));
        register15.setText(String.valueOf(registers[15]));
        // TODO: set each imageView whether is enable or disable
    }

    public void onRegister15(MouseEvent mouseEvent) {
        execute();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    public class ThreadHelper implements Runnable {
        @Override
        public void run() {
            for (pc = 0; pc < Assembler.limit; pc++) {
                instructionCode = Utilities.getBinaryWithDigits(Integer.parseInt(Assembler.getMachineCode(pc)), 32);
                System.out.println(instructionCode);
                ControlUnit.setSignals();
                Registers.readRegisters();
                ALU.ALUExecution();
                checkJump();
                Memory.memoryManager();
                Registers.writeBack();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        changeImageViews();
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
