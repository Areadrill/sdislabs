package Server;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;


public class Server {
	
	private static Hashtable<String, String> db = new Hashtable<String, String>();
	
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
			
			handleReceived(dataReceived, dSock, dPack);
		}
	}
	
	public static int register(String plate, String owner){
		if(db.containsKey(plate)){
			return -1;
		}
		db.put(plate, owner);
		return db.size();
	}
	
	public static String lookup(String plate){
		if(db.containsKey(plate)){
			return db.get(plate);
		}
		return "NOT_FOUND";
	}
	
	public static void handleReceived(String received, DatagramSocket dSock, DatagramPacket dPack){
		if(received == null){
			return;
		}
		
		String[] rec2 = received.split(" ");
		boolean goodCommand = true;
		
		switch(rec2[0]){
			case "reg":
				if(rec2.length != 3){
					System.out.println("SERVER: Register request with wrong number of arguments");
					goodCommand = false;
				}
				
				DatagramPacket dPackResp = null;
				if(goodCommand){
					int resp = register(rec2[1], rec2[2]);
					
					dPackResp = new DatagramPacket(new Integer(resp).toString().getBytes(), new Integer(resp).toString().length(),
							dPack.getAddress(), dPack.getPort());
				}
				else{
					dPackResp = new DatagramPacket("-1".getBytes(), 2, dPack.getAddress(), dPack.getPort());
				}
				
				try {
					dSock.send(dPackResp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
			case "look":
				if(rec2.length != 2){
					System.out.println("SERVER: Lookup request with wrong number of arguments");
					goodCommand = false;
				}
				
				if(goodCommand){
					String owner = lookup(rec2[1]);
					
					dPackResp = new DatagramPacket(owner.getBytes(), owner.length(),
							dPack.getAddress(), dPack.getPort());
				}
				else{
					dPackResp = new DatagramPacket("ERROR_NOT_CORRECT_COMMAND".getBytes(), "ERROR_NOT_CORRECT_COMMAND".length(), dPack.getAddress(), dPack.getPort());
				}
				
				try {
					dSock.send(dPackResp);
				} catch (IOException e) {
					System.out.println("SERVER: Could not send response to client, resuming operation");
				}
				
				
			default:
				System.out.println("SERVER: Unknown request");
				break;
		}
			
		
		
		
	}
		
}
	
	