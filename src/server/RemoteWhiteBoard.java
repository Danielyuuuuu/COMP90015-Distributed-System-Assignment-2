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
import remote.TextMessage;
import remote.Coordinates;

public class RemoteWhiteBoard extends UnicastRemoteObject implements IRemoteWhiteBoard{

	private List<String> clients = Collections.synchronizedList(new ArrayList<String>());
	private String manager = "";
	private Boolean isManagerDisconnected = false;
	private ConcurrentHashMap<Shape, Color> whiteBoardContent = new ConcurrentHashMap<>();
	
	private List<String> clientsWaitList = Collections.synchronizedList(new ArrayList<String>());
	private List<String> clientsDeclinedList = Collections.synchronizedList(new ArrayList<String>());
	
	private ConcurrentHashMap<Coordinates, String> textList = new ConcurrentHashMap<>();
	private Boolean isWhiteBoardStarted = false;
	
	private CopyOnWriteArrayList<TextMessage> textMessages = new CopyOnWriteArrayList<>();
	
	
	protected RemoteWhiteBoard() throws RemoteException {
	
	}
	
	public CopyOnWriteArrayList<TextMessage> getTextMessages() throws RemoteException{
		return this.textMessages;
	}
	
	public void updateTextMessages(TextMessage text) throws RemoteException{
		this.textMessages.add(text);
	}

	
	public Boolean hasManager() throws RemoteException {
		if (clients == null || clients.size() == 0) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean userNameAlreadyExist(String userName) throws RemoteException {
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
		if (!hasManager()) {
			manager = userName;
		}
		clients.add(userName);
	}

	@Override
	public Boolean isManager(String userName) throws RemoteException {
		if (manager.equals(userName)) {
			return true;
		}
		return false;
	}

	@Override
	public String[] getUserList() throws RemoteException {
		return clients.toArray(new String[0]);
	}

	@Override
	public Boolean haveIBeenKicked(String userName) throws RemoteException {
		for (String client: clients) {
			if (client.equals(userName)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Boolean kickUser(String userName) throws RemoteException {
		if (clients.contains(userName)){
			clients.remove(userName);
			return true;
		}
		return false;
	}

	@Override
	public void managerDisconnect() throws RemoteException {
		isManagerDisconnected = true;
		resetWhiteBoard();
		isWhiteBoardStarted = false;
		manager = "";
		clients = Collections.synchronizedList(new ArrayList<String>());
	}

	@Override
	public Boolean isManagerDisconnected() throws RemoteException {
		return isManagerDisconnected;
	}

	@Override
	public void clientDisconnect(String userName) throws RemoteException {
		if (clients.contains(userName)){
			clients.remove(userName);
		}
	}

	@Override
	public void resetWhiteBoard() throws RemoteException {
		whiteBoardContent = new ConcurrentHashMap<Shape, Color>();
		textList = new ConcurrentHashMap<Coordinates, String>();
	}

	@Override
	public void drawWhiteBoard(ConcurrentHashMap<Shape, Color> whiteBoardContent) throws RemoteException {
		this.whiteBoardContent.putAll(whiteBoardContent);
	}
	
	public void drawText(ConcurrentHashMap<Coordinates, String> textList) throws RemoteException {
		this.textList.putAll((ConcurrentHashMap<Coordinates, String>) textList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ConcurrentHashMap<Shape, Color> getWhiteBoardContent() throws RemoteException {
		return whiteBoardContent;
	}
	
	public ConcurrentHashMap<Coordinates, String> getTextList() throws RemoteException {
		return this.textList;
	}

	@Override
	public List<String> getClientsWaitList() throws RemoteException {
		return this.clientsWaitList;
	}

	@Override
	public void addClientToWaitList(String clientName) throws RemoteException {
		this.clientsWaitList.add(clientName);
	}
	

	@Override
	public Boolean isClientInDeclinedList(String clientName) throws RemoteException {
		Iterator<String> it = this.clientsDeclinedList.iterator();
		while(it.hasNext()) {
			String thisClientName = it.next();
			if (thisClientName.equals(clientName)) {
				it.remove();
				return true;
			}
		}
		return false;
	}


	@Override
	public Boolean isClientInWaitList(String clientName) throws RemoteException {
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
		this.clients.add(clientName);
		this.clientsWaitList.remove(clientName);
	}

	@Override
	public void declineClient(String clientName) throws RemoteException {
		this.clientsDeclinedList.add(clientName);
		this.clientsWaitList.remove(clientName);
	}

	@Override
	public Boolean isWhiteBoardStarted() throws RemoteException {
		return this.isWhiteBoardStarted;
	}

	@Override
	public void startWhiteBoard() throws RemoteException {
		this.isWhiteBoardStarted = true;
	}

	@Override
	public void closeWhiteBoard() throws RemoteException {
		this.isWhiteBoardStarted = false;
	}

}
