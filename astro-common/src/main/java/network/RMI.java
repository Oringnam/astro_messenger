package network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import message.AstroMessage;

public interface RMI extends Remote {
    public AstroMessage messaging(AstroMessage message) throws RemoteException;
}
