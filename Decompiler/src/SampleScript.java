import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SampleScript extends JFrame implements ActionListener {
    private int varA;
    
    public String varB;

    public SampleScript() {
        this.varA = 10;
        this.varB = "Sample String";
    }

    private void methodA() {
        System.out.println("Method A Output");
    }

    public void methodB() {
        System.out.println("Calling method A");
        this.methodA();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    public static void main(String[] args) {
        SampleScript ss = new SampleScript();
        ss.methodB();
    }
}
