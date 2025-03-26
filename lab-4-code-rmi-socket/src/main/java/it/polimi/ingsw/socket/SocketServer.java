package it.polimi.ingsw.socket;

import it.polimi.ingsw.controller.CounterController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketServer {
    final ServerSocket listenSocket;
    final CounterController controller;

    final List<SocketClientHandler> clients = new ArrayList<>();

    public SocketServer(ServerSocket listenSocket, CounterController controller) {
        this.listenSocket = listenSocket;
        this.controller = controller;
    }

    private void run() throws IOException {
        Socket clientSocket = null;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            InputStreamReader socketRx = new InputStreamReader(clientSocket.getInputStream());
            OutputStreamWriter socketTx = new OutputStreamWriter(clientSocket.getOutputStream());

            SocketClientHandler handler = new SocketClientHandler(this.controller, this, new BufferedReader(socketRx), new PrintWriter(socketTx));

            synchronized (this.clients) {
                clients.add(handler);
            }

            new Thread(() -> {
                try {
                    handler.runVirtualView();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    public void broadcastUpdate(Integer value) {
        // Potrei usare un canale
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showUpdate(value);
            }
        }
    }


    public static void main(String[] args) throws IOException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        ServerSocket listenSocket = new ServerSocket(port);

        new SocketServer(listenSocket, new CounterController()).run();
    }
}
