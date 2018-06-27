import java.nio.ByteBuffer;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import message.AstroCoder;
import message.AstroMessage;
import network.RMI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AstroProperties;
import utils.Basic;

public class Client {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private RMI stub;

    public Client() {
    }

    public boolean connect(String host) {
        if (host == null) {
            logger.error("Server not founded");
            return false;
        }

        String serverName = AstroProperties.getProperty("server.name");

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            stub = (RMI) registry.lookup(serverName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean send(AstroMessage message) {
        try {
            Object response = stub.messaging(message);
            logger.info("response : {}", response.toString());
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        String host = Basic.getIp();

        Client client = new Client();
        client.connect(host);

        String message = "testMessage";
        byte[] messageBytes = message.getBytes();
        Long time = System.currentTimeMillis();

        AstroMessage astroMessage = new AstroMessage();

        astroMessage.setDatetime(time);
        astroMessage.setIndex(0);
        astroMessage.setTopic("test");
        astroMessage.setMessage(message);

        ByteBuffer buf = ByteBuffer.allocate(8);
        byte[] timeBytes = buf.putLong(time).array();
        String uuid = AstroCoder.getUniqueId(timeBytes, messageBytes);

        astroMessage.setUuid(uuid);

        client.send(astroMessage);
    }
}
