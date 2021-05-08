package client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.IRemoteWhiteBoard;

public class Client {

	private String userName;
	private Registry registry = null;
	private IRemoteWhiteBoard remoteWhiteBoard = null;
	
	public Client() {}
	
	public Boolean initializeRMIConnection(String hostName, int portNumber, String userName) {
		//Connect to the rmiregistry that is running on localhost
		try {
			registry = LocateRegistry.getRegistry("localhost", portNumber);
			remoteWhiteBoard = (IRemoteWhiteBoard) registry.lookup("whiteBoard");
			System.out.println("Client: calling remote methods");
			remoteWhiteBoard.printHello();
			this.userName = userName;
			return true;
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return false;
		}
		


	}
}
