import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface IM_Client extends Remote {
    void receiveIM(String message, String sender) throws RemoteException;
    String getUsername() throws RemoteException;

}