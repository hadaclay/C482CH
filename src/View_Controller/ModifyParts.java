package View_Controller;

import Model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import java.util.Optional;

import Model.InhousePart;
import Model.Inventory;
import Model.OutsourcedPart;

import static Model.Inventory.getAllParts;
import static View_Controller.MainScreen.getModifyPartIndex;

/**
 * FXML Controller class
 *
 * @author Clayton Hadaway
 */
public class ModifyParts implements Initializable {

    @FXML
    private Button modifyPartsSave;
    @FXML
    private Button modifyPartsCancel;
    @FXML
    private AnchorPane modifyPartScreen;
    @FXML
    private RadioButton modifyInHouseRadio;
    @FXML
    private RadioButton modifyOutsourcedRadio;
    @FXML
    private Label modifyPartSwitchLabel;
    @FXML
    private TextField modifyPartIDBox;
    @FXML
    private TextField modifyPartNameBox;
    @FXML
    private TextField modifyPartInvBox;
    @FXML
    private TextField modifyPartPriceBox;
    @FXML
    private TextField modifyPartMinBox;
    @FXML
    private TextField modifyPartSwitchBox;
    @FXML
    private TextField modifyPartMaxBox;
    @FXML
    private RadioButton modifyInhouseRadio;
    
    private boolean isInhousePart;
    private int partID;
    private final int modifiedPartIndex = getModifyPartIndex();
    private String errMsg = new String();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Part modifiedPart = getAllParts().get(modifiedPartIndex);
        partID = getAllParts().get(modifiedPartIndex).getId();
        modifyPartIDBox.setText(Integer.toString(partID));
        modifyPartNameBox.setText(modifiedPart.getName());
        modifyPartInvBox.setText(Integer.toString(modifiedPart.getStock()));
        modifyPartPriceBox.setText(Double.toString(modifiedPart.getPrice()));
        modifyPartMinBox.setText(Integer.toString(modifiedPart.getMin()));
        modifyPartMaxBox.setText(Integer.toString(modifiedPart.getMax()));
                
        if (modifiedPart instanceof InhousePart) {
            modifyPartSwitchLabel.setText("Machine ID");
            modifyPartSwitchBox.setText(Integer.toString(((InhousePart) getAllParts().get(modifiedPartIndex)).getMachineID()));
            modifyInHouseRadio.setSelected(true);
        } else {
            modifyPartSwitchLabel.setText("Company Name");
            modifyPartSwitchBox.setText(((OutsourcedPart) getAllParts().get(modifiedPartIndex)).getCompanyName());
            modifyOutsourcedRadio.setSelected(true);
        }
    }
    
    @FXML
    private void handleInHouseRadio(ActionEvent event) {
        isInhousePart = true;
        modifyInHouseRadio.setSelected(true);
        modifyOutsourcedRadio.setSelected(false);
        modifyPartSwitchLabel.setText("Machine ID");
        modifyPartSwitchBox.setPromptText("Machine ID");
    }
    
    @FXML
    private void handleOutsourcedRadio(ActionEvent event) {
        isInhousePart = false;
        modifyOutsourcedRadio.setSelected(true);
        modifyInHouseRadio.setSelected(false);
        modifyPartSwitchLabel.setText("Company Name");
        modifyPartSwitchBox.setPromptText("Company Name");
    }

    @FXML
    private void handleModifyPartSave(ActionEvent event) throws IOException {
        String name = modifyPartNameBox.getText();
        String inv = modifyPartInvBox.getText();
        String price = modifyPartPriceBox.getText();
        String min = modifyPartMinBox.getText();
        String max = modifyPartMaxBox.getText();
        String switchBoxText = modifyPartSwitchBox.getText();
        
        try {
            errMsg = Part.checkValid(name, Double.parseDouble(price), Integer.parseInt(inv), Integer.parseInt(min), Integer.parseInt(max), errMsg);
            if (errMsg.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Part Modification Error");
                alert.setContentText(errMsg);
                alert.showAndWait();
                errMsg = "";
            } else {
                if (isInhousePart) {
                    InhousePart newPart = new InhousePart();
                    newPart.setPartId(partID);
                    newPart.setName(name);
                    newPart.setStock(Integer.parseInt(inv));
                    newPart.setPrice(Double.parseDouble(price));
                    newPart.setMin(Integer.parseInt(min));
                    newPart.setMax(Integer.parseInt(max));
                    newPart.setMachineID(Integer.parseInt(switchBoxText));
                    Inventory.updatePart(modifiedPartIndex, newPart);
                } else {
                    OutsourcedPart newPart = new OutsourcedPart();
                    newPart.setPartId(partID);
                    newPart.setName(name);
                    newPart.setStock(Integer.parseInt(inv));
                    newPart.setPrice(Double.parseDouble(price));
                    newPart.setMin(Integer.parseInt(min));
                    newPart.setMax(Integer.parseInt(max));
                    newPart.setCompanyName(switchBoxText);
                    Inventory.updatePart(modifiedPartIndex, newPart);
                }

                Parent mainScreen = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreen);

                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

                window.setScene(mainScreenScene);
                window.show();
            }
        } catch (IOException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Part Modification Error");
            alert.setContentText("Please fill out all fields.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleModifyPartsCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Cancel Part Modification?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent mainScreen = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            Scene mainScreenScene = new Scene(mainScreen);

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

            window.setScene(mainScreenScene);
            window.show();
        }
    }
}