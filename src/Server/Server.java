package Server;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Server implements RemoteInterface{
	
	private static Hashtable<String, String> db = new Hashtable<String, String>();
	
	public Server(){}
	
	public static void main(String[] args) throws RemoteException{
		
		/*for(String str: args){
			System.out.println(str);
		}*/
		
		Server server = new Server();
		RemoteInterface stub = (RemoteInterface) UnicastRemoteObject.exportObject(server, 0);
		
		Registry reg = LocateRegistry.getRegistry();
		try {
			reg.bind(args[0], stub);
		} catch (AlreadyBoundException e) {
			System.err.println("SERVER: Tried to bind what was already bound");
		}
		
	}
	
	public int register(String plate, String owner) throws RemoteException{
		if(db.containsKey(plate)){
			return -1;
		}
		db.put(plate, owner);
		return db.size();
	}
	
	public String lookup(String plate) throws RemoteException{
		if(db.containsKey(plate)){
			return db.get(plate);
		}
		return "NOT_FOUND";
	}
	
	public boolean checkValidPlate(String plate){
		return Pattern.matches("([0-9A-Z]{2}-){2}[0-9A-Z]{2}", plate);
	}
}
	
	