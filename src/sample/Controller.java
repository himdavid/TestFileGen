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
        delimiterCharacter.setVisible(false);
        baselineFilePath.setVisible(false);
        configFilePath.setVisible(false);
        magicButton.setDisable(true);
    }

    public void delimiterChange(ActionEvent event) {
        String delimiterType = delimiter.getValue();
        if(delimiterType.equals("Delimited")) {
            delimiterCharacter.setVisible(true);
            configFileButton.setDisable(true);
            step3Label.setDisable(true);
            step3Label.setText("NA");
            step4Label.setText("Step 3:");
        } else {
            delimiterCharacter.setVisible(false);
            delimiterStatusLabel.setTextFill(Color.GREEN);
            configFileButton.setDisable(false);
            step3Label.setDisable(false);
            step3Label.setText("Step 3:");
            step4Label.setText("Step 4:");
        }

        delimiterLabel.setText(delimiterType);
        enableMagicButton();
    }

    public void delimiterCharacterChange(ActionEvent event) {
        String delimiterCharacterText = delimiterCharacter.getText();

        if(!delimiterCharacterText.trim().equals("")) {
            delimiterLabel.setText(delimiter.getValue() + ": " + delimiterCharacterText);
            delimiterStatusLabel.setTextFill(Color.GREEN);
            enableMagicButton();
        } else {
            delimiterStatusLabel.setTextFill(Color.RED);
        }


    }

    public void uploadBaselineFileAction(ActionEvent event) {
        FileChooser fileChooserBaseline = new FileChooser();
        fileChooserBaseline.setTitle("Select your baseline file");
        fileChooserBaseline.setInitialDirectory(new File(System.getProperty("user.home")));

        File baselineFile = fileChooserBaseline.showOpenDialog(baselineFileButton.getScene().getWindow());
        String filePath = baselineFile.getAbsolutePath();
        String fileName = baselineFile.getName();
        baselineFilePath.setVisible(true);
        baselineFilePath.setText(filePath);
        baselineFileName.setText(fileName);
        baselineStatusLabel.setTextFill(Color.GREEN);
        enableMagicButton();
    }

    public void uploadConfigFileAction(ActionEvent event) {
        FileChooser fileChooserConfig = new FileChooser();
        fileChooserConfig.setTitle("Select your config file");
        fileChooserConfig.setInitialDirectory(new File(System.getProperty("user.home")));

        File configFile = fileChooserConfig.showOpenDialog(configFileButton.getScene().getWindow());
        String filePath = configFile.getAbsolutePath();
        String fileName = configFile.getName();
        configFilePath.setVisible(true);
        configFilePath.setText(filePath);
        configFileName.setText(fileName);
        configFileStatusLabel.setTextFill(Color.GREEN);
        enableMagicButton();
    }

    public void fileNameFormatChange(ActionEvent event) {
        fileNameFormatName.setText(fileNameFormatText.getText());
        fileNameFormatStatusLabel.setTextFill(Color.GREEN);
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
