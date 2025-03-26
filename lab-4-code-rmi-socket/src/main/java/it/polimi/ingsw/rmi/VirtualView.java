package it.polimi.ingsw.rmi;

import java.rmi.Remote;
// usata da rmi per lanciare eccezioni quando ci sono problemi di comunicazioni
import java.rmi.RemoteException;

public interface VirtualView extends Remote { ;
    void showUpdate(Integer number) throws RemoteException;
    void reportError(String details) throws RemoteException;
}
