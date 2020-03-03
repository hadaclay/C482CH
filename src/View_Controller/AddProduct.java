package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Optional;
import Model.Part;
import Model.Product;
import Model.Inventory;
import static Model.Inventory.getAllParts;

/**
 * FXML Controller class
 *
 * @author Clayton Hadaway
 */
public class AddProduct implements Initializable {

    @FXML
    private Button addProductCancel;
    @FXML
    private AnchorPane addProductScreen;
    @FXML
    private TextField addProductPromptSwitch;
    @FXML
    private TextField productNameBox;
    @FXML
    private TextField productInvBox;
    @FXML
    private TextField productCostBox;
    @FXML
    private TextField productMinBox;
    @FXML
    private TextField productMaxBox;
    @FXML
    private TableView<Part> productAddTable;
    @FXML
    private TableColumn<Part, Integer> addProductIdColumn;
    @FXML
    private TableColumn<Part, String> addProductNameColumn;
    @FXML
    private TableColumn<Part, Integer> addProductInvColumn;
    @FXML
    private TableColumn<Part, Double> addProductPriceColumn;
    @FXML
    private TableView<Part> productDeleteTable;
    @FXML
    private TableColumn<Part, Integer> deleteProductIdColumn;
    @FXML
    private TableColumn<Part, String> deleteProductNameColumn;
    @FXML
    private TableColumn<Part, Integer> deleteProductInvColumn;
    @FXML
    private TableColumn<Part, Double> deleteProductPriceColumn;
    @FXML
    private TextField addProductSearchBox;
    @FXML
    private Button addProductSearchButton;
    
    private ObservableList<Part> productParts = FXCollections.observableArrayList();
    private String errMsg = new String();
    private int productID;
    @FXML
    private Button addProductClearSearch;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addProductIdColumn.setCellValueFactory(cellData -> cellData.getValue().partIdProperty().asObject());
        addProductNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        addProductInvColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        addProductPriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        deleteProductIdColumn.setCellValueFactory(cellData -> cellData.getValue().partIdProperty().asObject());
        deleteProductNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        deleteProductInvColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        deleteProductPriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        
        updateAddPartsTable();
        updateDeletePartsTable();
        
        productID = Inventory.generateProductID();
        addProductPromptSwitch.setPromptText(Integer.toString(productID));
    }
    
    public void updateAddPartsTable() {
        productAddTable.setItems(getAllParts());
    }
    
    public void updateDeletePartsTable() {
        productDeleteTable.setItems(productParts);
    }
    
    @FXML
    private void handleAddProductCancel(ActionEvent event) throws IOException {
        Parent mainScreen = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        Scene mainScreenScene = new Scene(mainScreen);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(mainScreenScene);
        window.show();
    }

    @FXML
    private void handleAddProductAdd(ActionEvent event) {
        Part part = productAddTable.getSelectionModel().getSelectedItem();
        productParts.add(part);
        updateAddPartsTable();
    }

    @FXML
    private void handleAddProductDelete(ActionEvent event) {
        Part part = productDeleteTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete a Part");
        alert.setHeaderText("Please Confirm");
        alert.setContentText("Are you sure you want to delete " + part.getName() + " from part list?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            productParts.remove(part);
        }
    }

    @FXML
    private void handleAddProductSave(ActionEvent event) {
        String name = productNameBox.getText();
        String inv = productInvBox.getText();
        String price = productCostBox.getText();
        String min = productMinBox.getText();
        String max = productMaxBox.getText();
        
        try {
            if (productParts.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Product Add Error");
                alert.setHeaderText("Products must have at least one part.");
                alert.showAndWait();
            } else {
                Product newProduct = new Product();
                newProduct.setId(productID);
                newProduct.setName(name);
                newProduct.setStock(Integer.parseInt(inv));
                newProduct.setPrice(Double.parseDouble(price));
                newProduct.setMin(Integer.parseInt(min));
                newProduct.setMax(Integer.parseInt(max));
                newProduct.addAssociatedParts(productParts);
                Inventory.addProduct(newProduct);
                
                Parent mainScreen = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreen);

                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

                window.setScene(mainScreenScene);
                window.show();
            }
        } catch (IOException | NumberFormatException err) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Product Error");
            alert.setContentText("Please fill out all fields.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAddProductSearch(ActionEvent event) {
        String searchPart = addProductSearchBox.getText();
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
            productAddTable.setItems(tempPartList);
        }
    }  

    @FXML
    private void handleAddProductClearSearch(ActionEvent event) {
        addProductSearchBox.clear();
        updateAddPartsTable();
    }
}
