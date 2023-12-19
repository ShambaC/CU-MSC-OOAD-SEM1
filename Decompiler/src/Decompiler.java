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
import javax.swing.text.html.HTMLDocument.Iterator;

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

        try {
            Class theClass = Class.forName(classToLoad);

            String loadedClassName = theClass.getSimpleName();

            int classModifiers = theClass.getModifiers();
            boolean isPublic = Modifier.isPublic(classModifiers);
            boolean isPrivate = Modifier.isPrivate(classModifiers);
            boolean isAbstract = Modifier.isAbstract(classModifiers);
            boolean isFinal = Modifier.isFinal(classModifiers);

            boolean isInterface = theClass.isInterface();

            Class [] implInterfaces = theClass.getInterfaces();

            Class thisSuper = theClass.getSuperclass();

            Field [] declareFields = theClass.getDeclaredFields();

            Method [] declareMethods = theClass.getDeclaredMethods();

            // Tests
            System.out.println(loadedClassName);
            System.out.println(isPublic);
            System.out.println(isPrivate);
            System.out.println(isAbstract);
            System.out.println(isFinal);
            System.out.println(isInterface);
            for(Class c : implInterfaces) {
                System.out.println(c.toGenericString());
            }
            System.out.println(thisSuper.toGenericString());
            for(Field f : declareFields) {
                System.out.println(f.toGenericString());
            }
            for(Method m : declareMethods) {
                System.out.println(m.toGenericString());
            }

            // Generate code stub

            String sourceCode = "";
            String dependecies = "";
            String mainContent = "";

            if(isPublic) {
                mainContent += "public ";
            }
            else if(isPrivate) {
                mainContent += "private ";
            }
            else {
                mainContent += "protected";
            }

            if(isAbstract) { mainContent += "abstract "; }
            if(isFinal) { mainContent += "final "; }
            if(isInterface) { mainContent += "interface "; }

            mainContent += "class " + loadedClassName;

            if(thisSuper != null) {
                String [] classDets = thisSuper.toGenericString().split(" ");
                dependecies += "import " + classDets[classDets.length - 1] + ";\n";
                
                String [] superClassNameArr = classDets[classDets.length - 1].split("\\.");
                mainContent += " extends " + superClassNameArr[superClassNameArr.length - 1];
            }

            if(implInterfaces != null) {
                mainContent += " implements ";
                int count = 0;
                for(Class c : implInterfaces) {
                    String [] intTempString = c.toGenericString().split(" ");
                    dependecies += "import " + intTempString[intTempString.length - 1] + ";\n";
                    String [] resInterface = intTempString[intTempString.length - 1].split("\\.");
                    mainContent += resInterface[resInterface.length -1];
                    count++;
                    if(count != implInterfaces.length) {
                        mainContent += ", ";
                    }
                }
            }
            
            dependecies += "\n";
            sourceCode += dependecies + mainContent;

            // Output
            d.setCode(sourceCode);
            d.setVisible(true);

        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
