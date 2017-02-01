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
    private TextField delimiterCharacter;
    @FXML
    private Button baselineFileButton;
    @FXML
    private Button magicButton;
    @FXML
    private Label baselineFilePath;
    @FXML
    private Label baselineFileName;
    @FXML
    private Label baselineStatusLabel;
    @FXML
    private Label delimiterStatusLabel;
    @FXML
    private Button configFileButton;
    @FXML
    private Label configFilePath;
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
    private Label fileNameFormatStatusLabel;
    @FXML
    private Label fileNameFormatName;
    @FXML
    private TextField fileNameFormatText;
    private int requirementCounter = 0;


    ObservableList<String> delimiterList = FXCollections.observableArrayList("Delimited", "Fixed width");

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
        fileNameFormatText.setDisable(true);
        magicButton.setDisable(true);
    }

    public void delimiterChange(ActionEvent event) {
        String delimiterType = delimiter.getValue();
        if(delimiterType.equals("Delimited")) {
            step2Label.setDisable(false);
            step2Label.setText("Step 2:");
            step3Label.setText("Step 3:");
            step4Label.setText("---");
            step5Label.setText("Step 4:");
            delimiterCharacter.setDisable(false);
            step4Label.setDisable(true);
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
            if(!baselineFilePath.getText().equals("")) {
                step3Label.setDisable(false);
                step3Label.setText("Step 2:");
                step4Label.setDisable(false);
                step4Label.setText("Step 3:");
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
            fileChooserBaseline.setInitialDirectory(new File(System.getProperty("user.home")));
            File baselineFile = fileChooserBaseline.showOpenDialog(baselineFileButton.getScene().getWindow());
            String filePath = baselineFile.getAbsolutePath();
            String fileName = baselineFile.getName();
            baselineFilePath.setText(filePath);
            baselineFileName.setText(fileName);

            baselineStatusLabel.setTextFill(Color.GREEN);

            if(delimiter.getValue().equals("Fixed width")) {
                step4Label.setDisable(false);
                configFileButton.setDisable(false);
                fileNameFormatText.setDisable(false);
            } else {
                step5Label.setDisable(false);
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
            fileChooserConfig.setInitialDirectory(new File(System.getProperty("user.home")));

            File configFile = fileChooserConfig.showOpenDialog(configFileButton.getScene().getWindow());
            String filePath = configFile.getAbsolutePath();
            String fileName = configFile.getName();
            configFilePath.setText(filePath);
            configFileName.setText(fileName);
            step5Label.setDisable(false);
            configFileStatusLabel.setTextFill(Color.GREEN);
        } catch(NullPointerException ex) {
            System.out.println("Please select a config file to upload");
        }
        enableMagicButton();
    }

    public void fileNameFormatChange(ActionEvent event) {
        String fileNameText = fileNameFormatText.getText();
        fileNameFormatName.setText(fileNameText);

        if(fileNameText.trim().equals("")) {
            fileNameFormatStatusLabel.setTextFill(Color.RED);
        } else {
            fileNameFormatStatusLabel.setTextFill(Color.GREEN);
        }
        enableMagicButton();
    }

    public boolean enableMagicButton() {

        boolean requirementsMet = false;

        if(delimiter.getValue().equals("Fixed width")) {
            if (delimiterStatusLabel.getTextFill() == Color.GREEN
                    && baselineStatusLabel.getTextFill() == Color.GREEN
                    && configFileStatusLabel.getTextFill() == Color.GREEN
                    && fileNameFormatStatusLabel.getTextFill() == Color.GREEN) {
                magicButton.setDisable(false);
            } else {
                magicButton.setDisable(true);
            }
        } else if(delimiter.getValue().equals("Delimited")){
            if (delimiterStatusLabel.getTextFill() == Color.GREEN
                    && baselineStatusLabel.getTextFill() == Color.GREEN
                    && fileNameFormatStatusLabel.getTextFill() == Color.GREEN) {
                magicButton.setDisable(false);
            } else {

            }
        }
        return requirementsMet;
    }
}
