package multithreading;

public class MainProgram {

	public static void main(String[] args) throws Exception {
		Account acc = new Account();
		
		Deposit d = new Deposit(acc);
		Withdraw w = new Withdraw(acc);
		d.start();
		w.start();
		d.join();
		w.join();
		System.out.println("Balance = "+acc.getBalance());
		
				

	}

}
