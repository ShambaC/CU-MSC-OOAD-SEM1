import java.util.ArrayList;
import java.util.List;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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

class Employee {
    protected String name;
    protected String designation;
    protected String dept;
    protected int salary;

    Employee(String name, String designation, String dept, int salary) {
        this.name = name;
        this.designation = designation;
        this.dept = dept;
        this.salary = salary;
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

    @Override
    public String toString() {
        return name;
    }
}

class Worker extends Employee {
    
    Worker(String name, String designation, String dept, int salary) {
		super(name, designation, dept, salary);
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

class Leader extends Employee {
    private List<Employee> subordinates = new ArrayList<Employee>();

    Leader(String name, String designation, String dept, int salary) {
		super(name, designation, dept, salary);
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

public class EmpMgmt extends JFrame {

    DefaultListModel<Employee> model = new DefaultListModel<Employee>();
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

        JPanel mainPanel = new JPanel(gb);

        gbc.weightx = 7;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(6, 0, 0, 0);
        JTabbedPane tabsMenu = new JTabbedPane(JTabbedPane.LEFT);

        JPanel addTab = new JPanel();

        JPanel addLabelGroup = new JPanel(new GridLayout(0, 2, 50, 15));

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

        addEntryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String des = desField.getText();
                String dep = depField.getText();
                int salary = salaryField.getText().isBlank() ? -1 : Integer.parseInt(salaryField.getText());

                if(!name.isBlank() && !des.isBlank() && !dep.isBlank() && salary > 0) {
                    if(leaderRadio.isSelected()) {
                        model.addElement(new Leader(name, des, dep, salary));
                    }
                    else {
                        model.addElement(new Worker(name, des, dep, salary));
                    }

                    nameField.setText("");
                    desField.setText("");
                    depField.setText("");
                    salaryField.setText("");
                    workerRadio.setSelected(true);
                    leaderRadio.setSelected(false);
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel, "There are empty fields !", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

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

        JPanel queriesTab = new JPanel();
        JPanel queriesGroup = new JPanel(new GridLayout(0, 1));

        JLabel addSubLabel = new JLabel("Subordinates to Add: ");
        JList subSelectList = new JList(model);
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

        JPanel detailsTab = new JPanel();
        JPanel detailsGroup = new JPanel(new GridLayout(0, 1));

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

        detailsGroup.add(nameLabel);
        detailsGroup.add(desLabel);
        detailsGroup.add(depLabel);
        detailsGroup.add(salary);
        detailsGroup.add(totalSalary);
        detailsGroup.add(subLabel);
        detailsGroup.add(subList);
        detailsGroup.add(totalWorkers);
        detailsTab.add(detailsGroup);

        tabsMenu.addTab("Add", addTab);
        tabsMenu.addTab("Queries", queriesTab);
        tabsMenu.addTab("Details", detailsTab);
        tabsMenu.setEnabledAt(1, false);
        gb.setConstraints(tabsMenu, gbc);

        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(0, 0, 0, 0);
        JList empList = new JList(model);
        JScrollPane empScroll = new JScrollPane(empList);
        empScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "Employees", TitledBorder.CENTER, TitledBorder.TOP));
        gb.setConstraints(empScroll, gbc);

        queryRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Employee> selectedSubs = new ArrayList<Employee>();
                selectedSubs = subSelectList.getSelectedValuesList();

                for(Employee E: selectedSubs) {
                    selectedEmployee.add(E);
                }

                selectedSubs = subRemoveList.getSelectedValuesList();

                for(Employee E: selectedSubs) {
                    selectedEmployee.remove(E);
                }

                subSelectList.clearSelection();
                subRemoveList.clearSelection();
            }
        });

        empList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedEmployee = (Employee) empList.getSelectedValue();
                if(selectedEmployee.isLeader()) {
                    subRemoveList.setListData(selectedEmployee.getsubs().toArray());
                    tabsMenu.setEnabledAt(1, true);

                    subLabel.setVisible(true);
                    subList.setListData(selectedEmployee.getsubs().toArray());
                    subList.setVisible(true);
                }
                else {
                    if(tabsMenu.getSelectedIndex() == 1) {
                        tabsMenu.setSelectedIndex(0);
                    }
                    tabsMenu.setEnabledAt(1, false);

                    subLabel.setVisible(false);
                    subList.setVisible(false);
                }
                
                nameLabel.setText("Name: " + selectedEmployee.getName());
                desLabel.setText("Designation: " + selectedEmployee.getDes());
                depLabel.setText("Department: " + selectedEmployee.getDep());
                salary.setText("Salary: " + selectedEmployee.getSal());
                totalSalary.setText("Total Salary: " + selectedEmployee.getTotalSalary());
                totalWorkers.setText("Total Workers: " + selectedEmployee.getTotalWorkers());
            }
        });

        mainPanel.add(tabsMenu);
        mainPanel.add(empScroll);

        add(mainPanel);
    }

    public static void main(String[] args) {
        // Adding CEO
        Leader CEO = new Leader("Dell", "CEO", "Company", 20000);

        // Adding project leaders
        Leader DevLead = new Leader("Jack", "Manager", "Dev", 19000);
        Leader SalesLead = new Leader("John", "Manager", "Sales", 15000);

        // Adding project leaders under CEO
        CEO.add(DevLead);
        CEO.add(SalesLead);

        // Adding workers
        Worker devWork0 = new Worker("Pam", "Leaf", "Dev", 12000);
        Leader devJuniorLeader = new Leader("Sip", "JLead", "Dev", 14000);

        DevLead.add(devWork0);
        DevLead.add(devJuniorLeader);

        Worker devWork1 = new Worker("Bam", "Leaf", "Dev", 11000);
        devJuniorLeader.add(devWork1);

        Worker salesWork0 = new Worker("Jon", "Leaf", "Sales", 9000);
        Worker salesWork1 = new Worker("Bon", "Leaf", "Sales", 8000);

        SalesLead.add(salesWork0);
        SalesLead.add(salesWork1);

        // Showing CEO details
        CEO.displayEmpDets();

        // Other stuff
        System.out.println("SalesWorker1 salary: " + salesWork1.getTotalSalary());
        System.out.println("DevLead total salary: " + DevLead.getTotalSalary());
        System.out.println("CEO total salary: " + CEO.getTotalSalary());

        System.out.println("Total workers under CEO: " + CEO.getTotalWorkers());

        // UI
        EmpMgmt E = new EmpMgmt();
        E.setVisible(true);

    }
}
