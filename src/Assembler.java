
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Assembler {
    public static int pc = 0;
    private static int length = 0;
    private static ArrayList<String> lines = new ArrayList<>();
    private static HashMap<String, Integer> labels = new HashMap<>();
    private static HashMap<String, String> oppCodes = new HashMap<>();
    private static ArrayList<String> directives = new ArrayList<>();
    private static ArrayList<String> used = new ArrayList<>();
    private static ArrayList<String> formatR = new ArrayList<>();
    private static ArrayList<String> formatI = new ArrayList<>();
    private static ArrayList<String> formatJ = new ArrayList<>();
    public static int limit = 0;

    public static void main() {

        preProcess();
        preProcessFormates();
        directives.add(".fill");
        directives.add(".space");
        String outputFileName = "program.mc";
        try {
            FileReader fileReader = new FileReader("program.as");
            FileWriter fileWriter = new FileWriter(outputFileName);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            // First scan:
            int chCode;
            while ((chCode = fileReader.read()) != -1) {
                String str = "";
                while ((char) chCode != '\n') {
                    str += (char) chCode;
                    chCode = fileReader.read();
                }
                str = str.trim();
                if (str.length() > 0) {
                    checkLine(str);
                    pc++;
                    length++;
                }
            }
            if (used.size() > 0) {
                System.out.println("Error!");
                System.exit(1);
            }
            //Second scan:
            pc = 0;
            int i = 0;
            while (pc < length) {
                String s = getMachineCode(pc);
                MainController.memory[i++] = Integer.parseInt(s); /*   gcgcxf*/
                s += "\n";
                writer.append(s);
                pc++;
            }
            limit = i - 1;
            fileReader.close();
            writer.close();
            System.out.println("Successful:)");

        } catch (Exception e) {
            System.out.println("Error!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void preProcess() {
        oppCodes.put("add", "0000");
        oppCodes.put("sub", "0001");
        oppCodes.put("slt", "0010");
        oppCodes.put("or", "0011");
        oppCodes.put("nand", "0100");
        oppCodes.put("addi", "0101");
        oppCodes.put("slti", "0110");
        oppCodes.put("ori", "0111");
        oppCodes.put("lui", "1000");
        oppCodes.put("lw", "1001");
        oppCodes.put("sw", "1010");
        oppCodes.put("beq", "1011");
        oppCodes.put("jalr", "1101");
        oppCodes.put("j", "1101");
        oppCodes.put("halt", "1110");
    }

    private static void preProcessFormates() {
        formatR.add("add");
        formatR.add("sub");
        formatR.add("slt");
        formatR.add("or");
        formatR.add("nand");

        formatI.add("addi");
        formatI.add("ori");
        formatI.add("slti");
        formatI.add("bne");
        formatI.add("beq");
        formatI.add("sw");
        formatI.add("lw");
        formatI.add("jalr");
        formatI.add("lui");

        formatJ.add("j");
        formatJ.add("halt");
    }

    private static void checkLine(String str) {
        str = str.toLowerCase().replace(" ", ",");
        String[] arr = str.split(",");
        lines.add(str);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals("#")) {
                break;
            }
            if (arr[i].length() > 0) {
                if ((Pattern.matches("\\d+", arr[i]) || Pattern.matches("\\w+", arr[i]))
                        && !checkExpression(arr[i], i == 0)) {
                    System.out.println("No:" + arr[i]);
                    System.exit(1);
                }
            }
        }
    }

    private static boolean checkExpression(String exp, boolean isLabel) {
        if (oppCodes.containsKey(exp)) {
            return true;
        }
        if (Pattern.matches("\\d+", exp) && !isLabel) {
            return Integer.parseInt(exp) <= 65535;
        } else if (directives.contains(exp)) {
            return true;
        } else if (isLabel) {
            labels.put(exp, pc);
            if (used.contains(exp)) {
                used.remove(exp);
            }
            return true;
        } else {
            if (used.contains(exp)) {
                return false;
            }
            if (!labels.containsKey(exp)) {
                used.add(exp);
                return true;
            }
        }
        return true;
    }

    public static String getBinaryCode(int pc) {
        String binary;
        String answer;
        String line = lines.get(pc).toLowerCase();
        ArrayList<String> splitedList = getSplitedList(line);

        if (labels.containsKey(splitedList.get(0))) { //remove first index for labeled instructions
            splitedList.remove(0);
        }
        //generate oppcode for kinds of formats
        if (formatR.contains(splitedList.get(0))) {
            binary = "0000" + oppCodes.get(splitedList.get(0)) +
                    Utilities.getBinaryWithDigits(Integer.parseInt(splitedList.get(2)), 4) +
                    Utilities.getBinaryWithDigits(Integer.parseInt(splitedList.get(3)), 4) +
                    Utilities.getBinaryWithDigits(Integer.parseInt(splitedList.get(1)), 4) +
                    "000000000000";

        } else if (formatI.contains(splitedList.get(0))) {

            if (splitedList.get(0).equals("lui")) {//lui instruction is exception in rs field
                splitedList.add(2, "0");
            }
            String offset;
            int offsetindex = 3;

            if (Utilities.isNumeric(splitedList.get(offsetindex))) {// check offset is label or a number
                if (splitedList.get(0).equals("beq")) {
                    offset = String.valueOf(Integer.valueOf(splitedList.get(offsetindex)) - pc - 1);
                } else {
                    offset = (splitedList.get(offsetindex));
                }
            } else {
                if (splitedList.get(0).equals("beq")) {
                    offset = Integer.valueOf(labels.get(splitedList.get(offsetindex)) - pc - 1).toString();
                } else {
                    offset = labels.get(splitedList.get(offsetindex)).toString();

                }
            }

            if (splitedList.get(0).equals("jalr")) {//jalr instruction is exception in rs offset field
                offset = "00000000000000000000";
            }
            binary = "0000" + oppCodes.get(splitedList.get(0)) +
                    Utilities.getBinaryWithDigits(Integer.parseInt(splitedList.get(2)), 4) +
                    Utilities.getBinaryWithDigits(Integer.parseInt(splitedList.get(1)), 4) +
                    Utilities.getBinaryWithDigits(Integer.parseInt(offset), 16);
        } else if (formatJ.contains(splitedList.get(0))) {
            // halt instruction end program and machine code is always constant
            if (splitedList.get(0).equals("halt")) {
                binary = "1110000000000000000000000000";
            } else {
                binary = "0000" + oppCodes.get(splitedList.get(0)) + "00000000" +
                        Utilities.getBinaryWithDigits(Integer.parseInt(labels.get(splitedList.get(1)).toString()), 16);
            }
        } else {
            // print directives
            if (Utilities.isNumeric(splitedList.get(splitedList.size() - 1))) {// check offset is label or a number
                answer = splitedList.get(splitedList.size() - 1);
            } else {
                answer = labels.get(splitedList.get(splitedList.size() - 1)).toString();
            }

            System.out.println(answer);
            return answer;
        }
        return binary;
    }

    public static String getMachineCode(int pc) {
        String binary = getBinaryCode(pc);
        String answer = "";
        binary = binary.replaceFirst("^0+(?!$)", ""); //remove zero from fist of binary
        answer = String.valueOf(Utilities.getDecimal(binary));
        System.out.println(answer);

        return answer;
    }

    private static ArrayList<String> getSplitedList(String str) {
        if (str.contains("#")) { //delete from # to end
            str = str.substring(0, str.indexOf("#"));
        }
        ArrayList<String> splitedArray = new ArrayList<>(Arrays.asList(str.split(","))); // Create an ArrayList object

        for (int i = 0; i < splitedArray.size(); i++) {
            if (splitedArray.get(i).equals("")) {
                splitedArray.remove(i);
                i = 0;
            }
        }
        return splitedArray;
    }

}
