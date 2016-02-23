package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) {
		
//		for(String str: args){
//			System.out.println(str);
//		}
		
		DatagramSocket dSock = null;

		try {
			dSock = new DatagramSocket(8888);
		} catch (SocketException e) {
			System.out.println("CLIENT: Could not create socket, exiting now");
			return;
		}

		
		try {
			dSock.connect(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
			
			String message = args[2];
			for(int i = 3; i < args.length; i++){
				message += (" " + args[i]);
			}
			
			DatagramPacket dPack = new DatagramPacket(message.getBytes(), message.length());
			dSock.send(dPack);
		} catch (UnknownHostException e) {
			System.out.println("CLIENT: Could not connect to server, exiting now");
			return;
		} catch (IOException e) {
			System.out.println("CLIENT: Could not send datagram, exiting now");
		}
		
		
		byte[] buf = new byte[256];
		DatagramPacket dPackResp = new DatagramPacket(buf, buf.length);
		try {
			dSock.receive(dPackResp);
			String resp = new String(buf, "UTF-8").trim();
			System.out.println(resp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
