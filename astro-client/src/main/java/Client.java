import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public Client() {
        System.out.println("hello Client");

        try {
            Socket socket = new Socket("localhost", 8080);

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            dataOutputStream.writeUTF("first message");
            String response = dataInputStream.readUTF();

            System.out.println(response);

            inputStream.close();
            outputStream.close();
            outputStream.flush();
            dataInputStream.close();
            dataOutputStream.flush();
            dataOutputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void clientTest() {
        Client client = new Client();
    }
}
