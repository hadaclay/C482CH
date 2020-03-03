package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import Model.Inventory;
import Model.Part;
import Model.Product;

import static Model.Inventory.getAllParts;
import static Model.Inventory.getAllProducts;
import static Model.Inventory.deletePart;
import static Model.Inventory.deleteProduct;
import static Model.Inventory.canDeletePart;
import static Model.Inventory.canDeleteProduct;

/**
 * FXML Controller class
 *
 * @author Clayton Hadaway
 */
public class MainScreen implements Initializable {

    @FXML
    private Button Exit;
    @FXML
    private Button partsSearch;
    @FXML
    private TableColumn<Part, Integer> partsPartID;
    @FXML
    private TableColumn<Part, String> partsPartName;
    @FXML
    private TableColumn<Part, Integer> partsInvLevel;
    @FXML
    private TableColumn<Part, Double> partsPrice;
    @FXML
    private Button partsAdd;
    @FXML
    private Button partsModify;
    @FXML
    private Button partsDelete;
    @FXML
    private Button productsSearch;
    @FXML
    private TableColumn<Product, Integer> productsPartID;
    @FXML
    private TableColumn<Product, String> productsPartName;
    @FXML
    private TableColumn<Product, Integer> productsInvLevel;
    @FXML
    private TableColumn<Product, Double> productsPrice;
    @FXML
    private Button productsAdd;
    @FXML
    private Button productsModify;
    @FXML
    private Button productsDelete;
    @FXML
    private AnchorPane mainScreen;
    @FXML
    public TableView<Part> partsTable;
    @FXML
    public TableView<Product> productsTable;
    @FXML
    private TextField partSearchBox;
    @FXML
    private TextField productSearchBox;
    @FXML
    private Button partSearchClear;
    @FXML
    private Button productSearchClear;
    
    private static Part partToModify;
    private static int modifyPartIndex;
    private static Product productToModify;
    private static int modifyProductIndex;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partsPartID.setCellValueFactory(cellData -> cellData.getValue().partIdProperty().asObject());
        partsPartName.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        partsInvLevel.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        partsPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        productsPartID.setCellValueFactory(cellData -> cellData.getValue().productIdProperty().asObject());
        productsPartName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        productsPrice.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty().asObject());
        productsInvLevel.setCellValueFactory(cellData -> cellData.getValue().productStockProperty().asObject());
        
        updatePartTable();
        updateProductTable();
    }

    @FXML
    private void handlePartSearch(ActionEvent event) {
        String searchPart = partSearchBox.getText();
        int partsIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error.");
            alert.setHeaderText("No parts found.");
            alert.setContentText("No parts found for query '" + searchPart + "'.");
            alert.showAndWait();
        } else {
            partsIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getAllParts().get(partsIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            partsTable.setItems(tempPartList);
        }
    }

    @FXML
    private void handleAddPart(ActionEvent event) throws IOException {
        Parent addParts = FXMLLoader.load(getClass().getResource("/View_Controller/AddParts.fxml"));
        Scene addPartsScene = new Scene(addParts);
                
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(addPartsScene);
        window.show();
    }

    @FXML
    private void handleModifyPart(ActionEvent event) throws IOException {
        partToModify = partsTable.getSelectionModel().getSelectedItem();
        modifyPartIndex = getAllParts().indexOf(partToModify);
        
        Parent modifyPartsPage = FXMLLoader.load(getClass().getResource("/View_Controller/ModifyParts.fxml"));
        Scene modifyPartsScene = new Scene(modifyPartsPage);
                
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(modifyPartsScene);
        window.show();
    }
    
    // Static methods for retrieving indices for part modification
    public static int getModifyPartIndex() {
        return modifyPartIndex;
    }
    
    public static int getModifyProductIndex() {
        return modifyProductIndex;
    }

    @FXML
    private void handleDeletePart(ActionEvent event) {
        Part partToDelete = partsTable.getSelectionModel().getSelectedItem();
        if (canDeletePart(partToDelete)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deletion Error");
            alert.setHeaderText("Part cannot be deleted!");
            alert.setContentText("Part is being used by one or more products.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Part Deletion");
            alert.setHeaderText("Confirm Delete");
            alert.setContentText("Delete " + partToDelete.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                deletePart(partToDelete);
                updatePartTable();
            }
        }
    }

    @FXML
    private void handleProductSearch(ActionEvent event) {
        String searchProduct = productSearchBox.getText();
        int productIndex = -1;
        if (Inventory.lookupProduct(searchProduct) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("No products found.");
            alert.setContentText("No parts found for query '" + searchProduct + "'.");          
            alert.showAndWait();
        } else {
            productIndex = Inventory.lookupProduct(searchProduct);
            Product tempProduct = Inventory.getAllProducts().get(productIndex);
            ObservableList<Product> tempProductList = FXCollections.observableArrayList();
            tempProductList.add(tempProduct);
            productsTable.setItems(tempProductList);
        }
    }

    @FXML
    private void handleAddProduct(ActionEvent event) throws IOException {
        Parent addProduct = FXMLLoader.load(getClass().getResource("/View_Controller/AddProduct.fxml"));
        Scene addProductScene = new Scene(addProduct);
                
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(addProductScene);
        window.show();
    }

    @FXML
    private void handleModifyProduct(ActionEvent event) throws IOException {
        productToModify = productsTable.getSelectionModel().getSelectedItem();
        modifyProductIndex = getAllProducts().indexOf(productToModify);
        
        
        
        Parent modifyProduct = FXMLLoader.load(getClass().getResource("/View_Controller/ModifyProduct.fxml"));
        Scene modifyProductScene = new Scene(modifyProduct);
                
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(modifyProductScene);
        window.show();
    }

    @FXML
    private void handleProductDelete(ActionEvent event) {
        Product product = productsTable.getSelectionModel().getSelectedItem();
        
        if(canDeleteProduct(product)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deletion Error");
            alert.setHeaderText("Unable to Delete Product");
            alert.setContentText("Product has one or more associated part.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Product Deletion");
            alert.setHeaderText("Confirm Delete?");
            alert.setContentText("Are you sure you want to delete " + product.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                deleteProduct(product);
                updateProductTable();
            }
        }
    }
    
    // Methods for clearing a part or product search
    @FXML
    private void handlePartSearchClear(ActionEvent event) {
        partSearchBox.clear();
        updatePartTable();
    }

    @FXML
    private void handleProductSearchClear(ActionEvent event) {
        productSearchBox.clear();
        updateProductTable();
    }
    
    // Refresh the MainScreen part/product tables
    public void updatePartTable() {
        partsTable.setItems(getAllParts());
    }
    
    public void updateProductTable() {
        productsTable.setItems(getAllProducts());
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Exit?");
        alert.setTitle("Confirm Exit");
        alert.setContentText("Would you like to exit the program?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) { System.exit(0); }
    }
}
