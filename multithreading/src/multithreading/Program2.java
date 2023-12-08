package multithreading;

public class Program2 {

	public static void main(String[] args) {
		while(true) {
			System.out.println(" From Process 2");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
