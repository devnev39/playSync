package TestClient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import Converter.Converter;

public class Main {
    public static void main(String[] args) throws UnknownHostException, IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter IP : ");
        String endpoint = sc.nextLine();
        sc.close();
        try (Socket sck = new Socket(endpoint,8888)) {
            System.out.println("Connected !");

            while(true){
                byte[] buff = new byte[256];
                sck.getInputStream().read(buff,0,buff.length);
                long t1 = System.currentTimeMillis();
                System.out.println(new String(buff));
                System.out.println(t1);
                sck.getOutputStream().write(Converter.LongToBytes(t1));
            }
        }
    }
}
