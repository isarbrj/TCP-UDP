import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket welcomeSocket = new ServerSocket(9999);

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();

            new Thread(() ->  {

                String clientSentence = "";
                BufferedReader inFromClient = null;
                DataOutputStream outToClient;
                try {
                    inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream(), StandardCharsets.UTF_8));
                    outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    clientSentence = inFromClient.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String fileName = "";

                System.out.println("Waiting client");

                boolean read = false;
                for(int i = 0; i < clientSentence.length(); i++) {
                    if(read) {
                        fileName += clientSentence.charAt(i);
                        read = false;
                    } else {
                        read = true;
                    }
                }

                System.out.println(fileName);

                System.out.println("Recebido");

                File folder = new File("./");

                File[] files = folder.listFiles();

                Boolean temArquivo = false;
                File arquivoDevolver = null;

                for(File file : files) {
                    if(file.getName().contains(fileName)) {
                        temArquivo = true;
                        arquivoDevolver = file;
                    }
                }

                if(temArquivo) {
                    try {
                        outToClient.writeChars("Sucesso" + "\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    byte[] byteArray = new byte[(int) arquivoDevolver.length()];
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(arquivoDevolver);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    try {
                        bis.read(byteArray, 0, byteArray.length);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    OutputStream os = outToClient;
                    try {
                        os.write(byteArray, 0, byteArray.length);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        os.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        connectionSocket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        outToClient.writeChars("Fracasso" + "\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }).start();

        }
    }
}
