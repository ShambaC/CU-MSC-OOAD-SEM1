import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

public abstract class Subject extends JTextField {
    abstract void attach(Observer o);
    abstract void detach(Observer o);
    abstract void notifyObs();
    abstract int getSubval();
}

class Decimal extends Subject {
    private int dec;
    private List<Observer> observerList = new ArrayList<Observer>();

    @Override
    public void attach(Observer o) {
        observerList.add(o);
    }
    @Override
    public void detach(Observer o) {
        observerList.remove(o);
    }

    @Override
    public void notifyObs() {
        if(!this.getText().isBlank())
            dec = Integer.parseInt(this.getText());

        for(Observer o : observerList) {
            o.updateObs();
        }
    }

    @Override
    public int getSubval() {
        return dec;
    }
}