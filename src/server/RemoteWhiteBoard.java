package server;

import java.awt.Shape;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import remote.IRemoteWhiteBoard;

public class RemoteWhiteBoard extends UnicastRemoteObject implements IRemoteWhiteBoard{

	private List<String> clients = Collections.synchronizedList(new ArrayList<String>());
	private String manager = "";
	private Boolean isManagerDisconnected = false;
//	private ArrayList<Shape> whiteBoardContent = new ArrayList<>();
	private List<Shape> whiteBoardContent = Collections.synchronizedList(new ArrayList<Shape>());
	private Boolean isUpdatingWhiteBoardContent = false;
	
	private List<String> clientsWaitList = Collections.synchronizedList(new ArrayList<String>());
	private List<String> clientsDeclinedList = Collections.synchronizedList(new ArrayList<String>());
	
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

	@Override
	public void clientDisconnect(String userName) throws RemoteException {
		// TODO Auto-generated method stub
		if (clients.contains(userName)){
			clients.remove(userName);
		}
	}

	@Override
	public Boolean resetWhiteBoard() throws RemoteException {
		// TODO Auto-generated method stub
		if (!this.isUpdatingWhiteBoardContent) {
			this.isUpdatingWhiteBoardContent = true;
			whiteBoardContent = new ArrayList<Shape>();
			this.isUpdatingWhiteBoardContent = false;
			return true;
		}
		return false;
		
		
	}

	@Override
	public Boolean drawWhiteBoard(List<Shape> whiteBoardContent) throws RemoteException {
		// TODO Auto-generated method stub
//		whiteBoardContent.add(line);
//		for (Shape content: whiteBoardContent) {
//			if (!this.whiteBoardContent.contains(content)) {
//				this.whiteBoardContent.add(content);
//			}
//		}
//		while (this.isUpdatingWhiteBoardContent) {
//			try {
//				TimeUnit.SECONDS.sleep(1);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}

		if (!this.isUpdatingWhiteBoardContent) {
			synchronized (this.whiteBoardContent) {
				this.isUpdatingWhiteBoardContent = true;
				this.whiteBoardContent.addAll(whiteBoardContent);
				this.isUpdatingWhiteBoardContent = false;
				return true;
			}

		}
		return false;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shape> getWhiteBoardContent() throws RemoteException {
		// TODO Auto-generated method stub
		return whiteBoardContent;
	}

	@Override
	public List<String> getClientsWaitList() throws RemoteException {
		// TODO Auto-generated method stub
		return this.clientsWaitList;
	}

	@Override
	public void addClientToWaitList(String clientName) throws RemoteException {
		// TODO Auto-generated method stub
		this.clientsWaitList.add(clientName);
	}
	

	@Override
	public Boolean isClientInDeclinedList(String clientName) throws RemoteException {
		// TODO Auto-generated method stub
		Iterator<String> it = this.clientsDeclinedList.iterator();
		while(it.hasNext()) {
			String thisClientName = it.next();
			System.out.println("isClientInDeclinedList: " + thisClientName + "  " + clientName);
			if (thisClientName.equals(clientName)) {
				it.remove();
				System.out.println("isClientInDeclinedList: returned true");
				return true;
			}
		}
		System.out.println("isClientInDeclinedList: returned false");
		return false;
	}

//	@Override
//	public void addClientToDeclinedList(String clientName) throws RemoteException {
//		// TODO Auto-generated method stub
//		this.clientsDeclinedList.add(clientName);
//	}

	@Override
	public Boolean isClientInWaitList(String clientName) throws RemoteException {
		// TODO Auto-generated method stub
		Iterator<String> it = this.clientsWaitList.iterator();
		while(it.hasNext()) {
			String thisClientName = it.next();
			if (thisClientName.equals(clientName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void acceptClient(String clientName) throws RemoteException {
		// TODO Auto-generated method stub
		this.clients.add(clientName);
		this.clientsWaitList.remove(clientName);
	}

	@Override
	public void declineClient(String clientName) throws RemoteException {
		// TODO Auto-generated method stub
		this.clientsDeclinedList.add(clientName);
		this.clientsWaitList.remove(clientName);
	}

}
