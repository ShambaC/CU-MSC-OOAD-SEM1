package multithreading;

public class MyThread extends Thread {

	public MyThread(String name) {
		setName(name);
	}
	
	@Override
	public void run() {
		while(true) {
			System.out.println(" From "+getName()+".....");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
