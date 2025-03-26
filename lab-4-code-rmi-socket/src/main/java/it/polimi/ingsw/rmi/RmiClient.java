package it.polimi.ingsw.rmi;

import java.util.Scanner;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiClient extends UnicastRemoteObject implements VirtualView {
    final VirtualServer server;

    public RmiClient(VirtualServer server) throws RemoteException {
        this.server = server;
    }

    private void run() throws RemoteException {
        this.server.connect(this);
        this.runCli();
    }

    private void runCli() throws RemoteException {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            int command = scan.nextInt();

            if (command == 0) {
                server.reset();
            } else {
                server.add(command);
            }
        }
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        final String serverName = "AdderServer";
        Registry registry = LocateRegistry.getRegistry(args[0], 1234);
        VirtualServer server = (VirtualServer) registry.lookup(serverName);
        new RmiClient(server).run();
    }

    @Override
    public void showUpdate(Integer number) throws RemoteException {
        // Attenzione alle data races
        System.out.print("\n= " + number + "\n> ");
    }

    @Override
    public void reportError(String details) throws RemoteException {
        System.err.print("\n[ERROR] " + details + "\n> ");
    }
}