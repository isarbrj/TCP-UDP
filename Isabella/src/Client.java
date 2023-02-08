import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();

        Scanner sc = new Scanner(System.in);
        System.out.print("Insira a questão ou Escreva Q para receber as questões: ");
        String questao = sc.nextLine();

        byte[] message = questao.getBytes();

        InetAddress address = InetAddress.getByName("localhost");
        DatagramPacket packet = new DatagramPacket(message, message.length, address, 9876);
        socket.send(packet);
        byte[] buf = new byte[1024];
        DatagramPacket packet1 = new DatagramPacket(buf, buf.length);
        socket.receive(packet1);
        String received = new String(packet1.getData(), 0, packet1.getLength());
        System.out.println(received);
        socket.close();
    }
}
