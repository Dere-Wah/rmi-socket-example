package it.polimi.ingsw.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class VirtualSocketServer implements VirtualServer {
    final PrintWriter output;

    public VirtualSocketServer(BufferedWriter output) {
        this.output = new PrintWriter(output);
    }

    @Override
    public void add(Integer number) {
        output.println("add");
        output.println(number);
        output.flush();
    }

    @Override
    public void reset() {
        output.println("reset");
        output.flush();
    }
}


