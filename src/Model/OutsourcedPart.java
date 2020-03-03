package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Clayton Hadaway
 */
public class OutsourcedPart extends Part {
    private final StringProperty companyName;
    
    public OutsourcedPart() {
        super();
        this.companyName = new SimpleStringProperty();
    }
    
    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }
    
    public String getCompanyName() {
        return this.companyName.get();
    }
}
