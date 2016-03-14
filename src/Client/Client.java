package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Server.RemoteInterface;

public class Client {
	
	private Client() {};
	
	public static void main(String[] args) {
		
		/*for(String str: args){
			System.out.println(str);
		}*/
		
		try {
			Registry reg = LocateRegistry.getRegistry(args[0]);
			RemoteInterface stub = (RemoteInterface) reg.lookup(args[1]);
			
			if(args[2].equals("reg") && args.length == 5){
				stub.register(args[3], args[4]);
			}
			else if(args[2].equals("look") && args.length == 4){
				stub.lookup(args[3]);
			}
			
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.err.println("CLIENT: Registry lookup produced no results");
		}
		
	}	
}
