package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) {
		DatagramSocket dSock = null;

		try {
			dSock = new DatagramSocket(8888);
		} catch (SocketException e) {
			System.out.println("CLIENT: Could not create socket, exiting now");
			return;
		}

		
		try {
			dSock.connect(InetAddress.getByName("localhost"), 8080);
			DatagramPacket dPack = new DatagramPacket("look 1".getBytes(), "look 1".length());
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
