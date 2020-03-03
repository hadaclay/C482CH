package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Clayton Hadaway
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static int partIDCount = 0;
    private static int productIDCount = 0;
    
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
    
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }
    
    public static int lookupPart(String searchTerm) {
        boolean isFound = false;
        int index = 0;
        if (integerCheck(searchTerm)) { // check if searching by number
            for (int i = 0; i < allParts.size(); i++) {
                if (Integer.parseInt(searchTerm) == allParts.get(i).getId()) {
                    index = i;
                    isFound = true;
                }
            }
        }
        else { // otherwise search by string
            for (int i = 0; i < allParts.size(); i++) {
                searchTerm = searchTerm.toLowerCase();
                if (searchTerm.equals(allParts.get(i).getName().toLowerCase())) {
                    index = i;
                    isFound = true;
                }
            }
        }

        if (isFound) { return index; }
        else {
            System.out.println("Parts not found.");
            return -1;
        }
    }
    
    public static int lookupProduct(String searchTerm) {
        boolean isFound = false;
        int index = 0;
        if (integerCheck(searchTerm)) { // check if searching by number
            for (int i = 0; i < allProducts.size(); i++) {
                if (Integer.parseInt(searchTerm) == allProducts.get(i).getId()) {
                    index = i;
                    isFound = true;
                }
            }
        } else { // otherwise search by string
            for (int i = 0; i < allProducts.size(); i++) {
                searchTerm = searchTerm.toLowerCase();
                if (searchTerm.equals(allProducts.get(i).getName().toLowerCase())) {
                    index = i;
                    isFound = true;
                }
            }
        }

        if (isFound) { 
            return index; 
        } else {
            System.out.println("Products not found.");
            return -1;
        }
    }
    
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }
    
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }
    
    public static void deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
    }
    
    public static void deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
    }
    
    public static boolean canDeletePart(Part part) {
        boolean isFound = false;
        for (int i=0; i < allProducts.size(); i++){
            if (allProducts.get(i).getAllAssociatedParts().contains(part)){
                isFound=true;
            }
        }
        return isFound;
    }
    
    public static boolean canDeleteProduct(Product product) {
        boolean isFound = false;
        int productID = product.getId();
        for (int i=0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == productID) {
                if (!allProducts.get(i).getAllAssociatedParts().isEmpty()) {
                    isFound = true;
                }
            }
        }
        return isFound;
    }
    
    public static int generatePartID() {
        partIDCount += 1;
        return partIDCount;
    }
    
    public static int generateProductID() {
        productIDCount += 1;
        return productIDCount;
    }
    
    // Helper method for testing if a string is a valid integer
    private static boolean integerCheck(String integerToCheck) {
        try {
            Integer.parseInt(integerToCheck);
            return true;
        } catch (NumberFormatException err) {
            return false;
        }
    }
}
