package client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.IRemoteWhiteBoard;

public class Client {

	private String hostName;
	private int portNumber;
	private String userName;
	private Registry registry = null;
	private IRemoteWhiteBoard remoteWhiteBoard = null;
	
	public Client(String hostName, int portNumber, String userName, Registry registry, IRemoteWhiteBoard remoteWhiteBoard) throws RemoteException {
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.userName = userName;
		this.registry = registry;
		this.remoteWhiteBoard = remoteWhiteBoard;
		
		System.out.println("Client: calling remote methods");
		remoteWhiteBoard.printHello();

	}
	
	public IRemoteWhiteBoard getRMI() {
		return remoteWhiteBoard;
	}
	
	public String getUserName() {
		return userName;
	}
	
//	public Boolean initializeRMIConnection() {
//		//Connect to the rmiregistry that is running on localhost
//		try {
//			registry = LocateRegistry.getRegistry("localhost", portNumber);
//			remoteWhiteBoard = (IRemoteWhiteBoard) registry.lookup("whiteBoard");
//			System.out.println("Client: calling remote methods");
//			remoteWhiteBoard.printHello();
//			return true;
//		} catch (RemoteException | NotBoundException e) {
//			// TODO Auto-generated catch block
////			e.printStackTrace();
//			return false;
//		}
//	}
}
