package daisy;

public class DaisyMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Environment e = new Environment(100, 100, 2000, 0.9, 500, 0.12, 20, 0.1);
		Thread t = new Thread(e);
		t.start();
	}

}
