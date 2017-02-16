import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.io.*;
import java.io.Console;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client implements IM_Client {

    static String username;
    static Registry registry;

    public Client() {

    }

    public void receiveIM(String message, String sender) throws RemoteException {
        System.out.println("You've received a message from " + sender + ": ");
        System.out.println(message);
    }

    public String getUsername() throws RemoteException {
        return username;
    }

    private static void setUsernameAndRegister(Console c, IM_Client clientStub, IM_Server serverStub) {
        System.out.println("What username would you like to register with the server?");
        username = c.readLine();
        try {
            boolean response = serverStub.registerUser(username);
            if (response) {
                System.out.println("You have successfully registered your username.");
                registry.bind(username, clientStub);
            } else {
                System.out.println("There was a problem with your registration. Please try another username. Press q to quit.");
                setUsernameAndRegister(c,clientStub,serverStub);
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
            registry = LocateRegistry.getRegistry();
            IM_Server serverStub = (IM_Server) registry.lookup("IM_Server");
            Client obj = new Client();
            IM_Client clientStub = (IM_Client) UnicastRemoteObject.exportObject(obj, 0);
            Console console = System.console();
            System.out.println("Press q at any time to quit.");
            setUsernameAndRegister(console,clientStub,serverStub);
            while(true) {
                System.out.println("while");
                if (console.readLine().equals("get")) {
                    ArrayList<String> users = serverStub.viewUsers();
                    System.out.println(users.toString());
                }
                if (console.readLine().equals("send-message")) {
                    System.out.println("To: ");
                    String recipient = console.readLine();
                    System.out.println("Type your message: ");
                    String message = console.readLine();
                    serverStub.sendIM(message,recipient,username);
                }
                if (console.readLine().equals("q"))  {
                    System.out.println("Goodbye.");
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}