import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.awt.Insets;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Decompiler extends JFrame {
    private File file;
    private String className;
    private JTextArea tArea;
    
    public Decompiler() {
        setTitle("Decompiled code");
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // JFileChooser fc = new JFileChooser();
        // int res = fc.showOpenDialog(this);

        // if(res == JFileChooser.APPROVE_OPTION) {
        //     file = fc.getSelectedFile();
        // }
        // else if(res == JFileChooser.ERROR_OPTION) {
        //     JOptionPane.showMessageDialog(this, "Error", "Some error occured", JOptionPane.ERROR_MESSAGE);
        // }

        className = JOptionPane.showInputDialog(this, "What is the class name ?");
        System.out.println(className);
        JOptionPane.showMessageDialog(this, "Make sure the class file is in the classpath", "Warning", JOptionPane.WARNING_MESSAGE);

        tArea = new JTextArea();
        tArea.setMargin(new Insets(5, 10, 10, 0));

        JScrollPane scroll = new JScrollPane(tArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scroll);
    }

    public void setCode(String code) {
        tArea.setText(code);
    }

    public String getClassName() {
        return className;
    }

    public static void main(String[] args) {
        Decompiler d = new Decompiler();
        
        String classToLoad = d.getClassName();

        Class theClass = Class.forName(classToLoad);

        String loadedClassName = theClass.getSimpleName();

        int classModifiers = theClass.getModifiers();
        bool isPublic = Modifier.isPublic(classModifiers);
        bool isPrivate = Modifier.isPrivate(classModifiers);
        bool isAbstract = Modifier.isAbstract(classModifiers);
        bool isFinal = Modifier.isFinal(classModifiers);

        bool isInterface = theClass.isInterface();

        Class [] implInterfaces = theClass.getInterfaces();

        Class thisSuper = theClass.getSuperclass();

        Field [] declareFields = theClass.getDeclaredFields();

        Method [] declareMethods = theClass.getDeclaredMethods();

        d.setCode("LOL");
        d.setVisible(true);
    }
}
