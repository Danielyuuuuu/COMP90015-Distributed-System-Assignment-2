package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.IRemoteWhiteBoard;

public class Client {
	public static void main(String[] args) {
		try {
			//Connect to the rmiregistry that is running on localhost
			Registry registry = LocateRegistry.getRegistry("localhost", 3333);
           
			//Retrieve the stub/proxy for the remote math object from the registry
			IRemoteWhiteBoard remoteWhiteBoard = (IRemoteWhiteBoard) registry.lookup("whiteBoard");
			
			System.out.println("Client: calling remote methods");
			remoteWhiteBoard.printHello();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
