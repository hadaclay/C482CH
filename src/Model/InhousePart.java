package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;

/**
 *
 * @author Clayton Hadaway
 */
public class InhousePart extends Part {
    private final IntegerProperty machineID;
    
    public InhousePart() {
        super();
        this.machineID = new SimpleIntegerProperty();
    }

    public void setMachineID(int machineId) {
        this.machineID.set(machineId);
    }
    
    public int getMachineID() {
        return this.machineID.get();
    }
 }
