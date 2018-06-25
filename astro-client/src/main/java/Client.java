import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import network.RMI;
import utils.Basic;

public class Client {
    public Client() {
    }

    public static void main(String[] args) {
        String host = Basic.getIp();

        if (host == null) {
            System.err.println("localhost get err");
            return;
        }

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            RMI stub = (RMI) registry.lookup("tester");
            String response = stub.messaging("message for test");
            System.out.println("response : " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
