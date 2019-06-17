import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class WelcomeController {
    public static Stage myStage;
    public Button exe_btn;
    public TextArea text_area;
    public Button choose_file_btn;
    public Button submit_btn;
    private File file = null;

    public void onExeBtn(MouseEvent mouseEvent) throws IOException {
        if (file == null){
            return;
        }
        myStage.hide();
        Parent p = FXMLLoader.load(getClass().getResource("main.fxml"));
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("CPU Simulation");
        stage.setScene(new Scene(p));
        stage.show();
        MainController.myStage = stage;
//        MainController mainController = new MainController();
//        mainController.execute();
    }

    private File getFile() {
        JFileChooser jFileChooser = new JFileChooser();
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter( "asm or as", "asm", "as");
        jFileChooser.setFileFilter(fileFilter);
        int response = jFileChooser.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION){
            return jFileChooser.getSelectedFile();
        }
        return null;
    }

    public void onChooseBtn(MouseEvent mouseEvent) {

        File got_file = getFile();
        file = new File("program.as");
        if (got_file == null){
            return;
        }
        try {
            FileReader fileReader = new FileReader(got_file);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            int chCode;
            while ((chCode = fileReader.read()) != -1) {
                StringBuilder str = new StringBuilder();
                while ((char) chCode != '\n') {
                    str.append((char) chCode);
                    chCode = fileReader.read();
                }
                str.append("\n");
                writer.append(str);
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSubmitBtn(MouseEvent mouseEvent) {
        file = new File("program.as");
        writeFile(file);
    }

    private void writeFile(File file) {
        try {
            String text = text_area.getText();
            text += "\n";
            BufferedWriter bufferedWriter;
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(text);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
