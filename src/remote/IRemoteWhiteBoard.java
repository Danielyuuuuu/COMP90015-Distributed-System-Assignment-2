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
}
