package Client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) {
		
//		for(String str: args){
//			System.out.println(str);
//		}
		
		DatagramSocket dSock = null;
		MulticastSocket mSock = null;

		try {
			mSock = new MulticastSocket(Integer.parseInt(args[1]));
		} catch (SocketException e) {
			System.out.println("CLIENT: Could not create socket, exiting now");
			return;
		} catch (NumberFormatException e) {
			System.out.println("CLIENT: Port argument does not apear to be a number");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] mbuf = new byte[256];
		DatagramPacket mPack = new DatagramPacket(mbuf, mbuf.length);
		DatagramPacket dPack = null;
		try {
			mSock.joinGroup(InetAddress.getByName(args[0]));
			
			String message = args[2];
			for(int i = 3; i < args.length; i++){
				message += (" " + args[i]);
			}
			
			dPack = new DatagramPacket(message.getBytes(), message.length());
		} catch (UnknownHostException e) {
			System.out.println("CLIENT: Could not connect to server, exiting now");
			return;
		} catch (IOException e) {
			System.out.println("CLIENT: Could not send datagram, exiting now");
		}
		
		try {
			mSock.receive(mPack);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String mReceived = null;
		try {
			mReceived = new String(mbuf, "UTF-8").trim();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(mReceived);
		String[] serverData = mReceived.split(" ");
		
		try {
			dSock = new DatagramSocket(8888);
			dSock.connect(InetAddress.getByName(serverData[0]), Integer.parseInt(serverData[1]));
			
			dSock.send(dPack);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
