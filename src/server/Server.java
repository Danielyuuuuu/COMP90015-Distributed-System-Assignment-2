package server;
import java.util.Scanner;

public class Server {
	
	private int portNumber;
	private String hostName;
	
	public Server() {
		Scanner commandLineInput = new Scanner(System.in);
		System.out.println("Enter host name: ");
		hostName = commandLineInput.nextLine().strip();
		System.out.println("Enter port number: ");
		portNumber = commandLineInput.nextInt();
		
		System.out.println("Host name: " + hostName);
		System.out.println("Port number: " + portNumber);
	}
	
	public static void main(String[] args) {
		Server server = new Server();
	}
}
