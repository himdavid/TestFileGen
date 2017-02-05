package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Label delimiterLabel;
    @FXML
    private Label step2Label;
    @FXML
    private ComboBox<String> delimiter;
    @FXML
    private ComboBox<String> outputType;
    @FXML
    private TextField delimiterCharacter;
    @FXML
    private Button baselineFileButton;
    @FXML
    private Button magicButton;
    @FXML
    private Label baselineFileName;
    @FXML
    private Label baselineStatusLabel;

    @FXML
    private Button testCaseFileButton;
    @FXML
    private Label testCaseFileName;
    @FXML
    private Label testCaseStatusLabel;


    @FXML
    private Label delimiterStatusLabel;
    @FXML
    private Button configFileButton;
    @FXML
    private Label configFileStatusLabel;
    @FXML
    private Label configFileName;
    @FXML
    private Label step3Label;
    @FXML
    private Label step4Label;
    @FXML
    private Label step5Label;
    @FXML
    private Label step6Label;
    @FXML
    private Label step7Label;
    @FXML
    private Label outputTypeStatusLabel;
    @FXML
    private Label outputTypeName;
    @FXML
    private Label fileNameFormatStatusLabel;
    @FXML
    private Label fileNameFormatName;
    @FXML
    private TextField fileNameFormatText;
    private int requirementCounter = 0;
    private String currentDirectory;
    private File file = null;


    ObservableList<String> delimiterList = FXCollections.observableArrayList("Delimited", "Fixed width");
    ObservableList<String> outputTypeList = FXCollections.observableArrayList("Single", "Multiple");
    private String baselineFilePath;
    private String testCaseFilePath;
    private String configFilePath;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        delimiter.setItems(delimiterList);
        outputType.setItems(outputTypeList);
        delimiterCharacter.setDisable(true);
        step2Label.setDisable(true);
        step2Label.setText("---");
        step3Label.setText("---");
        step3Label.setDisable(true);
        baselineFileButton.setDisable(true);
        step4Label.setDisable(true);
        step4Label.setText("---");
        step5Label.setDisable(true);
        step5Label.setText("---");
        step6Label.setText("---");
        step7Label.setText("---");
        fileNameFormatText.setDisable(true);
        magicButton.setDisable(true);
    }

    public void delimiterChange(ActionEvent event) {
        String delimiterType = delimiter.getValue();
        if(delimiterType.equals("Delimited")) {
            step2Label.setDisable(false);
            step2Label.setText("Step 2:");
            step3Label.setText("Step 3:");
            step4Label.setText("Step 4:");
            step5Label.setText("---");
            step6Label.setText("Step 5:");
            step7Label.setText("Step 6:");
            delimiterCharacter.setDisable(false);
            configFileButton.setDisable(true);
        } else {
            step2Label.setDisable(true);
            step2Label.setText("---");
            delimiterCharacter.setDisable(true);
            delimiterStatusLabel.setTextFill(Color.GREEN);
            baselineFileButton.setDisable(false);
            step3Label.setDisable(false);
            step3Label.setText("Step 2:");
            step4Label.setText("Step 3:");
            step5Label.setText("Step 4:");
            step6Label.setText("Step 5:");
            step7Label.setText("Step 6:");
            if(!testCaseFileName.getText().equals("---")) {
                step5Label.setDisable(false);
                configFileButton.setDisable(false);
            }
        }

        delimiterLabel.setText(delimiterType);
        enableMagicButton();
    }

    public void delimiterCharacterChange(ActionEvent event) {
        String delimiterCharacterText = delimiterCharacter.getText();

        if(!delimiterCharacterText.trim().equals("")) {
            delimiterLabel.setText(delimiter.getValue() + ": " + delimiterCharacterText);
            delimiterStatusLabel.setTextFill(Color.GREEN);
            step3Label.setDisable(false);
            baselineFileButton.setDisable(false);
            enableMagicButton();
        } else {
            delimiterStatusLabel.setTextFill(Color.RED);
        }
    }

    public void uploadBaselineFileAction(ActionEvent event) {
        FileChooser fileChooserBaseline;

        try {
            fileChooserBaseline = new FileChooser();
            fileChooserBaseline.setTitle("Select your baseline file");
            //fileChooserBaseline.setInitialDirectory(new File(System.getProperty("user.home")));
            setCurrentDirectory(file, fileChooserBaseline);
            File baselineFile = fileChooserBaseline.showOpenDialog(baselineFileButton.getScene().getWindow());
            file = baselineFile;
            baselineFilePath = baselineFile.getAbsolutePath();
            String fileName = baselineFile.getName();
            baselineFileName.setText(fileName);
            baselineStatusLabel.setTextFill(Color.GREEN);
            step4Label.setDisable(false);
            testCaseFileButton.setDisable(false);
            fileNameFormatText.setDisable(false);
        } catch(NullPointerException ex) {
            System.out.println("Please select a baseline file to upload");
        }
        enableMagicButton();
    }

    public void uploadTestCaseAction(ActionEvent event) {
        FileChooser fileChooserTestCase;

        try {
            fileChooserTestCase = new FileChooser();
            fileChooserTestCase.setTitle("Select your test case file");
            //fileChooserTestCase.setInitialDirectory(new File(System.getProperty("user.home")));
            setCurrentDirectory(file, fileChooserTestCase);
            File testCaseFile = fileChooserTestCase.showOpenDialog(testCaseFileButton.getScene().getWindow());
            file = testCaseFile;
            testCaseFilePath = testCaseFile.getAbsolutePath();
            String fileName = testCaseFile.getName();
            testCaseFileName.setText(fileName);
            testCaseStatusLabel.setTextFill(Color.GREEN);
            step5Label.setDisable(false);
            if(delimiter.getValue().equals("Fixed width")) {
                step5Label.setDisable(false);
                configFileButton.setDisable(false);
            } else {
                step6Label.setDisable(false);
                fileNameFormatText.setDisable(false);
            }
        } catch(NullPointerException ex) {
            System.out.println("Please select a baseline file to upload");
        }
        enableMagicButton();
    }

    public void uploadConfigFileAction(ActionEvent event) {

        try {
            FileChooser fileChooserConfig = new FileChooser();
            fileChooserConfig.setTitle("Select your config file");
            //fileChooserConfig.setInitialDirectory(new File(System.getProperty("user.home")));
            setCurrentDirectory(file, fileChooserConfig);
            File configFile = fileChooserConfig.showOpenDialog(configFileButton.getScene().getWindow());
            file = configFile;
            configFilePath = configFile.getAbsolutePath();
            String fileName = configFile.getName();
            configFileName.setText(fileName);
            step6Label.setDisable(false);
            fileNameFormatText.setDisable(false);
            configFileStatusLabel.setTextFill(Color.GREEN);
        } catch(NullPointerException ex) {
            System.out.println("Please select a config file to upload");
        }
        enableMagicButton();
    }

    public void fileNameFormatChange(ActionEvent event) {
        String fileNameText = fileNameFormatText.getText();
        fileNameFormatName.setText(fileNameText);

        if(fileNameText.trim().equals("---") || fileNameText.trim().equals("")) {
            fileNameFormatStatusLabel.setTextFill(Color.RED);
        } else {
            fileNameFormatStatusLabel.setTextFill(Color.GREEN);
        }
        step7Label.setDisable(false);
        outputType.setDisable(false);
        enableMagicButton();
    }

    public void outputTypeChange(ActionEvent event) {
        outputTypeStatusLabel.setTextFill(Color.GREEN);
        outputTypeName.setText(outputType.getValue());
        enableMagicButton();
    }

    public boolean enableMagicButton() {

        boolean requirementsMet = false;

        if(delimiter.getValue().equals("Fixed width")) {
            if (delimiterStatusLabel.getTextFill() == Color.GREEN
                    && baselineStatusLabel.getTextFill() == Color.GREEN
                    && testCaseStatusLabel.getTextFill() == Color.GREEN
                    && configFileStatusLabel.getTextFill() == Color.GREEN
                    && fileNameFormatStatusLabel.getTextFill() == Color.GREEN
                    && outputTypeStatusLabel.getTextFill() == Color.GREEN){
                magicButton.setDisable(false);
            } else {
                magicButton.setDisable(true);
            }
        } else if(delimiter.getValue().equals("Delimited")){
            if (delimiterStatusLabel.getTextFill() == Color.GREEN
                    && baselineStatusLabel.getTextFill() == Color.GREEN
                    && testCaseStatusLabel.getTextFill() == Color.GREEN
                    && fileNameFormatStatusLabel.getTextFill() == Color.GREEN
                    && outputTypeStatusLabel.getTextFill() == Color.GREEN) {
                magicButton.setDisable(false);
            } else {

            }
        }
        return requirementsMet;
    }

    public void clickMagicButton(ActionEvent event) {

        CreateTestFiles ctf = new CreateTestFiles();
        String fileNameString = fileNameFormatText.getText();
        if(outputType.getValue().equals("Multiple")) {
            if(fileNameString.contains(".")) {
                int fileNameIndex = fileNameString.lastIndexOf(".");
                fileNameString = fileNameString.substring(0, fileNameIndex) + "_[FieldName]" + fileNameString.substring(fileNameIndex, fileNameString.length());
            } else {

            }
        }

        try {

            if(delimiter.getValue().equals("Fixed width")) {
                ctf.createOutputFixedWidth(testCaseFilePath, baselineFilePath, configFilePath, fileNameString, false, false);

            } else if(delimiter.getValue().equals("Delimited")) {
                ctf.createOutput(testCaseFilePath,
                        baselineFilePath,
                        delimiterCharacter.getText(), fileNameString, outputType.getValue());
            } else {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(File file, FileChooser fileChooser) {
        if(file != null){
            File existDirectory = file.getParentFile();
            fileChooser.setInitialDirectory(existDirectory);
        } else {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }
    }

}
