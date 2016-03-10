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


public class Server {
	
	private static Hashtable<String, String> db = new Hashtable<String, String>();
	
	public static void main(String[] args){
		
		/*for(String str: args){
			System.out.println(str);
		}*/
		
		ServerSocket servSock = null;
		try {
			servSock = new ServerSocket(Integer.parseInt(args[0]));
		} catch (NumberFormatException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Socket sSock = null;
	
		System.out.println("Server is running");
		while(true){
			try {
				
				sSock = servSock.accept();
				
				Thread sThread = new Thread(new ServerThread(sSock));
				sThread.start();
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static class ServerThread implements Runnable{
		Socket sock;
		BufferedReader sBufRead;
		PrintWriter sPrntWriter;
		byte[] msg;
		
		public ServerThread(Socket sock){
			System.out.println("New thread running");
			this.sock = sock;
		}
		
		
		@Override
		public void run() {
			try {
				sBufRead = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				sPrntWriter = new PrintWriter(sock.getOutputStream());
				
				String received = null;
				
				
				
				if(sBufRead.ready()){
					received = sBufRead.readLine();
				}
				System.out.println("Received: " + received);
				
				this.handleReceived(received, sPrntWriter);
				
				
			} catch (IOException e) {
				System.err.println("SERVER: Could not establish I/O channels, exiting now.");
				System.exit(-1);
			}
		}
		
		public int register(String plate, String owner){
			if(db.containsKey(plate)){
				return -1;
			}
			db.put(plate, owner);
			return db.size();
		}
		
		public String lookup(String plate){
			if(db.containsKey(plate)){
				return db.get(plate);
			}
			return "NOT_FOUND";
		}
		
		public void handleReceived(String received, PrintWriter writer){
			if(received == null){
				return;
			}
			
			System.out.println(received);
			
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
					
					if(goodCommand){
						int resp = register(rec2[1], rec2[2]);
						writer.println(Integer.toString(resp));
					}
					else{
						writer.println(Integer.toString(-1));
					}
					writer.flush();
					
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
						writer.println(owner);
					}
					else{
						writer.println("ERROR_NOT_CORRECT_COMMAND");
					}
					writer.flush();
				default:
					System.out.println("SERVER: Unknown request");
					break;
			}
			
			try {
				sock.shutdownInput();
				sock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		public boolean checkValidPlate(String plate){
			return Pattern.matches("([0-9A-Z]{2}-){2}[0-9A-Z]{2}", plate);
		}
		
	}
}
	
	