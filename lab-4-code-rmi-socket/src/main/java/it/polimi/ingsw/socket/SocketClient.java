package it.polimi.ingsw.socket;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class SocketClient implements VirtualView {
    final BufferedReader input;
    final VirtualSocketServer server;

    protected SocketClient(BufferedReader input, BufferedWriter output) {
        this.input = input;
        this.server = new VirtualSocketServer(output);
    }

    private void run() throws RemoteException {
        new Thread(() -> {
            try {
                runVirtualServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        runCli();
    }

    private void runVirtualServer() throws IOException {
        String line;

        while ((line = input.readLine()) != null) {
            // Trovare un modo più corretto per invocare metodi
            // sul controller in base al messaggio che arriva!!!
            switch (line) {
                case "update" -> this.showUpdate(Integer.parseInt(input.readLine()));
                case "error" -> this.reportError(input.readLine());
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
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

    @Override
    public void showUpdate(Integer number) {
        System.out.print("\n= " + number + "\n> ");
    }

    @Override
    public void reportError(String details) {
        System.err.print("\n[ERROR] " + details + "\n> ");
    }

    public static void main(String[] args) throws IOException, UnknownHostException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        Socket serverSocket = new Socket(host, port);

        InputStreamReader socketRx = new InputStreamReader(serverSocket.getInputStream());
        OutputStreamWriter socketTx = new OutputStreamWriter(serverSocket.getOutputStream());

        new SocketClient(new BufferedReader(socketRx), new BufferedWriter(socketTx)).run();
    }
}
