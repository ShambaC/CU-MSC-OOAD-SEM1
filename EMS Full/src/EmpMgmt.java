import java.util.ArrayList;
import java.util.List;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

// Base Employee class
class Employee implements Serializable {
    protected int uid;
    protected int supId;
    protected String name;
    protected String designation;
    protected String dept;
    protected int salary;

    Employee(int uid, String name, String designation, String dept, int salary) {
        this.uid = uid;
        this.name = name;
        this.designation = designation;
        this.dept = dept;
        this.salary = salary;
        supId = -1;
    }

    public String getName() {
        return name;
    }
    public String getDes() {
        return designation;
    }
    public String getDep() {
        return dept;
    }
    public int getSal() {
        return salary;
    }

    public int getTotalSalary() {
        return salary;
    }

    public int getTotalWorkers() {
        return 1;
    }

    public void displayEmpDets() {
        System.out.println("Employee details-");
        System.out.println("Name: " + name);
        System.out.println("Designation: " + designation);
        System.out.println("Department: " + dept);
        System.out.println("Salary: " + salary);
    }

    public void add(Employee e) {    }

    public void remove(Employee e) {    }

    public List<Employee> getsubs() {
        List<Employee> tempList = new ArrayList<Employee>();
        return tempList;
    }

    public boolean isLeader() {
        return false;
    }

    public boolean Equals(Employee E) {
        return (E.getName().equalsIgnoreCase(name) && E.getDep().equalsIgnoreCase(dept) && E.getDes().equalsIgnoreCase(designation));
    }

    public void setSuperior(int id) {
        this.supId = id;
    }

    public int getSuperior() {
        return this.supId;
    }

    public int getId() {
        return this.uid;
    }

    @Override
    public String toString() {
        return name;
    }
}

// Worker class
class Worker extends Employee {
    
    Worker(int uid, String name, String designation, String dept, int salary) {
		super(uid, name, designation, dept, salary);
	}

	public int getTotalSalary() {
        return salary;
    }

    public int getTotalWorkers() {
        return 1;
    }

    public boolean isLeader() {
        return false;
    }
}

// Leader class, has subordinates
class Leader extends Employee {
    private List<Employee> subordinates = new ArrayList<Employee>();

    Leader(int uid, String name, String designation, String dept, int salary) {
		super(uid, name, designation, dept, salary);
	}

    public void add(Employee e) {
        subordinates.add(e);
    }

    public void remove(Employee e) {
        subordinates.remove(e);
    }

    public List<Employee> getsubs() {
        return subordinates;
    }

    public int getTotalSalary() {
        int totalSalary = salary;

        for(Employee e: subordinates) {
            totalSalary += e.getTotalSalary();
        }

        return totalSalary;
    }

    public int getTotalWorkers() {
        int totalWorkers = 1;

        for(Employee e: subordinates) {
            totalWorkers += e.getTotalWorkers();
        }

        return totalWorkers;
    }

    public boolean isLeader() {
        return true;
    }
}

// Data Access Object Pattern
class DataAccessObject extends JFrame {

    /**
     * Takes a Transfer Object and stores the datato the database
     * @param TO        The transfer object containing data
     * @param isNew     Whether a new file is being created or old one is being replaced
     */
    public void Store(TransferObject TO, boolean isNew) {
        // Open the file to write
        File f = new File(config.empDBlocation);
        // CSV file header row
        String headingRow = "UID, SUPID, NAME, DES, DEP, SAL\n";

        try {
            // If a new file is being created, then write the headers as the first row
            if(isNew) {
                Files.write(f.toPath(), headingRow.getBytes());
            }
            // Write data row
            String row = TO.uid + "," + TO.supId + "," + TO.empName + "," + TO.empDes + "," + TO.empDep + "," + TO.empSal + "\n";
            Files.write(f.toPath(), row.getBytes(), StandardOpenOption.APPEND);
        }
        catch(IOException err) {
            err.printStackTrace();
        }
    }

    /**
     * Method to retrieve the whole list of employees from the database
     * @return  Returns a list of Transfer Objects containing the desired data
     */
    public List<TransferObject> Load() {
        List<TransferObject> TOList = new ArrayList<TransferObject>();

        JFileChooser fc = new JFileChooser();
        int res = fc.showOpenDialog(fc);

        File f = null;
        if(res == JFileChooser.APPROVE_OPTION) {
            f = fc.getSelectedFile();
        }

        try {
            // Get the string content of the CSV file
            String content = new String(Files.readAllBytes(f.toPath()));
            // Split the CSV file by lines. \r is for UNIX line break
            String contentLines[] = content.split("\\r?\\n");

            // Store the lines into an ArrayList for easier operations
            List<String> linesCSV = new ArrayList<String>();
            for(int i = 0; i < contentLines.length; i++) {
                linesCSV.add(contentLines[i]);
            }

            // Remove first line as it is the header row
            linesCSV.remove(0);

            // Iterate through each of the remaining lines
            for(String S : linesCSV) {
                // Split the lines by comma to get each cell value
                String EData[] = S.split(",");
                // Create a new Transfer object with the data and add it to the list
                TOList.add(new TransferObject(Integer.parseInt(EData[0]), Integer.parseInt(EData[1]), EData[2], EData[3], EData[4], Integer.parseInt(EData[5])));
            }

            return TOList;
        }
        catch(IOException err) {
            err.printStackTrace();
        }

        return TOList;
    }
}

// Class that acts as a bridge between the client program and the Data Access Object
class TransferObject {
    public int uid;
    public int supId;
    public String empName;
    public String empDes;
    public String empDep;
    public int empSal;

    public TransferObject(int uid, int supId, String empName, String empDes, String empDep, int empSal) {
        this.uid = uid;
        this.supId = supId;
        this.empName = empName;
        this.empDes = empDes;
        this.empDep = empDep;
        this.empSal = empSal;
    }
}

// BRIDGE DESIGN PATTERN
class StorageRepository{

    /**
     * Method to store the employee list to a particular file type
     * @param model The list model containing the employee list
     * @param type  The type of storage to use
     */
    public void Store(DefaultListModel<Employee> model, String type) {
        if(type.equalsIgnoreCase("File")) {
            SaveFile(model);
        }
        else if(type.equalsIgnoreCase("DB")) {
            SaveDB(model);
        }
    }

    /**
     * Method to retrieve a list of employees from a particular type of file
     * @param type  The type of file to retrieve data from
     * @return  The list of employees
     */
    public DefaultListModel<Employee> Read(String type) {
        DefaultListModel<Employee> model = null;

        if(type.equalsIgnoreCase("File")) {
            model = LoadFile();
            return model;
        }
        else if(type.equalsIgnoreCase("DB")) {
            return LoadDB();
        }

        return model;
    }

    /**
     * The method to store data to binary file
     * @param model The list of employees to store
     */
    private void SaveFile(DefaultListModel<Employee> model) {

        try {
            // Create a stream to the file to write data
            FileOutputStream fout = new FileOutputStream(config.empFileLocation);
            ObjectOutputStream out = new ObjectOutputStream(fout);

            // Store the model to the file
            out.writeObject(model);
            out.flush();
            out.close();
        }
        catch (IOException err) {
            err.printStackTrace();
        }
    }

    /**
     * Method to Load a list of employees from a binary file
     * @return  List of employees
     */
    private DefaultListModel<Employee> LoadFile() {
        DefaultListModel<Employee> model = new DefaultListModel<Employee>();
        JFileChooser fc = new JFileChooser();

        int res = fc.showOpenDialog(fc);
        File f = null;

        if(res == JFileChooser.APPROVE_OPTION) {
            f = fc.getSelectedFile();
        }

        try {
            // Read data from file and return it
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
            model = (DefaultListModel<Employee>) in.readObject();
            in.close();

            return model;
        }
        catch(IOException | ClassNotFoundException err) {
            err.printStackTrace();
        }

        return model;
    }

    /**
     * Method to store employee data into a DB, here it is a CSV file
     * @param model List of employees to save
     */
    private void SaveDB(DefaultListModel<Employee> model) {
        DataAccessObject DAO = new DataAccessObject();
        // Iterate through the Employees to create a Transfer Object for them individually
        for(int i = 0; i < model.getSize(); i++) {
            Employee E = model.getElementAt(i);

            TransferObject TO = new TransferObject(E.getId(), E.getSuperior(), E.getName(), E.getDes(), E.getDep(), E.getSal());
            // Save the objects
            DAO.Store(TO, i==0);
        }
    }

    /**
     * Method to retrieve employee data from the DB
     * @return  The list of retrieved employees
     */
    private DefaultListModel<Employee> LoadDB() {
        DefaultListModel<Employee> model = new DefaultListModel<Employee>();
        DataAccessObject DAO = new DataAccessObject();
        // Create a list of Transfer Objects to hold data
        List<TransferObject> TOList = new ArrayList<TransferObject>();
        TOList = DAO.Load();

        // Convert the transfer objects to Employee object
        for(TransferObject T : TOList) {
            Employee E = new Worker(T.uid, T.empName, T.empDes, T.empDep, T.empSal);
            // Check if the employee has a superior
            if(T.supId != -1) {
                E.setSuperior(T.supId);
            }

            // Add employee to the list
            model.addElement(E);
        }

        // Iterate through the employees to look for Workers
        for(int i = 0; i < model.getSize(); i++) {
            Employee E = model.getElementAt(i);
            if(E.getSuperior() != -1) {
                // Get the superior
                Employee E2 = model.getElementAt(E.getSuperior() - 1);
                Employee L = new Leader(E2.getId(), E2.getName(), E2.getDes(), E2.getDep(), E2.getSal());
                // Add the worker as a subordinate of the superior
                L.add(E);
                model.set(E.getSuperior() - 1, L);
            }
        }

        return model;
    }
}

// Abstract class storing methods to be implemented for according  type of Storage institute
abstract class baseRepository {
    protected StorageRepository sr;

    public baseRepository(StorageRepository sr) {
        this.sr = sr;
    }

    abstract public void Save(DefaultListModel<Employee> model, String type);
    abstract public DefaultListModel<Employee> Load(String type);
}

// This class calls already implemented load save methods
class EmployeeRepository extends baseRepository {
    public EmployeeRepository(StorageRepository sr) {
        super(sr);
    }

    @Override
    public void Save(DefaultListModel<Employee> model, String type) {
        sr.Store(model, type);
    }
    @Override
    public DefaultListModel<Employee> Load(String type) {
        return sr.Read(type);
    }
}

// Main employee management class
public class EmpMgmt extends JFrame {

    // A list model to store all the employees
    DefaultListModel<Employee> model = new DefaultListModel<Employee>();
    // Stores the currently selected employee from the list
    Employee selectedEmployee = null;

    EmpMgmt() {
        setTitle("Employee Manager");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        init();
    }

    private void init() {

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        // Main panel to hold everything
        JPanel mainPanel = new JPanel(gb);

        gbc.weightx = 7;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(6, 0, 0, 0);
        // Tabs for operations
        JTabbedPane tabsMenu = new JTabbedPane(JTabbedPane.LEFT);

        // The add tab
        JPanel addTab = new JPanel();

        // Group the components in a single panel for better layout
        JPanel addLabelGroup = new JPanel(new GridLayout(0, 2, 50, 15));

        // The form to add a new employee
        JLabel empType = new JLabel("Employee category: ");
        JRadioButton leaderRadio = new JRadioButton("Leader");
        JRadioButton workerRadio = new JRadioButton("Worker", true);
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(leaderRadio);
        radioGroup.add(workerRadio);
        JPanel radioPanel = new JPanel();
        radioPanel.add(leaderRadio);
        radioPanel.add(workerRadio);
        JLabel addNameLabel = new JLabel("Name: ");
        JTextField nameField = new JTextField();
        JLabel addDesLabel = new JLabel("Designation: ");
        JTextField desField = new JTextField();
        JLabel addDepLabel = new JLabel("Departmrnt: ");
        JTextField depField = new JTextField();
        JLabel addSalaryLabel = new JLabel("Salary: ");
        JTextField salaryField = new JTextField();
        JButton addEntryButton = new JButton("Add");
        JButton clearEntriesButton = new JButton("Reset");

        // Add button event listener
        addEntryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String des = desField.getText();
                String dep = depField.getText();
                int salary = salaryField.getText().isBlank() ? -1 : Integer.parseInt(salaryField.getText());

                // Add employee only when all the fields were properly filled
                if(!name.isBlank() && !des.isBlank() && !dep.isBlank() && salary > 0) {
                    if(leaderRadio.isSelected()) {
                        model.addElement(new Leader(model.getSize() + 1, name, des, dep, salary));
                    }
                    else {
                        model.addElement(new Worker(model.getSize() + 1, name, des, dep, salary));
                    }

                    nameField.setText("");
                    desField.setText("");
                    depField.setText("");
                    salaryField.setText("");
                    workerRadio.setSelected(true);
                    leaderRadio.setSelected(false);
                }
                // Else show warning
                else {
                    JOptionPane.showMessageDialog(mainPanel, "There are empty fields !", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // A reset button to clear the form
        clearEntriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameField.setText("");
                desField.setText("");
                depField.setText("");
                salaryField.setText("");
                workerRadio.setSelected(true);
                leaderRadio.setSelected(false);
            }
        });

        addLabelGroup.add(empType);
        addLabelGroup.add(radioPanel);
        addLabelGroup.add(addNameLabel);
        addLabelGroup.add(nameField);
        addLabelGroup.add(addDesLabel);
        addLabelGroup.add(desField);
        addLabelGroup.add(addDepLabel);
        addLabelGroup.add(depField);
        addLabelGroup.add(addSalaryLabel);
        addLabelGroup.add(salaryField);
        addLabelGroup.add(addEntryButton);
        addLabelGroup.add(clearEntriesButton);
        addTab.add(addLabelGroup);

        // Queries tab to add and remove employees under a leader
        JPanel queriesTab = new JPanel();
        JPanel queriesGroup = new JPanel(new GridLayout(0, 1));

        // The lists
        JLabel addSubLabel = new JLabel("Subordinates to Add: ");
        JList subSelectList = new JList();
        subSelectList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane subSelectScroll = new JScrollPane(subSelectList);
        JLabel removeSubLabel = new JLabel("Subordinates to remove: ");
        JList subRemoveList = new JList();
        subRemoveList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane subRemoveScroll = new JScrollPane(subRemoveList);
        JButton queryRun = new JButton("Run Queries");

        queriesGroup.add(addSubLabel);
        queriesGroup.add(subSelectScroll);
        queriesGroup.add(removeSubLabel);
        queriesGroup.add(subRemoveScroll);
        queriesGroup.add(queryRun);
        queriesTab.add(queriesGroup);

        // The details tab to show information for the selected employee
        JPanel detailsTab = new JPanel();
        JPanel detailsGroup = new JPanel(new GridLayout(0, 1));

        JLabel uidLabel = new JLabel("UID: ");
        JLabel supLabel = new JLabel("Superior ID: ");
        supLabel.setVisible(false);
        JLabel nameLabel = new JLabel("Name: ");
        JLabel desLabel = new JLabel("Designation: ");
        JLabel depLabel = new JLabel("Department: ");
        JLabel salary = new JLabel("Salary: ");
        JLabel subLabel = new JLabel("Subordinates: ");
        subLabel.setVisible(false);
        JList subList = new JList();
        subList.setVisible(false);
        JLabel totalSalary = new JLabel("Total Salary: ");
        JLabel totalWorkers = new JLabel("Total Workers: ");

        detailsGroup.add(uidLabel);
        detailsGroup.add(supLabel);
        detailsGroup.add(nameLabel);
        detailsGroup.add(desLabel);
        detailsGroup.add(depLabel);
        detailsGroup.add(salary);
        detailsGroup.add(totalSalary);
        detailsGroup.add(subLabel);
        detailsGroup.add(subList);
        detailsGroup.add(totalWorkers);
        detailsTab.add(detailsGroup);

        // The data tab to store and load data from files
        JPanel dataTab = new JPanel();
        JPanel dataGorup = new JPanel(new GridLayout(0, 2, 50, 15));
        JLabel connTypeLabel = new JLabel("Storage Type: ");
        JPanel connRadioContainer = new JPanel();
        ButtonGroup connRadioGroup = new ButtonGroup();
        JRadioButton fileRadio = new JRadioButton("File", true);
        JRadioButton DBRadio = new JRadioButton("DataBase");
        connRadioGroup.add(fileRadio);
        connRadioGroup.add(DBRadio);
        connRadioContainer.add(fileRadio);
        connRadioContainer.add(DBRadio);
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");

        dataGorup.add(connTypeLabel);
        dataGorup.add(connRadioContainer);
        dataGorup.add(saveButton);
        dataGorup.add(loadButton);
        dataTab.add(dataGorup);

        tabsMenu.addTab("Add", addTab);
        tabsMenu.addTab("Queries", queriesTab);
        tabsMenu.addTab("Details", detailsTab);
        tabsMenu.addTab("Data", dataTab);
        tabsMenu.setEnabledAt(1, false);
        gb.setConstraints(tabsMenu, gbc);

        // The list of the employees
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(0, 0, 0, 0);
        JList empList = new JList(model);
        JScrollPane empScroll = new JScrollPane(empList);
        empScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "Employees", TitledBorder.CENTER, TitledBorder.TOP));
        gb.setConstraints(empScroll, gbc);

        // The button that runs the query to add or remove employees under a leader
        queryRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Employee> selectedSubs = new ArrayList<Employee>();
                selectedSubs = subSelectList.getSelectedValuesList();

                // Add selected employees
                for(Employee E: selectedSubs) {
                    selectedEmployee.add(E);
                    E.setSuperior(selectedEmployee.getId());
                }

                selectedSubs = subRemoveList.getSelectedValuesList();

                // Remove the selected employees
                for(Employee E: selectedSubs) {
                    selectedEmployee.remove(E);
                }

                // Clear the list selections after queries are done executing
                subSelectList.clearSelection();
                subRemoveList.clearSelection();
            }
        });

        // Listener for when an employee is selected from the global list
        empList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedEmployee = (Employee) empList.getSelectedValue();
                // If the selected employee is of type leader
                if(selectedEmployee.isLeader()) {
                    DefaultListModel<Employee> selectionModel = new DefaultListModel<Employee>();
                    // Add possible subordinates to the selection list
                    for(int i = 0; i < model.getSize(); i++) {
                        Employee tempEmp = model.getElementAt(i);
                        if(tempEmp.Equals(selectedEmployee) || tempEmp.getSuperior() != -1) {
                            continue;
                        }

                        selectionModel.addElement(model.getElementAt(i));
                    }
                    subSelectList.setModel(selectionModel);
                    // Add subordinates to the removal list
                    subRemoveList.setListData(selectedEmployee.getsubs().toArray());
                    // Show the query tab
                    tabsMenu.setEnabledAt(1, true);

                    // Show subordinates in the details tab
                    subLabel.setVisible(true);
                    subList.setListData(selectedEmployee.getsubs().toArray());
                    subList.setVisible(true);
                }
                // Else hide the leader specific details
                else {
                    if(tabsMenu.getSelectedIndex() == 1) {
                        tabsMenu.setSelectedIndex(0);
                    }
                    tabsMenu.setEnabledAt(1, false);

                    subLabel.setVisible(false);
                    subList.setVisible(false);
                }
                
                // Show all the details
                uidLabel.setText("UID: " + selectedEmployee.getId());
                if(selectedEmployee.getSuperior() != -1) {
                    supLabel.setVisible(true);
                    supLabel.setText("Superior ID: " + selectedEmployee.getSuperior());
                }
                else {
                    supLabel.setVisible(false);
                }
                nameLabel.setText("Name: " + selectedEmployee.getName());
                desLabel.setText("Designation: " + selectedEmployee.getDes());
                depLabel.setText("Department: " + selectedEmployee.getDep());
                salary.setText("Salary: " + selectedEmployee.getSal());
                totalSalary.setText("Total Salary: " + selectedEmployee.getTotalSalary());
                totalWorkers.setText("Total Workers: " + selectedEmployee.getTotalWorkers());
            }
        });

        // Save load button tab
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmployeeRepository er = new EmployeeRepository(new StorageRepository());
                String type = fileRadio.isSelected() ? "file" : "DB";
                er.Save(model, type);
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmployeeRepository er = new EmployeeRepository(new StorageRepository());
                String type = fileRadio.isSelected() ? "file" : "DB";
                model = er.Load(type);
                empList.setModel(model);
                empList.repaint();
            }
        });

        mainPanel.add(tabsMenu);
        mainPanel.add(empScroll);

        add(mainPanel);
    }

    public static void main(String[] args) {
        // UI
        EmpMgmt E = new EmpMgmt();
        E.setVisible(true);

    }
}