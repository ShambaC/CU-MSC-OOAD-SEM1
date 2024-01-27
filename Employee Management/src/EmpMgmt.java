import java.util.ArrayList;
import java.util.List;


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
}

public class EmpMgmt {

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

    }
}
