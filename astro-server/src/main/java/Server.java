import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import network.RMI;

public class Server implements RMI {
    public Server() {
    }

    @Override
    public String messaging(String message) {
        return message + " - success";
    }

    /**
     * getRegistry() 수행 시, gc가 registry 정리해버림.
     * createRegistry(port)로 수행할 것
     * @param args
     */
    public static void main(String[] args) {
        Server server = new Server();
        try {
            RMI stub = (RMI) UnicastRemoteObject.exportObject(server, 1099);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("tester", stub);
            System.out.println("Server test ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
