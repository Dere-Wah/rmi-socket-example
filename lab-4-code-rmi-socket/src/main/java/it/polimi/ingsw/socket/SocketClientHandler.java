package it.polimi.ingsw.socket;

import it.polimi.ingsw.controller.CounterController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class SocketClientHandler implements VirtualView {
    final CounterController controller;
    final SocketServer server;
    final BufferedReader input;
    final PrintWriter output;

    public SocketClientHandler(CounterController controller, SocketServer server, BufferedReader input, PrintWriter output) {
        this.controller = controller;
        this.server = server;
        this.input = input;
        this.output = output;
    }

    public void runVirtualView() throws IOException {
        String line;
        while ((line = input.readLine()) != null) {
            // Trovare un modo più corretto per invocare metodi
            // sul controller in base al messaggio che arriva!!!
            switch (line) {
                case "add" -> {
                    this.controller.add(Integer.parseInt(input.readLine()));
                    this.server.broadcastUpdate(this.controller.getState());
                }
                case "reset" -> {
                    this.controller.reset();
                    this.server.broadcastUpdate(this.controller.getState());
                }
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }

    @Override
    public void showUpdate(Integer number) {
        this.output.println("update");
        this.output.println(number);
        this.output.flush();
    }

    @Override
    public void reportError(String details) {
        this.output.println("error");
        this.output.println(details);
        this.output.flush();
    }
}
