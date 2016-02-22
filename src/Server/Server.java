package Server;
import java.util.Queue;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;


public class Server {
	
	private static Queue<Vehicle> db;
	
	public static void main(String[] args){
		DatagramSocket dSock = null;
		try {
			dSock = new DatagramSocket(8080);
		} catch (SocketException e) {
			System.out.println("SERVER: Could not create socket, exiting now.");
			return;
		}
		while(true){
			System.out.println("Server is running");
			DatagramPacket dPack = null;
			byte[] buf = new byte[256];
			
			try {
				dPack = new DatagramPacket(buf, buf.length);
				dSock.receive(dPack);
			} catch (IOException e) {
				System.out.println("SERVER: Something went wrong while receiving messages");
				return;
			}
			
			String dataReceived = null;
			
			try {
				dataReceived = new String(buf, "UTF-8").trim();
			} catch (UnsupportedEncodingException e) {
				System.out.println("SERVER: Data received was not text, resuming operation.");
			}
			
			handleReceived(dataReceived);
		}
	}
	
	public static int register(String plate, String owner){
		for(Vehicle v: db){
			if(v.getPlate().equals(plate)){
				return -1;
			}
		}
		
		db.add(new Vehicle(plate, owner));
		return db.size();
	}
	
	public static String lookup(String plate){
		for(Vehicle v: db){
			if(v.getPlate().equals(plate)){
				return v.getOwner();
			}
		}
		return "NOT_FOUND";
	}
	
	public static void handleReceived(String received){
		if(received == null){
			return;
		}
		
		String[] rec2 = received.split(" ");
		//faltam as respostas ao cliente
		switch(rec2[0]){
			case "reg":
				if(rec2.length != 3){
					System.out.println("SERVER: Register request with wrong number of arguments");
				}
				int resp = register(rec2[1], rec2[2]);
				break;
			case "look":
				if(rec2.length != 2){
					System.out.println("SERVER: Lookup request with wrong number of arguments");
				}
				String owner = lookup(rec2[1]);
			default:
				System.out.println("SERVER: Unknown request");
				break;
		}
			
		
		
		
	}
		
}
	
	