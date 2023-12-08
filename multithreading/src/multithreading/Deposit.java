package multithreading;

public class Deposit extends Thread {
	
	private Account acc;
	
	public Deposit(Account a) {
		acc = a;
	}
	
	@Override
	public void run() {
		synchronized (acc) {
			int balance = acc.getBalance();
			balance += 1000;
			acc.setBalance(balance);
		}
	}

}
