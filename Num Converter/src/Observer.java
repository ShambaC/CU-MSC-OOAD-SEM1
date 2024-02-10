// The observer class to update information based on the subject observed

import javax.swing.JTextField;

public abstract class Observer extends JTextField {
    // Update method to update observer information
    abstract void updateObs();
}

// Binary converter observer
class BinField extends Observer {
    String binary;

    Subject sub;

    public BinField(Subject sub) {
        super();
        this.sub = sub;
        this.setEditable(false);
    }

    @Override
    void updateObs() {
        // convert decimal to binary
        String res = Integer.toBinaryString(sub.getSubval());
        this.setText(res);
    }

}

// Hexadecimal converter observer
class HexField extends Observer {
    String hex;

    Subject sub;

    public HexField(Subject sub) {
        super();
        this.sub = sub;
        this.setEditable(false);
    }

    @Override
    void updateObs() {
        // convert decimal to hex
        String res = Integer.toHexString(sub.getSubval());
        this.setText(res);
    }
    
}

// Octal converter observer
class OctField extends Observer {
    String octal;

    Subject sub;

    public OctField(Subject sub) {
        super();
        this.sub = sub;
        this.setEditable(false);
    }
    
    @Override
    void updateObs() {
        // convert decimal to octal
        String res = Integer.toOctalString(sub.getSubval());
        this.setText(res);
    }
}