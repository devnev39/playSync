package Connector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import Converter.Converter;

public class Connector {
    private static String getLocalIp() throws UnknownHostException{
        try {
            Enumeration<NetworkInterface> networkInterfaceList = NetworkInterface.getNetworkInterfaces();
            while(networkInterfaceList.hasMoreElements()){
                for(InterfaceAddress address : networkInterfaceList.nextElement().getInterfaceAddresses()){
                    if(address.getAddress().isSiteLocalAddress()){
                        return address.getAddress().getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }

    public static Socket getReachableSocket(int port) throws UnknownHostException{
        String[] split = getLocalIp().split("\\.");
        String rawIp = String.join(".", Converter.pop(split));
        for(int i=1;i<255;i++){
            String IP = rawIp+"."+i;
            try {
                Socket sck = new Socket();
                // sck.setSoTimeout(3);
                sck.connect(new InetSocketAddress(IP, port), 5);
                System.out.println("Server Reached : "+IP);
                return sck;
            } catch (IOException e) {
                System.out.print("Unreachable : "+IP+"\r");
            }
        }
        System.out.println("No server found in listening mode on "+port+" !");
        return null;
    }
}
