package network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import message.AstroMessage;

public interface RMI extends Remote {
    void messaging(AstroMessage message) throws RemoteException;
}
