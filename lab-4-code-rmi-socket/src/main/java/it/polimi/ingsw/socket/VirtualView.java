package it.polimi.ingsw.socket;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView {
    void showUpdate(Integer number) throws IOException;
    void reportError(String details) throws IOException;
}
