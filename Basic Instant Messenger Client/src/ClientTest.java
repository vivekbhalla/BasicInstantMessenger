import javax.swing.JFrame;

public class ClientTest {
	public static void main(String[] args){
		Client test;
		test =  new Client("127.0.0.1"); // Here the server IP should be used!
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.startRunning();
	}
}
