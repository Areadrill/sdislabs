package Server;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;


public class Server {
	
	private static Hashtable<String, String> db = new Hashtable<String, String>();
	
	public static void main(String[] args){
		
		/*for(String str: args){
			System.out.println(str);
		}*/
		
		DatagramSocket dSock = null;
		MulticastSocket mSock = null;
		try {
			dSock = new DatagramSocket(Integer.parseInt(args[0]));
			dSock.setSoTimeout(1000);
			
			mSock = new MulticastSocket(Integer.parseInt(args[2]));
			mSock.joinGroup(InetAddress.getByName(args[1]));
		} catch (SocketException e) {
			System.out.println("SERVER: Could not create sockets, exiting now.");
			return;
		}
		catch(NumberFormatException e){
			System.out.println("SERVER: Argument for port number is not a number");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		String broadcastMsg = null;
		try {
			broadcastMsg = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		broadcastMsg += (" " + args[0]);
		
		
		
		System.out.println("Server is running");
		while(true){
			DatagramPacket mPack;
			try {
				mPack = new DatagramPacket(broadcastMsg.getBytes(), broadcastMsg.length(), InetAddress.getByName(args[1]), Integer.parseInt(args[2]));
				mSock.send(mPack);
			} catch (NumberFormatException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (UnknownHostException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			DatagramPacket dPack = null;
			byte[] buf = new byte[275];
			
			try {
				dPack = new DatagramPacket(buf, buf.length);
				dSock.receive(dPack);
			}catch (SocketTimeoutException e){
				System.out.println("SERVER: Receive timed out, restarting loop");
				continue;
			}
			catch (IOException e) {
				System.out.println("SERVER: Something went wrong while receiving messages");
				return;
			}
			
			
			String dataReceived = null;
			
			try {
				dataReceived = new String(buf, "UTF-8").trim();
			} catch (UnsupportedEncodingException e) {
				System.out.println("SERVER: Data received was not text, resuming operation.");
			}
			
			if(dataReceived.equals(""));
			
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
				if(goodCommand && (!checkValidPlate(rec2[1]) || rec2[2].length() > 256)){
					System.out.println("SERVER: Plate is not in the right format or owner name is too big");
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
					System.out.println("SERVER: Could not send response to client, resuming operation");
				}
				
				break;
			case "look":
				if(rec2.length != 2){
					System.out.println("SERVER: Lookup request with wrong number of arguments");
					goodCommand = false;
				}
				if(rec2[1].length() > 256){
					System.out.println("SERVER: Owner name is too big");
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
	
	public static boolean checkValidPlate(String plate){
		return Pattern.matches("([0-9A-Z]{2}-){2}[0-9A-Z]{2}", plate);
	}
}
	
	