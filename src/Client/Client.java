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

public class Client {
	public static void main(String[] args) {
		
		for(String str: args){
			System.out.println(str);
		}
		
		
		Socket cSock = null;
		try {
			cSock = new Socket(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
			BufferedReader cBufRead = new BufferedReader(new InputStreamReader(cSock.getInputStream()));
			PrintWriter cPrntWriter = new PrintWriter(cSock.getOutputStream());
			
			String message = args[2];
			for(int i = 3; i < args.length; i++){
				message += (" " + args[i]);
			}
			
			System.out.println("Gonna send: " + message);
			cPrntWriter.println(message);
			cPrntWriter.flush();
			
			while(!cBufRead.ready()){
				;
			}
			
			String response = cBufRead.readLine();
			
			System.out.println(response);
			
			cSock.shutdownOutput();
			
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
}
