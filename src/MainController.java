
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
        setOpcity();

        // TODO: set each imageView whether is enable or disable
    }

    private void setFullOpcity(){
        registerfile.setOpacity(1);
        alu.setOpacity(1);
        dmem.setOpacity(1);
        wb_mux.setOpacity(1);
        add_with_4_adder.setOpacity(1);
        alu_mux.setOpacity(1);
        sign_extention.setOpacity(1);
        control.setOpacity(1);
        rf_mux.setOpacity(1);
        j_sll2.setOpacity(1);
        add_with_j_adder.setOpacity(1);
        adder_mux.setOpacity(1);
        last_mux.setOpacity(1);
        adder_sll2.setOpacity(1);
    }
    private void setROpcity(){

        dmem.setOpacity(0.1);
        sign_extention.setOpacity(0.1);
        j_sll2.setOpacity(0.1);
        add_with_j_adder.setOpacity(0.1);
        adder_mux.setOpacity(0.1);
        //last_mux.setOpacity(0.1);
        adder_sll2.setOpacity(0.1);

    }

    private void setIOpcity(){

        dmem.setOpacity(0.1);
        j_sll2.setOpacity(0.1);
        add_with_j_adder.setOpacity(0.1);
        adder_mux.setOpacity(0.1);
        // last_mux.setOpacity(0.1);
        adder_sll2.setOpacity(0.1);


    }

    private void setOpcity() {

        String opcode = MainController.instructionCode.substring(4, 8);

        switch (opcode) {

            case "0000": //add R
                setFullOpcity();
                setROpcity();
                break;

            case "0001": //sub
                setFullOpcity();
                setROpcity();
                break;

            case "0010": //slt
                setFullOpcity();
                setROpcity();

                break;

            case "0100": //nand
                setFullOpcity();
                setROpcity();

                break;

            case "0011": //or
                setFullOpcity();
                setROpcity();

                break;

            case "0101": //addi
                setFullOpcity();
                setIOpcity();
                break;

            case "0110": //slti
                setFullOpcity();
                setIOpcity();

                break;

            case "0111": //ori
                setFullOpcity();
                setIOpcity();

                break;

            case "1000": //lui
                setFullOpcity();
                setIOpcity();

                break;

            case "1001": //lw
                setFullOpcity();
                setIOpcity();
                dmem.setOpacity(1);

                break;

            case "1010": //sw
                setFullOpcity();
                setIOpcity();
                dmem.setOpacity(1);
                wb_mux.setOpacity(0.1);

                break;

            case "1011": //beq

                setFullOpcity();
                dmem.setOpacity(0.1);
                wb_mux.setOpacity(0.1);
                adder_sll2.setOpacity(0.1);

                break;

            case "1101": //j
                setFullOpcity();
                alu.setOpacity(0.1);
                alu_mux.setOpacity(0.1);
                dmem.setOpacity(0.1);
                wb_mux.setOpacity(0.1);
                sign_extention.setOpacity(0.1);
                j_sll2.setOpacity(0.1);
                add_with_j_adder.setOpacity(0.1);
                break;

            case "1100": //jalr
                setFullOpcity();
                alu.setOpacity(0.1);
                alu_mux.setOpacity(0.1);
                dmem.setOpacity(0.1);
                wb_mux.setOpacity(0.1);
                sign_extention.setOpacity(0.1);
                j_sll2.setOpacity(0.1);
                add_with_j_adder.setOpacity(0.1);

                break;

            case "1110": //halt
                setFullOpcity();

                break;

        }
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
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    instructions.setText("instructions: " + instruction_counter);
                    double mem_used = 100 * (double)mem_cells_used / 65536
                            , registers_used = 100 * (float)total_registers_used.size() / 16;
                    memory_used.setText("Memory percent used: " + Math.floor(mem_used));
                    register_used.setText("Registers percent used: " + Math.floor(registers_used));
                    System.out.println((mem_used));
                    System.out.println(total_registers_used.size());
                }
            });
        }
    }
}
