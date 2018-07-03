package network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import message.AstroMessage;

public interface RMI extends Remote {
    public AstroMessage messaging(AstroMessage message) throws RemoteException;
    public void messaging2(AstroMessage message) throws RemoteException;
}
