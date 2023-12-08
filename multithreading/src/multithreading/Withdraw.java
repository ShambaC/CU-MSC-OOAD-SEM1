package multithreading;

public class Withdraw extends Thread {
	
	private Account acc;
	
	public Withdraw(Account a) {
		acc = a;
	}
	
	@Override
	public void run() {
		synchronized (this) {
			int balance = acc.getBalance();
			balance -= 1000;
			acc.setBalance(balance);
		}
	}

}
