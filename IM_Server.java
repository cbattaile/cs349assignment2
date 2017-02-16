import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface IM_Server extends Remote {
    boolean registerUser(String username) throws RemoteException;
    boolean sendIM(String message, String recipient, String sender) throws RemoteException;
    ArrayList<String> viewUsers() throws RemoteException;
}