import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public Server() {
        System.out.println("hello Server");

        try {
            ServerSocket server = null;
            Socket client;

            server = new ServerSocket(8080);

            while(true) {
                client = server.accept();

                InputStream inputStream = client.getInputStream();
                OutputStream outputStream = client.getOutputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

                String message = dataInputStream.readUTF();
                dataOutputStream.writeUTF("first response");
                System.out.println(message);

                inputStream.close();
                outputStream.close();
                outputStream.flush();
                dataInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();
                client.close();

                Thread.sleep(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void serverTest() {
        Server server = new Server();
    }
}
