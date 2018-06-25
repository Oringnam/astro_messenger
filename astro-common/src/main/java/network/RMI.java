package network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI extends Remote {
    public String messaging(String message) throws RemoteException;
}
