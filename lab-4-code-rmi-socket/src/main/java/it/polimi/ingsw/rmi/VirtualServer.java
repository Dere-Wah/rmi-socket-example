package it.polimi.ingsw.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {

    void connect(VirtualView client) throws RemoteException;
    void add(Integer number) throws RemoteException;
    void reset() throws RemoteException;
}
