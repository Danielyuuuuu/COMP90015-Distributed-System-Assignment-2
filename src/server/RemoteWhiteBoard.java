package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import remote.IRemoteWhiteBoard;

public class RemoteWhiteBoard extends UnicastRemoteObject implements IRemoteWhiteBoard{

	private ArrayList<String> clients = new ArrayList<>();
	private String manager = "";
	private Boolean isManagerDisconnected = false;
	
	protected RemoteWhiteBoard() throws RemoteException {

	}

	@Override
	public void printHello() throws RemoteException {
		System.out.println("Hello from server");
	}
	
	public Boolean hasManager() throws RemoteException {
		if (clients == null || clients.size() == 0) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean userNameAlreadyExist(String userName) throws RemoteException {
		// TODO Auto-generated method stub
		if (clients == null || clients.size() == 0) {
			return false;
		}
		else {
			for (String existedUserName: clients) {
				if (existedUserName.equals(userName)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void addNewUser(String userName) throws RemoteException {
		// TODO Auto-generated method stub
		if (!hasManager()) {
			manager = userName;
		}
		clients.add(userName);
	}

	@Override
	public Boolean isManager(String userName) throws RemoteException {
		// TODO Auto-generated method stub
		if (manager.equals(userName)) {
			return true;
		}
		return false;
	}

	@Override
	public String[] getUserList() throws RemoteException {
		// TODO Auto-generated method stub
		return clients.toArray(new String[0]);
	}

	@Override
	public Boolean haveIBeenKicked(String userName) throws RemoteException {
		// TODO Auto-generated method stub
		for (String client: clients) {
			if (client.equals(userName)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Boolean kickUser(String userName) throws RemoteException {
		// TODO Auto-generated method stub
		if (clients.contains(userName)){
			clients.remove(userName);
			return true;
		}
		return false;
	}

	@Override
	public void managerDisconnect() throws RemoteException {
		// TODO Auto-generated method stub
		isManagerDisconnected = true;
	}

	@Override
	public Boolean isManagerDisconnected() throws RemoteException {
		// TODO Auto-generated method stub
		return isManagerDisconnected;
	}

}
