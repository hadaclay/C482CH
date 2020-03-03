package Model;

import javafx.beans.property.*;

/**
 *
 * @author Clayton Hadaway
 */
public abstract class Part {
    private final IntegerProperty partID;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty stock;
    private final IntegerProperty min;
    private final IntegerProperty max;
    
    public Part() {
        partID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        stock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }
    
    // Class Properties
    public IntegerProperty partIdProperty() {
        return partID;
    }
    
    public StringProperty partNameProperty() {
        return name;
    }
    
    public DoubleProperty priceProperty() {
        return price;
    }
    
    public IntegerProperty stockProperty() {
        return stock;
    }
    
    // Getters
    public int getId() {
        return this.partID.get();
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
    public void setMin(int min) {
        this.min.set(min);
    }
    
    public void setMax(int max) {
        this.max.set(max);
    }
    
    public void setPartId(int partID) {
        this.partID.set(partID);
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
    
    // Check if Part is valid before allowing its creation
    public static String checkValid(String name, double price, int inv, int min, int max, String error) {
        if (name == null) {
            error += "Please enter a part name.";
        } if (inv < 1) {
            error += "Inventory number must be at least 1.";
        } if (price <= 0) {
            error += "Price must be greater than $0.";
        } if (max < min) {
            error += "The min quantity cannot exceed the max.";
        } if (inv < min || inv > max) {
            error += "Inventory number must be in Min/Max range.";
        }
        
        return error;
    }
}
