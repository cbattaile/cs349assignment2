import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
        
public class Server implements IM_Server {

    ArrayList<String> userList;
    static Registry registry;

    public Server() {
        userList = new ArrayList<String>();
    }

    public boolean registerUser(String username) throws RemoteException {
        if (userList.contains(username)) {
            System.out.println("This username already exists.");
            return false;
        } else {
            userList.add(username);
            System.out.println("A new username has been registered.");
            return true;
        }
    }

    public ArrayList<String> viewUsers() throws RemoteException {
        return userList;
    }

    public boolean sendIM(String message, String recipient, String sender) throws RemoteException {
        if (!userList.contains(recipient)) {
            System.out.println("The user you are trying to message does not exist.");
            return false;
        } else {
            //call receiveIM for client with username recipient
            try {
                IM_Client stubClient = (IM_Client) registry.lookup(recipient);
                stubClient.receiveIM(message, sender);
                return true;
            } catch (Exception e) {
                System.err.println("Server exception: " + e.toString());
                e.printStackTrace();
                return false;
            }
        }
    }
        
    public static void main(String args[]) {
        
        try {
            Server obj = new Server();
            IM_Server serverStub = (IM_Server) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            registry = LocateRegistry.getRegistry();
            registry.bind("IM_Server", serverStub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}