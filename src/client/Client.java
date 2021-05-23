/*
 * The program is written by Yifei Yu
 * Student ID: 945753
 */

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
	private Boolean isWhiteBoardOpened = false;
	
	public Client(String hostName, int portNumber, String userName, Registry registry, IRemoteWhiteBoard remoteWhiteBoard) throws RemoteException {
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.userName = userName;
		this.registry = registry;
		this.remoteWhiteBoard = remoteWhiteBoard;
	}
	
	public IRemoteWhiteBoard getRMI() {
		return remoteWhiteBoard;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public Boolean isWhiteBoardOpened() {
		return this.isWhiteBoardOpened;
	}
	
	public void setWhiteBoardOpened() {
		this.isWhiteBoardOpened = true;
	}
	
	public void setWhiteBoardClosed() {
		this.isWhiteBoardOpened = false;
	}
}
