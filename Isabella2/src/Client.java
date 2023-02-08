import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 9999);


        byte[] byteArray = new byte[10000];
        InputStream is = socket.getInputStream();

        String modifiedSentence;
        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        Scanner sc = new Scanner(System.in);
        System.out.print("Insira o nome do arquivo completo: ");
        String questao = sc.nextLine();

        outToServer.writeChars(questao + "\n");

        modifiedSentence = inFromServer.readLine();

        System.out.println(modifiedSentence);

        FileOutputStream fos = new FileOutputStream("./" + "arquivo." + questao.split("\\.")[1]);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        String received = "";

        boolean read = false;
        for(int i = 0; i < modifiedSentence.length(); i++) {
            if(read) {
                received += modifiedSentence.charAt(i);
                read = false;
            } else {
                read = true;
            }
        }

        if(received.equals("Sucesso")) {
            int bytesRead = is.read(byteArray, 0, byteArray.length);
            bos.write(byteArray, 0, bytesRead);
            bos.flush();
            socket.close();
        } else {
            System.out.println("Não foi possível encontrar o arquivo");
            socket.close();
        }

    }
}
