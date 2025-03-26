package it.polimi.ingsw.socket;

import java.io.IOException;
import java.rmi.RemoteException;
public interface VirtualServer {
    public void add(Integer number) throws IOException;
    public void reset() throws IOException;
}
