package Model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Clayton Hadaway
 */
public class Product {
    private ObservableList<Part> associatedParts;
    private final IntegerProperty productID;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty stock;
    private final IntegerProperty min;
    private final IntegerProperty max;
    
    public Product() {
        this.productID = new SimpleIntegerProperty(0);
        this.name = new SimpleStringProperty("");
        this.price = new SimpleDoubleProperty(0);
        this.stock = new SimpleIntegerProperty(0);
        this.min = new SimpleIntegerProperty(0);
        this.max = new SimpleIntegerProperty(0);
        this.associatedParts = FXCollections.observableArrayList();
    }
    
    // Class Properties
    public IntegerProperty productIdProperty() {
        return productID;
    }
    
    public StringProperty productNameProperty() {
        return name;
    }
    
    public DoubleProperty productPriceProperty() {
        return price;
    }
    
    public IntegerProperty productStockProperty() {
        return stock;
    }
    
    // Getters
    public int getId() {
        return this.productID.get();
    }
    
    public String getName() {
        return this.name.get();
    }
    
    public double getPrice() {
        return this.price.get();
    }
    
    public int getStock() {
        return this.stock.get();
    }
    
    public int getMin() {
        return this.min.get();
    }
    
    public int getMax() {
        return this.max.get();
    }
    
    // Setters
    public void setId(int id) {
        this.productID.set(id);
    }
    
    public void setName(String name) {
        this.name.set(name);
    }
    
    public void setPrice(double price) {
        this.price.set(price);
    }
    
    public void setStock(int stock) {
        this.stock.set(stock);
    }
    
    public void setMin(int min) {
        this.min.set(min);
    }
    
    public void setMax(int max) {
        this.max.set(max);
    }    
       
    public void addAssociatedParts(ObservableList<Part> associatedParts) {
        this.associatedParts = associatedParts;
    }
    
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
 }
