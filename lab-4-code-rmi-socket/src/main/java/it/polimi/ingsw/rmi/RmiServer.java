package it.polimi.ingsw.rmi;

import it.polimi.ingsw.controller.CounterController;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RmiServer implements VirtualServer {
    final CounterController controller;
    final List<VirtualView> clients = new ArrayList<>();
/*
    final BlockingQueue<Integer> updates = new ArrayBlockingQueue<>(10);

    private void broadcastUpdateThread() throws InterruptedException, RemoteException {
        while (true) {
            Integer update = updates.take();
            synchronized (this.clients) {
                for(var c : clients) {
                    c.showUpdate(update);
                }
            }
        }
    }
 */

    public RmiServer(CounterController controller) {
        this.controller = controller;
    }

    public static void main(String[] args) throws RemoteException {
        final String serverName = "AdderServer";

        VirtualServer server = new RmiServer(new CounterController());
        VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(server, 0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName, stub);
        System.out.println("server bound.");

    }

    @Override
    public void connect(VirtualView client) throws RemoteException{
        synchronized (this.clients) {
            this.clients.add(client);
        }
    }


    @Override
    public void add(Integer number) throws RemoteException {
        System.err.println("add request received");
        this.controller.add(number);
        Integer currentState = this.controller.getState();
        /*
        try {
            updates.put(currentState);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        */
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdate(currentState);
            }
        }
    }


    @Override
    public void reset() throws RemoteException {
        System.err.println("reset request received");
        boolean result = this.controller.reset();
        synchronized (this.clients) {
            if (result) {
                Integer currentState = this.controller.getState();
                for (var c : this.clients) {
                    c.showUpdate(currentState);
                }
            } else {
                for (var c : this.clients) {
                    c.reportError("already reset");
                }
            }
        }
    }

}
