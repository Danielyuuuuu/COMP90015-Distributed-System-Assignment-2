package remote;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface IRemoteWhiteBoard extends Remote{
	public void printHello() throws RemoteException;
	public Boolean hasManager() throws RemoteException;
	public Boolean userNameAlreadyExist(String userName) throws RemoteException;
	public void addNewUser(String userName) throws RemoteException;
	public Boolean isManager(String userName) throws RemoteException;
	public String[] getUserList() throws RemoteException;
	public Boolean haveIBeenKicked(String userName) throws RemoteException;
	public Boolean kickUser(String userName) throws RemoteException;
	public void managerDisconnect() throws RemoteException;
	public Boolean isManagerDisconnected() throws RemoteException;
	public void clientDisconnect(String userName) throws RemoteException;
	public Boolean resetWhiteBoard() throws RemoteException;
	public Boolean drawWhiteBoard(List<Shape> whiteBoardContent) throws RemoteException;
	public List<Shape> getWhiteBoardContent() throws RemoteException;
	public List<String> getClientsWaitList() throws RemoteException;
	public void addClientToWaitList(String clientName) throws RemoteException;
	public Boolean isClientInDeclinedList(String clientName) throws RemoteException;
//	public void addClientToDeclinedList(String clientName) throws RemoteException;
	public Boolean isClientInWaitList(String clientName) throws RemoteException;
	public void acceptClient(String clientName) throws RemoteException;
	public void declineClient(String clientName) throws RemoteException;
}
