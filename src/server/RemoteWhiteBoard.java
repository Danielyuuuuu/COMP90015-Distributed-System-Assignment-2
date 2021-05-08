package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import remote.IRemoteWhiteBoard;

public class RemoteWhiteBoard extends UnicastRemoteObject implements IRemoteWhiteBoard{

	protected RemoteWhiteBoard() throws RemoteException {

	}

	@Override
	public void printHello() throws RemoteException {
		System.out.println("Hello from server");
	}

}
