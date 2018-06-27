import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import message.AstroMessage;
import network.RMI;
import utils.AstroProperties;

public class Server implements RMI {
    public Server() {
    }

    @Override
    public AstroMessage messaging(AstroMessage message) {
        return message;
    }

    /**
     * getRegistry() 수행 시, gc가 registry 정리해버림.
     * createRegistry(port)로 수행할 것
     */
    public static void main(String[] args) {
        Server server = new Server();
        String serverName = AstroProperties.getProperty("server.name");

        try {
            RMI stub = (RMI) UnicastRemoteObject.exportObject(server, 1099);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind(serverName, stub);
            System.out.println("Server test ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
