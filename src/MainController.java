
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

public class MainController implements Initializable {

    static boolean RegDst = false, RegWrite = false, ALUSrc = false, MemRead = false,
            MemWrite = false, MemtoReg = false, Branch = false, Jump = false, Jalr = false, Lui = false;
    static int pc = 0;
    static int memory[] = new int[65536];
    static int flag = 0; //halt
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
    static int instruction_counter = 0;
    File files[] = new File[]{new File("src\\StopBtn.png"),new File("src\\PlayBtn.png")};
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
    public ImageView i_mem;
    public Label pc_output;
    public ImageView registerfile;
    public ImageView alu;
    public ImageView dmem;
    public ImageView wb_mux;
    public ImageView add_with_4_adder;
    public ImageView alu_mux;
    public ImageView sign_extention;
    public ImageView control;
    public ImageView rf_mux;
    public ImageView j_sll2;
    public ImageView add_with_j_adder;
    public ImageView adder_mux;
    public ImageView last_mux;
    public ImageView adder_sll2;
    public ImageView start_btn;
    public Label register_used;
    public Label memory_used;
    public Label instructions;

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
            pc = (int) Utilities.getDecimal(instructionCode.substring(16, 32))-1;
        }

        if (Jalr) {
            pc = (int) Utilities.getDecimal(instructionCode.substring(16, 32))-1;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    static HashSet<Integer> total_registers_used = new HashSet<>();
    private int started = 1;
    public void onStart(MouseEvent mouseEvent) {
        if (started == 1) {
            Image image = new Image(getClass().getResourceAsStream("StopBtn.png"));
            start_btn.setImage(image);
            started = 0;
            execute();

        }else{
            flag = 1;
        }
    }


    public class ThreadHelper implements Runnable {
        @Override
        public void run() {
            for (pc = 0; pc < Assembler.limit; pc++) {
                if (flag == 0) {
                    instruction_counter++;
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
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    instructions.setText("instructions: " + instruction_counter);
                    float mem_used = mem_cells_used / 65536
                            , registers_used = total_registers_used.size() / 32;
                    memory_used.setText("Memory percent used: " + mem_used);
                    register_used.setText("Registers percent used: " + registers_used);
                }
            });
        }
    }
}
