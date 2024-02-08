import javax.swing.JTextField;

public abstract class Observer extends JTextField {
    abstract void updateObs();
}

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
        String res = Integer.toBinaryString(sub.getSubval());
        this.setText(res);
    }

}

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
        String res = Integer.toHexString(sub.getSubval());
        this.setText(res);
    }
    
}

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
        String res = Integer.toOctalString(sub.getSubval());
        this.setText(res);
    }
}