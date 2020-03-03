package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static View_Controller.MainScreen.getModifyProductIndex;
import static Model.Inventory.getAllParts;
import static Model.Inventory.getAllProducts;

import Model.Inventory;
import Model.Product;
import Model.Part;

/**
 * FXML Controller class
 *
 * @author Clayton Hadaway
 */
public class ModifyProduct implements Initializable {

    @FXML
    private Button modifyProductCancel;
    @FXML
    private AnchorPane modifyProductScreen;
    @FXML
    private TableView<Part> modifyProductAddTable;
    @FXML
    private TableColumn<Part, Integer> modifyProductAddIdCol;
    @FXML
    private TableColumn<Part, String> modifyProductAddNameCol;
    @FXML
    private TableColumn<Part, Integer> modifyProductAddInvCol;
    @FXML
    private TableColumn<Part, Double> modifyProductAddPriceCol;
    @FXML
    private TableView<Part> modifyProductDeleteTable;
    @FXML
    private TableColumn<Part, Integer> modifyProductDelIdCol;
    @FXML
    private TableColumn<Part, String> modifyProductDelNameCol;
    @FXML
    private TableColumn<Part, Integer> modifyProductDelInvCol;
    @FXML
    private TableColumn<Part, Double> modifyProductDelPriceCol;
    @FXML
    private TextField modifyProductNameBox;
    @FXML
    private TextField modifyProductInvBox;
    @FXML
    private TextField modifyProductPriceBox;
    @FXML
    private TextField modifyProductMinBox;
    @FXML
    private TextField modifyProductMaxBox;
    @FXML
    private TextField modifyProductIdBox;
    @FXML
    private TextField modifyProductSearchBox;
    @FXML
    private Button modifyProductSearchButton;
    @FXML
    private Button modifyProductSearchClear;
    
    private ObservableList<Part> partsToModify = FXCollections.observableArrayList();
    private final int modifyProductIndex = getModifyProductIndex();
    private int modifiedProductID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Product productToModify = getAllProducts().get(modifyProductIndex);
        modifiedProductID = getAllProducts().get(modifyProductIndex).getId();
        modifyProductIdBox.setPromptText(Integer.toString(modifiedProductID));
        modifyProductNameBox.setText(productToModify.getName());
        modifyProductInvBox.setText(Integer.toString(productToModify.getStock()));
        modifyProductPriceBox.setText(Double.toString(productToModify.getPrice()));
        modifyProductMinBox.setText(Integer.toString(productToModify.getMin()));
        modifyProductMaxBox.setText(Integer.toString(productToModify.getMax()));
        partsToModify = productToModify.getAllAssociatedParts();
        modifyProductAddIdCol.setCellValueFactory(cellData -> cellData.getValue().partIdProperty().asObject());
        modifyProductAddNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        modifyProductAddInvCol.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        modifyProductAddPriceCol.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        modifyProductDelIdCol.setCellValueFactory(cellData -> cellData.getValue().partIdProperty().asObject());
        modifyProductDelNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        modifyProductDelInvCol.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        modifyProductDelPriceCol.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        updateAddTable();
        updateDeleteTable();
    }
    
    public void updateAddTable() {
        modifyProductAddTable.setItems(getAllParts());
    }
    
    public void updateDeleteTable() {
        modifyProductDeleteTable.setItems(partsToModify);
    }

    @FXML
    private void handleModifyProductCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to cancel modifying the product?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent mainScreen = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            Scene mainScreenScene = new Scene(mainScreen);

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

            window.setScene(mainScreenScene);
            window.show();
        }
    }

    @FXML
    private void handleModifyProductAdd(ActionEvent event) {
        Part addPart = modifyProductAddTable.getSelectionModel().getSelectedItem();
        partsToModify.add(addPart);
        updateDeleteTable();
    }

    @FXML
    private void handleModifyProductDelete(ActionEvent event) {
        Part deletePart = modifyProductDeleteTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Part Deletion");
        alert.setHeaderText("Confirm");
        alert.setContentText("Delete " + deletePart.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            partsToModify.remove(deletePart);
        }
    }

    @FXML
    private void handleModifyProductSave(ActionEvent event) {
        String name = modifyProductNameBox.getText();
        String inv = modifyProductInvBox.getText();
        String price = modifyProductPriceBox.getText();
        String min = modifyProductMinBox.getText();
        String max = modifyProductMaxBox.getText();
        
        try {
            if (partsToModify.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Modification Error");
                alert.setHeaderText("Products must have one or more associated parts.");
                alert.showAndWait();
            } else {
                Product newProduct = new Product();
                newProduct.setId(modifiedProductID);
                newProduct.setName(name);
                newProduct.setStock(Integer.parseInt(inv));
                newProduct.setPrice(Double.parseDouble(price));
                newProduct.setMin(Integer.parseInt(min));
                newProduct.setMax(Integer.parseInt(max));
                newProduct.addAssociatedParts(partsToModify);
                Inventory.updateProduct(modifyProductIndex, newProduct);

                Parent mainScreen = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreen);

                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

                window.setScene(mainScreenScene);
                window.show();
            }
            
        } catch (IOException | NumberFormatException err) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Product Modification Error");
            alert.setContentText("Please fill out all fields.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleModifyProductSearch(ActionEvent event) {
        String searchPart = modifyProductSearchBox.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error.");
            alert.setHeaderText("No parts found.");
            alert.setContentText("No parts found for query '" + searchPart + "'.");
            alert.showAndWait();
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = getAllParts().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            modifyProductAddTable.setItems(tempPartList);
        }
    }

    @FXML
    private void handleModifyPartSearchClear(ActionEvent event) {
        modifyProductSearchBox.clear();
        updateAddTable();
    }
    
}
