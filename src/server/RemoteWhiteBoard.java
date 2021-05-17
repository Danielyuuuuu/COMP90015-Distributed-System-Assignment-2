package server;

import java.awt.Color;
import java.awt.Shape;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import remote.IRemoteWhiteBoard;
import remote.Coordinates;

public class RemoteWhiteBoard extends UnicastRemoteObject implements IRemoteWhiteBoard{

	private List<String> clients = Collections.synchronizedList(new ArrayList<String>());
	private String manager = "";
	private Boolean isManagerDisconnected = false;
//	private ArrayList<Shape> whiteBoardContent = new ArrayList<>();
//	private List<Shape> whiteBoardContent = Collections.synchronizedList(new ArrayList<Shape>());
	private ConcurrentHashMap<Shape, Color> whiteBoardContent = new ConcurrentHashMap<>();
	
//	private Boolean isUpdatingWhiteBoardContent = false;
	
	private List<String> clientsWaitList = Collections.synchronizedList(new ArrayList<String>());
	private List<String> clientsDeclinedList = Collections.synchronizedList(new ArrayList<String>());
	
	private ConcurrentHashMap<Coordinates, String> textList = new ConcurrentHashMap<>();
	private Boolean isWhiteBoardStarted = false;
	
	
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
		resetWhiteBoard();
		isWhiteBoardStarted = false;
		manager = "";
		clients = Collections.synchronizedList(new ArrayList<String>());
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
	public void resetWhiteBoard() throws RemoteException {
		// TODO Auto-generated method stub
//		if (!this.isUpdatingWhiteBoardContent) {
//			this.isUpdatingWhiteBoardContent = true;
		whiteBoardContent = new ConcurrentHashMap<Shape, Color>();
//			this.isUpdatingWhiteBoardContent = false;
			
		textList = new ConcurrentHashMap<Coordinates, String>();
			
//		return true;
//		}
//		return false;
		
		
	}

	@Override
	public void drawWhiteBoard(ConcurrentHashMap<Shape, Color> whiteBoardContent) throws RemoteException {
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

//		if (!this.isUpdatingWhiteBoardContent) {
////			synchronized (this.whiteBoardContent) {
//			this.isUpdatingWhiteBoardContent = true;
		this.whiteBoardContent.putAll(whiteBoardContent);
//			this.isUpdatingWhiteBoardContent = false;
//			}

//		}
//		return false;
		
	}
	
	public void drawText(ConcurrentHashMap<Coordinates, String> textList) throws RemoteException {
		this.textList.putAll((ConcurrentHashMap<Coordinates, String>) textList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ConcurrentHashMap<Shape, Color> getWhiteBoardContent() throws RemoteException {
		// TODO Auto-generated method stub
		return whiteBoardContent;
	}
	
	public ConcurrentHashMap<Coordinates, String> getTextList() throws RemoteException {
		return this.textList;
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

	@Override
	public Boolean isWhiteBoardStarted() throws RemoteException {
		return this.isWhiteBoardStarted;
	}

	@Override
	public void startWhiteBoard() throws RemoteException {
		// TODO Auto-generated method stub
		this.isWhiteBoardStarted = true;
	}

	@Override
	public void closeWhiteBoard() throws RemoteException {
		// TODO Auto-generated method stub
		this.isWhiteBoardStarted = false;
	}



}
