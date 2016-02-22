package Client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args){
		DatagramSocket dSock = null;
		
		try {
			dSock = new DatagramSocket(8888);
		} catch (SocketException e) {
			System.out.println("CLIENT: Could not create socket, exiting now");
			return;
		}
		
		//testing...
		try {
			dSock.connect(InetAddress.getByName("localhost"), 8080);
			DatagramPacket dPack = new DatagramPacket("ola biba tudo beim".getBytes(), 8);
			dSock.send(dPack);			
		} catch (UnknownHostException e) {
			System.out.println("CLIENT: Could not connect to server, exiting now");
			return;
		} catch (IOException e) {
			System.out.println("CLIENT: Could not send datagram, exiting now");
		}
	}
}
