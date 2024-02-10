// The subject class, to be observed by the observer

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

// ABSTRACTION
public abstract class Subject extends JTextField {
    // Add an observer to the list of observers
    abstract void attach(Observer o);
    // Remove an observer from the list of observers
    abstract void detach(Observer o);
    // Update all the observers
    abstract void notifyObs();
    // Return the value stored in the subject class
    abstract int getSubval();
}

// IMPLEMENTATIONS

// Class for storing the decimal value which will be converted
class Decimal extends Subject {
    private int dec;
    private List<Observer> observerList = new ArrayList<Observer>();

    // add observer to list
    @Override
    public void attach(Observer o) {
        observerList.add(o);
    }
    // remove observer from list
    @Override
    public void detach(Observer o) {
        observerList.remove(o);
    }

    // call update method of all observers in list
    @Override
    public void notifyObs() {
        // get text field content only when it is not blank
        if(!this.getText().isBlank())
            dec = Integer.parseInt(this.getText());

        // Update observers
        for(Observer o : observerList) {
            o.updateObs();
        }
    }

    // get decimal value
    @Override
    public int getSubval() {
        return dec;
    }
}