package remote;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteWhiteBoard extends Remote{
	public void printHello() throws RemoteException;
	public Boolean hasManager() throws RemoteException;
	public Boolean userNameAlreadyExist(String userName) throws RemoteException;
	public void addNewUser(String userName) throws RemoteException;
	public Boolean isManager(String userName) throws RemoteException;
	public String[] getUserList() throws RemoteException;
	public Boolean haveIBeenKicked(String userName) throws RemoteException;
	public Boolean kickUser(String userName) throws RemoteException;
}
