package multithreading;

public class Program1 {

	public static void main(String[] args) {
		while(true) {
			System.out.println(" From Process 1.....");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
