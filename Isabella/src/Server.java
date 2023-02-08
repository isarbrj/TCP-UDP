import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {

    public static void main(String[] args) throws IOException {

        DatagramSocket socket = new DatagramSocket(9876);

        ArrayList<String> questoes = new ArrayList<>();
        questoes.add("VFVFV");
        questoes.add("VVFVV");
        questoes.add("FFFFV");
        questoes.add("VVVF");
        questoes.add("VVVV");

        while(true) {
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            new Thread(() -> {

                String received = new String(packet.getData(), 0, packet.getLength());
                String[] parts;

                if(received.contains(";")) {
                    parts = received.split(";");
                }
                else {
                    String questoesCliente = "";

                    for (int i = 0; i < questoes.size(); i++) {
                        questoesCliente += "Questao " + (i+1) + ", com tamanho " + questoes.get(i).length() + "\n";
                    }

                    byte[] response = questoesCliente.getBytes();
                    DatagramPacket responsePacket = new DatagramPacket(response, response.length, packet.getAddress(), packet.getPort());
                    try {
                        socket.send(responsePacket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }



                Integer qtdQuestao = Integer.parseInt(parts[1]);

                String questaoUsada = questoes.get(Integer.parseInt(parts[0])-1);

                int qtdAcertos = 0;
                for(int i = 0; i < parts[2].length(); i++) {
                    if(questaoUsada.charAt(i) == parts[2].charAt(i)) {
                        qtdAcertos++;
                    }
                }

                String resposta = "";
                resposta += parts[0];
                resposta += ";";
                resposta += qtdAcertos;
                resposta += ";";
                resposta += qtdQuestao-qtdAcertos;

                byte[] response = resposta.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(response, response.length, packet.getAddress(), packet.getPort());
                try {
                    socket.send(responsePacket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }


    }
}
