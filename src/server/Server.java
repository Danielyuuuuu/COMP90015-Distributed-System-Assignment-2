package server;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import remote.IRemoteWhiteBoard;

public class Server {
	
	private int portNumber;
	private String hostName;
	
	public Server() {
		try{
			// Get input from the command line
			Scanner commandLineInput = new Scanner(System.in);
			System.out.println("Enter host name: ");
			hostName = commandLineInput.nextLine().strip();
			System.out.println("Enter port number: ");
			portNumber = Integer.parseInt(commandLineInput.nextLine().strip());
			
			System.out.println("Host name: " + hostName);
			System.out.println("Port number: " + portNumber);
			
//			while(portNumber > 65535 || portNumber <= 0) {
//				System.out.println("Please enter a correct port number: ");
//				portNumber = Integer.parseInt(commandLineInput.nextLine().strip());
//			}
			
			commandLineInput.close();
			
			// Setup the RMI
			System.setProperty("java.rmi.server.hostname", hostName);	
			IRemoteWhiteBoard remoteWhiteBoard = new RemoteWhiteBoard();
			System.out.println("after creating white board");
			Registry registry = LocateRegistry.getRegistry(portNumber);
			System.out.println("after registry");
            registry.bind("whiteBoard", remoteWhiteBoard);
            
            System.out.println("RMI set");
            
			
		}
		catch(NumberFormatException e) {
			System.out.println("You did not enter a valid port number, server terminated!");
			System.exit(1);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		Server server = new Server();
	}
}
