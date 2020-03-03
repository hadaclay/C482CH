package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;  
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.Inventory;
import Model.Part;
import Model.InhousePart;
import Model.OutsourcedPart;

/**
 * FXML Controller class
 *
 * @author Clayton Hadaway
 */
public class AddParts implements Initializable {

    @FXML
    private Button addPartsSave;
    @FXML
    private Button addPartsCancel;
    @FXML
    private AnchorPane addPartScreen;
    @FXML
    private TextField partIdBox;
    @FXML
    private TextField partNameBox;
    @FXML
    private TextField partInvBox;
    @FXML
    private TextField partPriceBox;
    @FXML
    private TextField partMinBox;
    @FXML
    private TextField partMaxBox;
    @FXML
    private RadioButton radioAddPartInHouse;
    @FXML
    private RadioButton radioAddPartOutsourced;
    @FXML
    private Label addPartLabelSwitch;
    @FXML
    private TextField addPartPromptSwitch;
    
    private boolean isInhousePart = true;
    private int partID;
    private String errMsg = new String();
    private ObservableList<Part> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partID = Inventory.generatePartID();
        partIdBox.setPromptText(Integer.toString(partID));
    }
    
    @FXML
    private void handleInHouseRadio(ActionEvent event) {
        isInhousePart = true;
        addPartLabelSwitch.setText("Machine ID");
        addPartPromptSwitch.setPromptText("Machine ID");
        radioAddPartOutsourced.setSelected(false);
    }

    @FXML
    private void handleOutsourcedRadio(ActionEvent event) {
        isInhousePart = false;
        addPartLabelSwitch.setText("Company Name");
        addPartPromptSwitch.setPromptText("Company Name");
        radioAddPartInHouse.setSelected(false);
    }
    
    @FXML
    private void handleAddPartsCancel(ActionEvent event) throws IOException {
        Parent mainScreen = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        Scene mainScreenScene = new Scene(mainScreen);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(mainScreenScene);
        window.show();
    }

    @FXML
    private void handleAddPartsSave(ActionEvent event) throws IOException {
        String name = partNameBox.getText();
        String inv = partInvBox.getText();
        String price = partPriceBox.getText();
        String min = partMinBox.getText();
        String max = partMaxBox.getText();
        String labelText = addPartPromptSwitch.getText();
                        
        try {
            errMsg = Part.checkValid(name, Double.parseDouble(price), Integer.parseInt(inv), Integer.parseInt(min), Integer.parseInt(max), errMsg);
            if (errMsg.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Part");
                alert.setContentText(errMsg);
                alert.showAndWait();
                errMsg = "";
            } else {
                if (isInhousePart) {
                    // Inhouse Parts
                    InhousePart newPart = new InhousePart();
                    newPart.setPartId(partID);
                    newPart.setName(name);
                    newPart.setMin(Integer.parseInt(min));
                    newPart.setMax(Integer.parseInt(max));
                    newPart.setPrice(Double.parseDouble(price));
                    newPart.setStock(Integer.parseInt(inv));
                    newPart.setMachineID(Integer.parseInt(labelText));
                    Inventory.addPart(newPart);
                } else {
                    // Outsourced Parts
                    OutsourcedPart outPart = new OutsourcedPart();
                    outPart.setPartId(partID);
                    outPart.setName(name);
                    outPart.setPrice(Double.parseDouble(price));
                    outPart.setStock(Integer.parseInt(inv));
                    outPart.setMin(Integer.parseInt(min));
                    outPart.setMax(Integer.parseInt(max));
                    outPart.setCompanyName(labelText);
                    Inventory.addPart(outPart);
                }

                Parent mainScreen = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreen);
        
                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

                window.setScene(mainScreenScene);
                window.show();
            }
        } catch (IOException | NumberFormatException err) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Part Add Error");
            alert.setContentText("Please fill out all fields.");
            alert.showAndWait();
        }
    }
}
